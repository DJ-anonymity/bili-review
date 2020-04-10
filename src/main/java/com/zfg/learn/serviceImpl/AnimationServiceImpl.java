package com.zfg.learn.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfg.learn.common.ServerResponse;
import com.zfg.learn.dao.AnimationMapper;
import com.zfg.learn.dao.RatingMapper;
import com.zfg.learn.pojo.Animation;
import com.zfg.learn.pojo.Rating;
import com.zfg.learn.service.AnimationService;
import com.zfg.learn.until.CatchApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
public class AnimationServiceImpl implements AnimationService {
    @Autowired
    private AnimationMapper animationMapper;
    @Autowired
    private RatingMapper ratingMapper;
    private CatchApi catchApi = new CatchApi();

    @Override
    public ServerResponse list(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Animation> animations = animationMapper.selectAllAnimation();
        PageInfo<Animation> pageInfo = new PageInfo<>(animations);
        return ServerResponse.createBySuccess(pageInfo);
    }

    @Override
    public ServerResponse findAnimationByMedia_id(Integer media_id) {
        Animation animation = animationMapper.selectAnimationByMedia_id(media_id);
        if (animation == null){
            return ServerResponse.createByErrorMessage("没找到");
        }
        return ServerResponse.createBySuccess(animation);
    }

    @Transactional
    @Override
    public ServerResponse pullNewAnimation(Integer media_id) throws IOException {
        if (animationMapper.selectAnimationByMedia_id(media_id) != null){
            return ServerResponse.createByErrorMessage("该剧已经拉取过");
        }
        String api = "https://api.bilibili.com/pgc/review/user?media_id="+media_id;
        String dataJson = catchApi.getJsonFromApi(api);
        System.out.println(dataJson);
        JSONObject jsonObject = JSON.parseObject(dataJson);
        JSONObject result = jsonObject.getJSONObject("result");
        Animation animation = result.getObject("media",Animation.class);
        animationMapper.insertAnimation(animation);
        Rating rating = animation.getRating();
        rating.setMedia_id(media_id);
        System.out.println(rating);
        ratingMapper.insertRating(rating);
        return ServerResponse.createBySuccess();
    }
}
