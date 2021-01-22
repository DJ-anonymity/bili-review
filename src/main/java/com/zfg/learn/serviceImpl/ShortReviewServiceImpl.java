package com.zfg.learn.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfg.learn.common.Const;
import com.zfg.learn.common.RedisConst;
import com.zfg.learn.common.ServerResponse;
import com.zfg.learn.dao.AnimationMapper;
import com.zfg.learn.dao.ShortReviewMapper;
import com.zfg.learn.dao.StatMapper;
import com.zfg.learn.dao.BiliUserMapper;
import com.zfg.learn.model.po.*;
import com.zfg.learn.service.ShortReviewService;
import com.zfg.learn.until.CatchApi;
import com.zfg.learn.until.SortUntil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ShortReviewServiceImpl implements ShortReviewService {
    @Autowired
    private ShortReviewMapper shortReviewMapper;
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

    //向b站的api获取某番剧的全部评价，并存在本地,因为怕访问太频繁被封ip，一次最多只获取3w
    @Transactional
    @Override
    public ServerResponse pullAllShortReviewFromBiliApi(Integer media_id) throws IOException {
        //如果该id的动漫已经拉取过全部评论了，则中断拉取并提醒只需更新就行了
        if (animationMapper.selectShortReviewPersistenceMarkByMedia_id(media_id) == Const.PERSISTENCE){
            return ServerResponse.createByErrorMessage("该评价区已经拉取过了，无序重复拉取");
        }
        JSONObject jsonObject;
        String apiUrl;
        String reviewJson;
        ShortReviewPageInfo reviewPageInfo;
        List<ShortReview> ShortReviewList;
        int i = 1;
        //如果缓存中的页面为空，赋初值为1
        Long cursor = (Long) redisTemplate.opsForValue().get("shortReviewCursor:"+media_id);
        if (cursor == null){
            cursor = 1L;
        }

        while (cursor != 0){
            //第一次访问的时候不需要页码
            if (cursor.equals(1L)){
                apiUrl = "https://api.bilibili.com/pgc/review/short/list?ps=30&sort=0&media_id="+media_id;
            } else {
                apiUrl = "https://api.bilibili.com/pgc/review/short/list?ps=30&sort=0&media_id="+media_id+"&cursor="+cursor;
            }
            reviewJson = catchApi.getJsonFromApi(apiUrl);
            //把json字符串转变成jsonObject
            jsonObject = JSON.parseObject(reviewJson);
            //获取分页信息
            reviewPageInfo = (ShortReviewPageInfo) jsonObject.getObject("data", ShortReviewPageInfo.class);
            System.out.println(reviewPageInfo);
            //从分页信息中取出数据
            ShortReviewList = reviewPageInfo.getList();
            //存进数据库
            if (ShortReviewList.size() > 0){
                ServerResponse serverResponse = insertShortReviews(ShortReviewList);
                if (serverResponse.getStatus() != 0){
                    return serverResponse;
                }
            }
            //设置下一页的页码
            cursor = reviewPageInfo.getNext();
            /*因为使用了事务管理，一次提交3W+评论，所以在获取3W+评论后才加入缓存
            //并加入缓存中
            redisTemplate.opsForValue().set("shortReviewCursor",cursor);*/
            i++;
            //获取数据超过3w时停止获取
            if (i > 1000) {
                //停止前把下一页页码存进redis中
                redisTemplate.opsForValue().set("shortReviewCursor:"+media_id,cursor);
                break;
            }
        }
        //如果下一页为0，则说明已经爬取完全部数据
        if (cursor == 0) {
            //删除缓存中的页码
            redisTemplate.delete("shortReviewCursor:"+media_id);
            //更改animation中的短评持久化状态
            animationMapper.updateShortReviewPersistenceMarkByMedia_id(Const.PERSISTENCE,media_id);
            System.out.println("id为"+media_id+"的动漫短评已经爬取完");
        }
        //拉取后，删除redis中关于评论数量的缓存
        redisTemplate.delete("review:quantity:"+media_id);
        //删除redis中关于该视频的查询缓存
        redisTemplate.delete((RedisConst.key(media_id)));
        return ServerResponse.createBySuccess();
    }

    //向b站的api获取某番剧的最新评价，并存在本地
    //事务管理增加插入性能，也可以保证更新的可靠性，以免出现更新到一半卡住，导致页面丢失的问题
    @Transactional
    @Override
    public ServerResponse pullNewShortReviewFromBiliApi(Integer media_id) throws IOException {
        //如果该id的动漫还没有持久化，则无法更新
        if (animationMapper.selectShortReviewPersistenceMarkByMedia_id(media_id) == Const.NO_PERSISTENCE){
            return ServerResponse.createByErrorMessage("请先持久化");
        }

        Long cursor = 1L;
        String apiUrl;
        String reviewJson;
        JSONObject jsonObject;
        ShortReviewPageInfo reviewPageInfo;
        List<ShortReview> reviewList;

        //把该番剧最新的短评的id取出来
        ShortReview latestReview =shortReviewMapper.selectLatestReviewByMedia_id(media_id);
        System.out.println("最新的短评为:"+latestReview);
        while(cursor != 0){
            if (cursor == 1L){
                apiUrl = "https://api.bilibili.com/pgc/review/short/list?ps=20&sort=1&media_id="+media_id;
            } else {
                apiUrl = "https://api.bilibili.com/pgc/review/short/list?ps=20&sort=1&media_id="+media_id+"&cursor="+cursor;
            }

            reviewJson = catchApi.getJsonFromApi(apiUrl);
            System.out.println(reviewJson);
            jsonObject = JSON.parseObject(reviewJson);
            reviewPageInfo = jsonObject.getObject("data",ShortReviewPageInfo.class);
            reviewList = reviewPageInfo.getList();

            //如果获取到的短评长度大于0
            if (reviewList.size() > 0){
                for (ShortReview shortReview:reviewList){
                    //如果评论创建的时间大于数据库中评论的最新时间，则插入该评论
                    if (shortReview.getCtime() > latestReview.getMtime()){
                        System.out.println("要插入的短评为:"+shortReview);
                        insertShortReview(shortReview);
                    } else {
                        //如果创建时间比已持久化的最新评论小，但是最终修改时间比它大，执行短评的修改
                        if (shortReview.getMtime() > latestReview.getMtime()){
                            //更新短评
                            System.out.println("要更新的短评为:"+shortReview);
                            shortReviewMapper.updateContentByReview_id(shortReview);
                            //更新短评的点赞数
                            Stat stat = shortReview.getStat();
                            stat.setReview_id(shortReview.getReview_id());
                            statMapper.updateStatByReview_id(stat);
                        } else {
                            //拉取后，删除redis中关于评论数量的缓存
                            redisTemplate.delete("review:quantity:"+media_id);
                            //删除redis中关于该视频的查询缓存
                            redisTemplate.delete((RedisConst.key(media_id)));
                            return ServerResponse.createBySuccess();
                        }
                    }

                }
            }
            cursor = reviewPageInfo.getNext();
        }
        return ServerResponse.createByErrorMessage("无需更新");
    }

    //把数据存进数据库，多条
    @Transactional
    @Override
    public ServerResponse insertShortReviews(List<ShortReview> shortReviewList) {
        if (shortReviewList.size() == 0){
            return ServerResponse.createByError();
        }

        List<Stat> statList = new ArrayList<>();
        List<BiliUser> userList = new ArrayList<>();

        //先插入review表的数据
        shortReviewMapper.insertShortReviewList(shortReviewList);
        for (ShortReview shortReview:shortReviewList){
            /* 这里使用try来执行插入用户信息语句，这样因为主键mid重复报错就不会导致程序中断执行，不重复就会直接插入成功
             * 而且还不用再用select语句在几十w的数据中查询用户是否存在，节省了非常多的时间
             * NEW: 使用ignore关键字可以直接使用sql处理主键重复问题，不需要在代码中处理，从而减少请求数量。
             * *//*
            try {
                userMapper.insertUser(shortReview.getAuthor());
            } catch (Exception e){

            }*/
            //把user存成一个集合 一起插入
            BiliUser user = shortReview.getAuthor();
            userList.add(user);
            //把stat存成一个集合 一起插入
            Stat stat = shortReview.getStat();
            stat.setReview_id(shortReview.getReview_id());
            statList.add(stat);
        }
        //插入stat表
        statMapper.insertShortReviewStatList(statList);
        //插入user表
        biliUserMapper.insertUserList(userList);
        return ServerResponse.createBySuccess();
    }

    //把数据存进数据库,一条
    @Transactional
    @Override
    public ServerResponse insertShortReview(ShortReview shortReview) {
        if (shortReview == null){
            return ServerResponse.createByError();
        }
        int status = shortReviewMapper.insertShortReview(shortReview);
        if (status > 0){
            /* 这里使用try来执行插入用户信息语句，这样因为主键mid重复报错就不会导致程序中断执行，不重复就会直接插入成功
             * 而且还不用再用select语句在几十w的数据中查询用户是否存在，节省了非常多的时间
             * */
            try {
                biliUserMapper.insertUser(shortReview.getAuthor());
            } catch (Exception e){

            }
            //执行插入stat
            Stat stat = shortReview.getStat();
            stat.setReview_id(shortReview.getReview_id());
            status = statMapper.insertShortReviewStat(stat);
            if (!(status > 0)){
                //插入stat失败，删除已经存进数据库的长评，并返回错误
                shortReviewMapper.deleteShortReviewByReview_id(shortReview.getReview_id());
                ServerResponse.createByError();
            }
        } else {
            return ServerResponse.createByError();
        }

        return ServerResponse.createBySuccess();
    }

    //通过review_id删除短评
    @Override
    public ServerResponse deleteShortReviewByReview_id(Integer review_id) {
        if (review_id == null){
            return ServerResponse.createByError();
        }
        //删除前先把短评内容查出来,方便等下失败时还原使用
        ShortReview originalShortReview = shortReviewMapper.selectShortReviewByReview_id(review_id);
        //删除短评
        int status = shortReviewMapper.deleteShortReviewByReview_id(review_id);
        if (status > 0){
            //删除长评中的stat
            status = statMapper.deleteStatByReview_id(originalShortReview.getReview_id());
            if (status > 0 ){
                return ServerResponse.createBySuccess();
            }
        }
        return ServerResponse.createByError();
    }

    @Override
    public ServerResponse listAll() {
        return null;
    }

    /*查询功能*/
    @Override
    public ServerResponse list(Integer media_id, Integer score, Integer sort, Integer pageNum, Integer pageSize) {
        HashOperations hashRedis = redisTemplate.opsForHash();
        String key = RedisConst.key(media_id);
        String hashKey = RedisConst.hashKey(score, sort, pageNum, pageSize);
        //直接从缓存中取
        if (hashRedis.get(key, hashKey) != null){
            return  ServerResponse.createBySuccess(hashRedis.get(key, hashKey));
        }

        PageHelper.startPage(pageNum, pageSize);
        String sortType = sortUntil.convertToReviewSortType(sort);
        List<ShortReview> shortReviewList = shortReviewMapper.selectReviewByMedia_id(media_id, score, sortType);
        PageInfo<ShortReview> pageInfo = new PageInfo<>(shortReviewList);
        //加入到缓存中，过期时间为4小时
        hashRedis.put(key, hashKey, pageInfo);
        redisTemplate.expire(key, 4*60*60, TimeUnit.SECONDS);
        return ServerResponse.createBySuccess(pageInfo);
    }

    //通过keyword搜索评论
    @Override
    public ServerResponse searchReviewByKeyword(Integer media_id, String keyword, Integer score, Integer sort, Integer pageNum, Integer pageSize) {
        String key = RedisConst.key(media_id, keyword);
        String hashKey = RedisConst.hashKey(score, sort, pageNum, pageSize);
        if (redisTemplate.opsForHash().get(key, hashKey) != null){
            return ServerResponse.createBySuccess(redisTemplate.opsForHash().get(key, hashKey));
        }

        PageHelper.startPage(pageNum, pageSize);
        String sortType = sortUntil.convertToReviewSortType(sort);
        List<ShortReview> shortReviewList = shortReviewMapper.selectReviewByKeyWord(media_id, keyword, score, sortType);
        PageInfo<ShortReview> pageInfo = new PageInfo<>(shortReviewList);
        redisTemplate.opsForHash().put(key, hashKey, pageInfo);
        redisTemplate.expire(key, 2*60, TimeUnit.SECONDS);
        return ServerResponse.createBySuccess(pageInfo);
    }

    //查找某用户的评论
    @Override
    public ServerResponse searchReviewByMid(Integer mid, Integer sort, Integer pageNum, Integer pageSize) {
        String key = RedisConst.MKey(mid);
        if (redisTemplate.opsForValue().get(key) != null && sort == Const.SortReview.DEFAULT && pageNum == 1){
            return ServerResponse.createBySuccess(redisTemplate.opsForValue().get(key));
        }

        PageHelper.startPage(pageNum, pageSize);
        String sortType = sortUntil.convertToReviewSortType(sort);
        List<ShortReview> shortReviewList = shortReviewMapper.selectReviewByMid(mid, sortType);
        PageInfo<ShortReview> pageInfo = new PageInfo<>(shortReviewList);
        if (sort == Const.SortReview.DEFAULT && pageNum == 1){
            redisTemplate.opsForValue().set(key, pageInfo);//只缓存第一页和默认排序方式
            redisTemplate.expire(key, 2*60, TimeUnit.SECONDS);
        }
        return ServerResponse.createBySuccess(pageInfo);
    }

}
