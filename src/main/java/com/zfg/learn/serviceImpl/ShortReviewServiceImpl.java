package com.zfg.learn.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zfg.learn.common.Const;
import com.zfg.learn.common.ServerResponse;
import com.zfg.learn.dao.AnimationMapper;
import com.zfg.learn.dao.ShortReviewMapper;
import com.zfg.learn.dao.StatMapper;
import com.zfg.learn.dao.UserMapper;
import com.zfg.learn.pojo.ShortReview;
import com.zfg.learn.pojo.ReviewPageInfo;
import com.zfg.learn.pojo.ShortReviewPageInfo;
import com.zfg.learn.pojo.Stat;
import com.zfg.learn.service.ShortReviewService;
import com.zfg.learn.until.CatchApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ShortReviewServiceImpl implements ShortReviewService {
    @Autowired
    private ShortReviewMapper shortReviewMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StatMapper statMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private AnimationMapper animationMapper;

    private CatchApi catchApi = new CatchApi();

    //向b站的api获取某番剧的全部评价，并存在本地,因为怕访问太频繁被封ip，一次最多只获取3w
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
        Long cursor = (Long) redisTemplate.opsForValue().get("shortReviewCursor");
        if (cursor == null){
            cursor = 1L;
        }
        //标志当前功能正在使用，无法再被执行
        redisTemplate.opsForValue().set("pullShortReviewStatus",Const.IS_RUNNING);

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
            //设置下一页的页码
            cursor = reviewPageInfo.getNext();
            //并加入缓存中
            redisTemplate.opsForValue().set("shortReviewCursor",cursor);
            //从分页信息中取出数据
            ShortReviewList = reviewPageInfo.getList();
            //存进数据库
            if (ShortReviewList.size() > 0){
                ServerResponse serverResponse = insertShortReviews(ShortReviewList);
                if (serverResponse.getStatus() != 0){
                    return serverResponse;
                }
            }
            i++;
            //获取数据超过3w时停止获取，并把下一页页码存进缓存中
            if (i>1000) {
                break;
            }
        }
        //如果下一页为0，则说明已经爬取完全部数据
        if (cursor == 0) {
            //删除缓存中的页码
            redisTemplate.delete("shortReviewCursor");
            //更改animation中的短评持久化状态
            animationMapper.updateShortReviewPersistenceMarkByMedia_id(Const.PERSISTENCE,media_id);
        }
        //标记该功能进入空闲状态
        redisTemplate.opsForValue().set("pullShortReviewStatus",Const.IS_FREE);
        return ServerResponse.createBySuccess();
    }

    //向b站的api获取某番剧的最新评价，并存在本地
    @Override
    public ServerResponse pullNewShortReviewFromBiliApi(Integer media_id) throws IOException {
        //把该番剧最新的短评的id取出来
        ShortReview latestReview =shortReviewMapper.selectLatestReviewByMedia_id(media_id);
        System.out.println("最新的短评为:"+latestReview);
        Long cursor = 1L;
        String apiUrl;
        String reviewJson;
        JSONObject jsonObject;
        ShortReviewPageInfo reviewPageInfo;
        List<ShortReview> reviewList;
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
                            return ServerResponse.createBySuccess();
                        }
                    }

                }
            }
            cursor = reviewPageInfo.getNext();
        }
        return ServerResponse.createByError();
    }

    //把数据存进数据库，多条
    @Override
    public ServerResponse insertShortReviews(List<ShortReview> ShortReviewList) {
        if (ShortReviewList.size() == 0){
            return ServerResponse.createByError();
        }
        int status = 0;
        for (ShortReview shortReview:ShortReviewList){
            status =shortReviewMapper.insertShortReview(shortReview);
            if (status > 0){
              /* 这里使用try来执行插入用户信息语句，这样因为主键mid重复报错就不会导致程序中断执行，不重复就会直接插入成功
              * 而且还不用再用select语句在几十w的数据中查询用户是否存在，节省了非常多的时间
              * */
              try {
                  userMapper.insertUser(shortReview.getAuthor());
              } catch (Exception e){

              }
              //执行插入stat
              Stat stat = shortReview.getStat();
              stat.setReview_id(shortReview.getReview_id());
              status = statMapper.insertStatByReview_id(stat);
              if (!(status > 0)){
                  //插入stat失败，删除已经存进数据库的长评，并返回错误
                  shortReviewMapper.deleteShortReviewByReview_id(shortReview.getReview_id());
                  ServerResponse.createByError();
              }
            } else {
                return ServerResponse.createByError();
            }
        }
        return ServerResponse.createBySuccess();
    }

    //把数据存进数据库,一条
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
                userMapper.insertUser(shortReview.getAuthor());
            } catch (Exception e){

            }
            //执行插入stat
            Stat stat = shortReview.getStat();
            stat.setReview_id(shortReview.getReview_id());
            status = statMapper.insertStatByReview_id(stat);
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
        //删除长评
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
}
