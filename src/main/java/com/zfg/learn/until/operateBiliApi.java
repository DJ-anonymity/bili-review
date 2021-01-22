package com.zfg.learn.until;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zfg.learn.model.po.LongReview;
import com.zfg.learn.model.po.ReviewPageInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.util.List;

public class operateBiliApi {
    @Autowired
    private RedisTemplate redisTemplate;
    private CatchApi catchApi = new CatchApi();
    @Test
    public void demo2() throws IOException {

        int i =1;
        //把全部评论的集合,从缓存中取出来
        List<LongReview> shortLongReviewList = (List<LongReview>) redisTemplate.opsForValue().get("shortCommentaryList");
        //下一页
        Long cursor = (Long) redisTemplate.opsForValue().get("shortCommentCursor");
        //如果缓存中的cursor为空，则赋一个初值
        if (cursor == null){
            cursor = 1L;
        }
        //要抓取的api
        String url = "";
        //分页信息
        ReviewPageInfo reviewPageInfo;
        //api获取到的数据
        String dataJson;
        JSONObject object;
        //如果下一页的页面为0，则证明已经把所有数据遍历完
        while (cursor!=0) {
            if (cursor == 1L){
                cursor = 0L;
            }
            //cursor为页码,ps为每页的大小，b站默认使用20条数据每页，经过检测最大每页30，sort为排序方式0为默认，1为最新时间
            url = "https://api.bilibili.com/pgc/review/short/list?media_id=28224080&ps=30&sort=0&cursor="+cursor;
            dataJson = catchApi.getJsonFromApi(url);
            //把json字符串转变成java的pojo类
            object = JSON.parseObject(dataJson);
            //获取分页信息
            reviewPageInfo = (ReviewPageInfo) object.getObject("data", ReviewPageInfo.class);
            //设置下一页的页码
            cursor = reviewPageInfo.getNext();
            System.out.println("第"+i+"页,下一页码是"+cursor);
            //如果不是空的就相加
            if (shortLongReviewList != null){
                shortLongReviewList.addAll(reviewPageInfo.getList());
            } else {
                shortLongReviewList = reviewPageInfo.getList();
            }

            i++;
            //为了避免访问过于频繁被封ip,一次之获取3w数据
            if (i>1000){
                break;
            }
        }
        System.out.println(shortLongReviewList.size());
        //把评论加入缓存
        redisTemplate.opsForValue().set("shortCommentaryList", shortLongReviewList);
        //把下一页的页码放进缓存
        redisTemplate.opsForValue().set("shortCommentCursor",cursor);
    }
}
