package com.zfg.learn.interesting;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zfg.learn.common.ServerResponse;
import com.zfg.learn.pojo.LongReview;
import com.zfg.learn.pojo.ReviewPageInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
/*
* 尝试调用b站api
*
* */


@SpringBootTest
public class CatchBili {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void catchBili2() throws IOException {
        System.out.println("ok");
        //接口地址
        ServerResponse serverResponse = null;
        String result = "";
        String apiUrl = "https://api.bilibili.com/x/space/bangumi/follow/list?type=1&follow_status=0&pn=1&ps=15&vmid=20736117&ts=1585405128288";
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // 设置连接主机服务器超时时间：15000毫秒
        connection.setConnectTimeout(15000);
        // 设置读取主机服务器返回数据超时时间：60000毫秒
        connection.setReadTimeout(60000);

        connection.setRequestMethod("GET");
        /*connection.setRequestProperty("Content-Type", "application/json");*/
        // 通过连接对象获取一个输入流，向远程读取
        if (connection.getResponseCode() == 200) {
            System.out.println("link ok");
            InputStream is = connection.getInputStream();
            // 对输入流对象进行包装:charset根据工作项目组的要求来设置
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            StringBuffer sbf = new StringBuffer();
            String temp = null;
            // 循环遍历一行一行读取数据
            while ((temp = br.readLine()) != null) {
                System.out.println("1");
                sbf.append(temp);
                sbf.append("\r\n");
            }
            result = sbf.toString();
            /*serverResponse = JSON.parseObject(result,ServerResponse.class);

            System.out.println(result);
            System.out.println(serverResponse.getData());*/
        }
    }

    //获取输入的api的数据
    @Test
    public String catchBiliCommentary(String originalUrl) throws IOException {
        //接口地址
        ServerResponse serverResponse = null;
        String result = "";
        URL url = new URL(originalUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // 设置连接主机服务器超时时间：15000毫秒
        connection.setConnectTimeout(15000);
        // 设置读取主机服务器返回数据超时时间：60000毫秒
        connection.setReadTimeout(60000);

        connection.setRequestMethod("GET");
        /*connection.setRequestProperty("Content-Type", "application/json");*/
        // 通过连接对象获取一个输入流，向远程读取
        if (connection.getResponseCode() == 200) {
            System.out.println("link ok");
            InputStream is = connection.getInputStream();
            // 对输入流对象进行包装:charset根据工作项目组的要求来设置
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            StringBuffer sbf = new StringBuffer();
            String temp = null;
            // 循环遍历一行一行读取数据
            while ((temp = br.readLine()) != null) {
                sbf.append(temp);
                sbf.append("\r\n");
            }
            result = sbf.toString();
            /*serverResponse = JSON.parseObject(result,ServerResponse.class);

            System.out.println(result);
            System.out.println(serverResponse.getData());*/
        }
        return result;
    }

    @Test
    public void demo() throws IOException {

        int i =1;
        //把全部评论的集合,从缓存中取出来
        List<LongReview> longLongReviewList = (List<LongReview>) redisTemplate.opsForValue().get("longCommentaryList");
        //下一页
        Long cursor = 1L;
        //要抓取的api
        String url = "";
        //分页信息
        ReviewPageInfo reviewPageInfo;
        //api获取到的数据
        String dataJson;
        JSONObject object;
        //如果下一页的页面为0，则证明已经把所有数据遍历完
        while (cursor!=0) {
            //cursor为页码,ps为每页的大小，b站默认使用20条数据每页，经过检测最大每页30，sort为排序方式0为默认，1为最新时间
            url = "https://api.bilibili.com/pgc/review/long/list?media_id=28224080&ps=30&sort=1&cursor="+cursor;
            dataJson = catchBiliCommentary(url);
            //把json字符串转变成java的pojo类
            object = JSON.parseObject(dataJson);
            //获取分页信息
            reviewPageInfo = (ReviewPageInfo) object.getObject("data", ReviewPageInfo.class);
            //设置下一页的页码
            cursor = reviewPageInfo.getNext();
            System.out.println("第"+i+"页,下一页码是"+cursor);
            longLongReviewList.addAll(reviewPageInfo.getList());
            i++;
            if (i>59){
                break;
            }
        }

        System.out.println(longLongReviewList.size());
        System.out.println(longLongReviewList);
        //把评论加入缓存
        redisTemplate.opsForValue().set("longCommentaryList", longLongReviewList);
    }

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
            dataJson = catchBiliCommentary(url);
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
