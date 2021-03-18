package com.zfg.learn.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfg.learn.common.Const;
import com.zfg.learn.common.ServerResponse;
import com.zfg.learn.dao.AnimationMapper;
import com.zfg.learn.dao.LongReviewMapper;
import com.zfg.learn.dao.StatMapper;
import com.zfg.learn.dao.BiliUserMapper;
import com.zfg.learn.model.po.LongReview;
import com.zfg.learn.model.po.ReviewPageInfo;
import com.zfg.learn.model.po.Stat;
import com.zfg.learn.model.po.BiliUser;
import com.zfg.learn.service.LongReviewService;
import com.zfg.learn.until.CatchApi;
import com.zfg.learn.until.SortUntil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class LongReviewServiceImpl implements LongReviewService {
    @Autowired
    private LongReviewMapper longReviewMapper;
    @Autowired
    private BiliUserMapper biliUserMapper;
    @Autowired
    private StatMapper statMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private AnimationMapper animationMapper;

    private CatchApi catchApi = new CatchApi();
    private SortUntil sortUntil = new SortUntil();

    //向b站的api获取某番剧的全部评价，并存在本地
    @Transactional
    @Override
    public ServerResponse pullAllLongReviewFromBiliApi(Integer media_id) throws IOException {
        //如果该id的动漫已经拉取过全部评论了，则中断拉取并提醒只需更新就行了
        if (animationMapper.selectLongReviewPersistenceMarkByMedia_id(media_id) == Const.PERSISTENCE){
            return ServerResponse.createByErrorMessage("该评论已经拉取过了，无序重复拉取");
        }

        JSONObject jsonObject;
        String apiUrl;
        String reviewJson;
        ReviewPageInfo reviewPageInfo;
        List<LongReview> longReviewList;
        Long cursor = 1L;

        while (cursor != 0){
            //第一次访问的时候不需要页码
            if (cursor.equals(1L)){
                apiUrl = "https://api.bilibili.com/pgc/review/long/list?ps=30&sort=0&media_id="+media_id;
            } else {
                apiUrl = "https://api.bilibili.com/pgc/review/long/list?ps=30&sort=0&media_id="+media_id+"&cursor="+cursor;
            }
            reviewJson = catchApi.getJsonFromApi(apiUrl);
            System.out.println(reviewJson);
            //把json字符串转变成jsonObject
            jsonObject = JSON.parseObject(reviewJson);
            //获取分页信息
            reviewPageInfo = (ReviewPageInfo) jsonObject.getObject("data", ReviewPageInfo.class);
            System.out.println(reviewPageInfo);
            //设置下一页的页码
            cursor = reviewPageInfo.getNext();
            //从分页信息中取出数据
            longReviewList = reviewPageInfo.getList();
            //存进数据库
            if (longReviewList.size() > 0){
                ServerResponse serverResponse = insertLongReviews(longReviewList);
                if (serverResponse.getStatus() != 0){
                    return serverResponse;
                }
            }
        }
        //如果下一页为0，则说明已经爬取完全部数据
        if (cursor == 0) {
            //更改animation中的短评持久化状态
            animationMapper.updateLongReviewPersistenceMarkByMedia_id(Const.PERSISTENCE,media_id);
        }
        //拉取后，删除redis中关于评论数量的缓存
        redisTemplate.delete("review:quantity:"+media_id);
        return ServerResponse.createBySuccess();
    }

    //向b站的api获取某番剧的最新评价，并存在本地
    @Transactional
    @Override
    public ServerResponse pullNewLongReviewFromBiliApi(Integer media_id) throws IOException {

        //如果该id的动漫还没有持久化完毕，无法更新
        if (animationMapper.selectLongReviewPersistenceMarkByMedia_id(media_id) == Const.NO_PERSISTENCE){
            return ServerResponse.createByErrorMessage("请先持久化完毕");
        }

        Long cursor = 1L;
        String apiUrl;
        String reviewJson;
        JSONObject jsonObject;
        ReviewPageInfo reviewPageInfo;
        List<LongReview> reviewList;

        //把该番剧最新的长评的id取出来
        LongReview latestReview =longReviewMapper.selectLatestReviewByMedia_id(media_id);
        System.out.println("最新的长评为:"+latestReview);
        while(cursor != 0){
            if (cursor == 1L){
                apiUrl = "https://api.bilibili.com/pgc/review/long/list?ps=20&sort=1&media_id="+media_id;
            } else {
                apiUrl = "https://api.bilibili.com/pgc/review/long/list?ps=20&sort=1&media_id="+media_id+"&cursor="+cursor;
            }
            reviewJson = catchApi.getJsonFromApi(apiUrl);
            System.out.println(reviewJson);
            jsonObject = JSON.parseObject(reviewJson);
            reviewPageInfo = jsonObject.getObject("data",ReviewPageInfo.class);
            reviewList = reviewPageInfo.getList();
            if (reviewList.size() > 0){
                for (LongReview longReview:reviewList){
                    //如果评论创建的时间大于数据库中评论的最新时间，则插入该评论
                    if (longReview.getCtime() > latestReview.getMtime()){
                        System.out.println("要插入长评的为:"+longReview);
                        insertLongReview(longReview);
                    } else {
                        //如果创建时间比已持久化的最新评论小，但是最终修改时间比它大，执行长评的修改
                        if (longReview.getMtime() > latestReview.getMtime()){
                            //更新长评
                            System.out.println("要更新长评的为:"+longReview);
                            longReviewMapper.updateContentByReview_id(longReview);
                            //更新长评的点赞数
                            Stat stat = longReview.getStat();
                            stat.setArticle_id(longReview.getArticle_id());
                            statMapper.updateStatByReview_id(longReview.getStat());
                        } else {
                            //拉取后，删除redis中关于评论数量的缓存
                            redisTemplate.delete("review:quantity:"+media_id);
                            return ServerResponse.createBySuccess();
                        }
                    }

                }
            }
            cursor = reviewPageInfo.getNext();
        }

        return ServerResponse.createByError();
    }

    //把数据存进数据库,多条
    @Transactional
    @Override
    public ServerResponse insertLongReviews(List<LongReview> longReviewList) {
        if (longReviewList.size() == 0){
            return ServerResponse.createByError();
        }

        List<Stat> statList = new ArrayList<>();
        List<BiliUser> userList = new ArrayList<>();
        longReviewMapper.insertLongReviewList(longReviewList);
        for (LongReview longReview:longReviewList){
            //获取用户
            BiliUser user = longReview.getAuthor();
            userList.add(user);
            //把stat组成一个集合
            Stat stat = longReview.getStat();
            stat.setArticle_id(longReview.getArticle_id());
            statList.add(stat);
        }

        biliUserMapper.insertUserList(userList);
        statMapper.insertLongReviewStatList(statList);
        return ServerResponse.createBySuccess();
    }

    //把数据存进数据库,一条
    @Transactional
    @Override
    public ServerResponse insertLongReview(LongReview longReview) {
        if (longReview == null){
            return ServerResponse.createByError();
        }

        int status = longReviewMapper.insertLongReview(longReview);
        if (status > 0){
            /* 这里使用try来执行插入用户信息语句，这样因为主键mid重复报错就不会导致程序中断执行，不重复就会直接插入成功
             * 而且还不用再用select语句在几十w的数据中查询用户是否存在，节省了非常多的时间
             * */
            try {
                biliUserMapper.insertUser(longReview.getAuthor());
            } catch (Exception e){

            }
            //执行插入stat
            Stat stat = longReview.getStat();
            stat.setArticle_id(longReview.getArticle_id());
            status = statMapper.insertLongReviewStat(stat);
            if (!(status > 0)){
                //插入stat失败，删除已经存进数据库的长评，并返回错误
                longReviewMapper.deleteLongReviewByReview_id(longReview.getReview_id());
                ServerResponse.createByError();
            }
        } else {
            return ServerResponse.createByError();
        }

        return ServerResponse.createBySuccess();
    }

    //通过id删除存在本地的评论
    @Override
    public ServerResponse deleteLongReviewByReview_id(Integer review_id) {
        if (review_id == null){
            return ServerResponse.createByError();
        }
        //删除前先把长评内容查出来,方便等下失败时还原使用
        LongReview originalLongReview = longReviewMapper.selectLongReviewByReview_id(review_id);
        //删除长评
        int status = longReviewMapper.deleteLongReviewByReview_id(review_id);
        if (status > 0){
           //删除长评中的stat
           status = statMapper.deleteStatByArticle_id(originalLongReview.getArticle_id());
           if (status > 0 ){
               return ServerResponse.createBySuccess();
           }
        }
        return ServerResponse.createByError();
    }

    //查找对应番剧的评价
    @Override
    public ServerResponse list(Integer media_id, Integer score, Integer sort, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        //通过根据类的方法把排序变成可以拼接进sql中的字符串
        String sortType = sortUntil.convertToReviewSortType(sort);
        List<LongReview> longReviewList = longReviewMapper.selectReviewByMedia_id(media_id, score, sortType);
        PageInfo<LongReview> pageInfo = new PageInfo<>(longReviewList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    //在番剧里面通过关键字搜索内容
    @Override
    public ServerResponse searchReviewByKeyword(Integer media_id, String keyword, Integer score, Integer sort, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        String sortType = sortUntil.convertToReviewSortType(sort);
        List<LongReview> longReviewList = longReviewMapper.selectReviewByKeyWord(media_id, keyword,score,  sortType);
        PageInfo<LongReview> pageInfo = new PageInfo(longReviewList);
        return ServerResponse.createBySuccess(pageInfo);
    }


    @Override
    public ServerResponse searchReviewByMid(Integer mid, Integer sort, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        String sortType = sortUntil.convertToReviewSortType(sort);
        List<LongReview> longReviewList = longReviewMapper.selectReviewByMid(mid, sortType);
        PageInfo<LongReview> pageInfo = new PageInfo(longReviewList);
        return ServerResponse.createBySuccess(pageInfo);
    }
}
