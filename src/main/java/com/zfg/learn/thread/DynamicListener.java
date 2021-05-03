package com.zfg.learn.thread;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zfg.learn.common.Const;
import com.zfg.learn.config.BeanContext;
import com.zfg.learn.model.po.Dynamic;
import com.zfg.learn.model.po.DynamicStat;
import com.zfg.learn.until.CatchApi;
import com.zfg.learn.until.DynamicBlockingQueue;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.util.HashMap;

/**
 * type 1:动态转发 2：自己发表的动态 4：自己发表的无图片动态 8：视频投稿 动漫：512
 * 4308：直播 64：专栏
 * 暂时一次只推送20条
 * todo 优化代码优雅度
 */
public class DynamicListener extends Thread{
    private boolean isStop = false;
    private DynamicBlockingQueue dynamicQueue = DynamicBlockingQueue.getInstance();
    private static DynamicListener dynamicListener;
    private volatile String cookie; //保证内存可见性
    RedisTemplate redisTemplate = BeanContext.getBean("redisTemplate");
    private final String DYNAMIC_URL = "https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/dynamic_new?uid=20736117&type_list=268435455&from=weball&platform=web";
    CatchApi catchApi = new CatchApi();

    private DynamicListener(){

    }

    public static DynamicListener getInstance(){
        synchronized (DynamicListener.class){
            if (dynamicListener == null){
                dynamicListener = new DynamicListener();
            }
        }
        return dynamicListener;
    }

    public void setCookie(String cookie){
        this.cookie = cookie;
    }

    @Override
    public void run() {
        HashMap hashMap = new HashMap();
        String json;
        if (cookie == null){
            return;
        }
        while (true){
            try {
                //如果停止工作则进入睡眠状态 等待唤醒
                if (isStop){
                    synchronized (this){
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                //2s监听一次
                Thread.sleep(2000);

                Long cursor = (Long) redisTemplate.opsForValue().get("dynamic:cursor");
                hashMap.put(Const.COOKIE, cookie);
                //请求Api并获取数据
                json = catchApi.getJsonFromApiByHeader(DYNAMIC_URL, hashMap);
                JSONObject data = JSONObject.parseObject(json).getJSONObject("data");
                //如果已经是最新的了 则跳出本次循环
                if (data.getLong("max_dynamic_id").equals(cursor)){
                    //当获取的内容是最新的时候 停止刷新 5000
                    Thread.sleep(5000);
                    continue;
                } else {
                    //设置最新页码
                    redisTemplate.opsForValue().set("dynamic:cursor", data.getLong("max_dynamic_id"));
                }

                JSONArray jsonArray = data.getJSONArray("cards");
                for (int i = 0;i < jsonArray.size();i++){

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JSONObject desc = jsonObject.getJSONObject("desc");

                    //已经推送过了
                    Long dynamic_id = desc.getLong("dynamic_id");
                    if (dynamic_id.equals(cursor)){
                        break;
                    }

                    Dynamic dynamic = new Dynamic();
                    JSONObject card = jsonObject.getJSONObject("card");
                    JSONObject userInfo = desc.getJSONObject("user_profile").getJSONObject("info");
                    if (desc.getInteger("type") == Const.Dynamic.NORMAL){
                        dynamic.setId(dynamic_id);
                        dynamic.setAuthorId(userInfo.getLong("uid"));
                        dynamic.setAuthorName(userInfo.getString("uname"));
                        dynamic.setContent(card.getJSONObject("item").getString("description"));
                        dynamic.setUrl("https://t.bilibili.com/" + dynamic_id);
                        dynamic.setImg(card.getJSONObject("item").getJSONArray("pictures").getJSONObject(0).getString("img_src"));
                        dynamic.setType(Const.Dynamic.NORMAL);
                    } else if (desc.getInteger("type") == Const.Dynamic.NORMAL_NO_IMG){
                        dynamic.setId(dynamic_id);
                        dynamic.setAuthorId(userInfo.getLong("uid"));
                        dynamic.setAuthorName(userInfo.getString("uname"));
                        dynamic.setContent(card.getJSONObject("item").getString("content"));
                        dynamic.setUrl("https://t.bilibili.com/"+dynamic_id+"?tab=2");
                        dynamic.setType(Const.Dynamic.NORMAL_NO_IMG);
                    } else if (desc.getInteger("type") == Const.Dynamic.FORWARD){
                        dynamic.setId(dynamic_id);
                        dynamic.setAuthorId(userInfo.getLong("uid"));
                        dynamic.setAuthorName(userInfo.getString("uname"));
                        dynamic.setContent(card.getJSONObject("item").getString("content"));
                        dynamic.setUrl("https://t.bilibili.com/" + dynamic_id);
                        dynamic.setType(Const.Dynamic.FORWARD);

                        //8 即为转发的是视频
                        DynamicStat stat = new DynamicStat();
                        if (desc.getInteger("orig_type") == 8){
                            JSONObject origin = card.getJSONObject("origin");
                            stat.setTitle(origin.getString("title"));
                            stat.setImg(origin.getString("pic"));
                        } else {
                            JSONObject origin = card.getJSONObject("origin");
                            stat.setDescription(origin.getString("description"));
                            JSONObject item = origin.getJSONObject("item");
                            if (item.getJSONArray("pictures") != null && item.getJSONArray("pictures").size() > 0){
                                stat.setImg(item.getJSONArray("pictures").getJSONObject(0).getString("img_src"));
                            }
                        }
                        dynamic.setStat(stat);
                    } else if (desc.getInteger("type") == Const.Dynamic.VIDEO_UP){
                        dynamic.setId(dynamic_id);
                        dynamic.setAuthorId(userInfo.getLong("uid"));
                        dynamic.setAuthorName(userInfo.getString("uname"));
                        //设置专栏url
                        dynamic.setContent(card.getString("dynamic"));
                        dynamic.setUrl("https://www.bilibili.com/video/" + desc.getString("bvid"));
                        dynamic.setType(Const.Dynamic.VIDEO_UP);

                        DynamicStat stat = new DynamicStat();
                        stat.setTitle(card.getString("title"));
                        stat.setDescription(card.getString("desc"));
                        stat.setImg(card.getString("pic"));
                        dynamic.setStat(stat);
                    } else if (desc.getInteger("type") == Const.Dynamic.MEDIA) {
                        //todo 影剧没有media_id o(╥﹏╥)o
                    } else if (desc.getInteger("type") == Const.Dynamic.LIVE) {
                        JSONObject live_play_info = card.getJSONObject("live_play_info");

                        dynamic.setId(dynamic_id);
                        dynamic.setAuthorId(userInfo.getLong("uid"));
                        dynamic.setAuthorName(userInfo.getString("uname"));
                        dynamic.setUrl(live_play_info.getString("link"));
                        dynamic.setType(4308);

                        DynamicStat stat = new DynamicStat();
                        stat.setTitle(live_play_info.getString("title"));
                        //直播的description为直播标签
                        stat.setDescription(live_play_info.getString("area_name"));
                        dynamic.setStat(stat);
                    } else if (desc.getInteger("type") == Const.Dynamic.ARTICLE) {
                        dynamic.setId(dynamic_id);
                        dynamic.setAuthorId(userInfo.getLong("uid"));
                        dynamic.setAuthorName(userInfo.getString("uname"));
                        //设置专栏url
                        dynamic.setUrl("https://www.bilibili.com/read/cv" + card.getLong("id"));
                        dynamic.setType(Const.Dynamic.ARTICLE);

                        DynamicStat stat = new DynamicStat();
                        stat.setTitle(card.getString("title"));
                        stat.setDescription(card.getString("summary"));
                        stat.setImg((String) card.getJSONArray("image_urls").get(0));
                        dynamic.setStat(stat);
                    }


                    dynamicQueue.add(dynamic);
                }

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("出现问题");
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("线程问题");
            } catch (RuntimeException e){
                e.printStackTrace();
                System.out.println("注意处理该异常!!!!!");
            }
        }
    }

    /**
     * 继续运行
     */
    public void goOn(){
        synchronized (this){
            notify();
        }
        isStop = false;
    }

    /**
     * 停止运行
     */
    public void pause(){
        this.isStop = true;
    }

    public boolean isStop(){
        return this.isStop;
    }
}
