package com.zfg.learn.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfg.learn.exception.ServiceException;
import com.zfg.learn.model.bo.AnimationReviewBo;
import com.zfg.learn.common.ServerResponse;
import com.zfg.learn.dao.AnimationMapper;
import com.zfg.learn.dao.LongReviewMapper;
import com.zfg.learn.dao.RatingMapper;
import com.zfg.learn.dao.ShortReviewMapper;
import com.zfg.learn.model.po.Animation;
import com.zfg.learn.model.po.Rating;
import com.zfg.learn.service.AnimationService;
import com.zfg.learn.until.CatchApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AnimationServiceImpl implements AnimationService {
    @Autowired
    private AnimationMapper animationMapper;
    @Autowired
    private RatingMapper ratingMapper;
    @Autowired
    private LongReviewMapper longReviewMapper;
    @Autowired
    private ShortReviewMapper shortReviewMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    private CatchApi catchApi = new CatchApi();

    @Override
    public ServerResponse list(Integer persistenceMark, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Animation> animations = animationMapper.selectAllAnimation(persistenceMark);
        PageInfo<Animation> pageInfo = new PageInfo<>(animations);
        return ServerResponse.createBySuccess(pageInfo);
    }

    //获取动漫的评论数量
    public AnimationReviewBo getReviewQuantity(Integer media_id) throws IOException {
        AnimationReviewBo animationReviewBo = new AnimationReviewBo();
        //先从redis中查询,若存在直接返回
        if ( redisTemplate.opsForValue().get("review:quantity:"+media_id) != null){
            animationReviewBo = (AnimationReviewBo) redisTemplate.opsForValue().get("review:quantity:"+media_id);
            return  animationReviewBo;
        }

        //获取短评已持久化的数量
        Integer shortReviewQuantity = shortReviewMapper.selectReviewQuantityByMedia_id(media_id);
        //获取长评已持久化的数量
        Integer longReviewQuantity = longReviewMapper.selectReviewQuantityByMedia_id(media_id);
        //获取长评的总数
        String api3 = "https://api.bilibili.com/pgc/review/long/list?media_id="+media_id;
        String dataJson3 = catchApi.getJsonFromApi(api3);
        Integer long_review_total = JSON.parseObject(dataJson3).getJSONObject("data").getInteger("total");
        //获取短评总数
        String api2 = "https://api.bilibili.com/pgc/review/short/list?media_id="+media_id;
        String dataJson2 = catchApi.getJsonFromApi(api2);
        Integer short_review_total = JSON.parseObject(dataJson2).getJSONObject("data").getInteger("total");
        animationReviewBo.setShort_review_finished(shortReviewQuantity);
        animationReviewBo.setLong_review_finished(longReviewQuantity);
        animationReviewBo.setShort_review_total(short_review_total);
        animationReviewBo.setLong_review_total(long_review_total);
        //存进redis中，并且设置6小时候过期
        redisTemplate.opsForValue().set("review:quantity:"+media_id, animationReviewBo, 6, TimeUnit.HOURS);
        return  animationReviewBo;
    }

    @Override
    public Animation findAnimationByMedia_id(Integer media_id) {
        Animation animation = animationMapper.selectAnimationByMedia_id(media_id);
        return animation;
    }

    @Transactional
    @Override
    public Animation pullNewAnimation(Integer media_id) throws IOException {
        if (animationMapper.selectAnimationByMedia_id(media_id) != null){
            throw new ServiceException("已经爬取过了");
        }

        String api = "https://api.bilibili.com/pgc/review/user?media_id="+media_id;
        String dataJson = catchApi.getJsonFromApi(api);
        JSONObject jsonObject = JSON.parseObject(dataJson);
        JSONObject result = jsonObject.getJSONObject("result");
        //获取番剧的基本信息
        Animation animation = result.getObject("media",Animation.class);
        animationMapper.insertAnimation(animation);
        //设置比分并插入数据库
        Rating rating = animation.getRating();
        rating.setMedia_id(media_id);
        ratingMapper.insertRating(rating);
        return animation;
    }
}
