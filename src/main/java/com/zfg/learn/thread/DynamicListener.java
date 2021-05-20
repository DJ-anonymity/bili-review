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
import org.springframework.data.redis.core.ValueOperations;

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

                //请求Api并获取数据
                hashMap.put(Const.COOKIE, cookie);
                json = catchApi.getJsonFromApiByHeader(DYNAMIC_URL, hashMap);
                //获取最新页码
                Long cursor = (Long) redisTemplate.opsForValue().get("dynamic:cursor");
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

                //获取最新动态的时间
                Integer latestTime = (Integer) redisTemplate.opsForValue().get("dynamic:latest:time");
                if (latestTime == null){
                    latestTime = 0;
                }
                JSONArray jsonArray = data.getJSONArray("cards");
                //for循环处理动态数据
                for (int i = 0;i < jsonArray.size();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JSONObject desc = jsonObject.getJSONObject("desc");

                    //通过最新标签id判断是否推送过了  tip:增加时间辅助判断 防止出现用户删除动态导致最新id丢失问题
                    Integer timestamp = desc.getInteger("timestamp");
                    Long dynamic_id = desc.getLong("dynamic_id");
                    //todo nullpointException????
                    if (dynamic_id.equals(cursor) || timestamp < latestTime){
                        break;
                    }

                    //解析dynamic
                    Dynamic dynamic = analysis(jsonObject);
                    dynamicQueue.add(dynamic);
                }

                //每次把最新页码的时间戳存到redis里面
                Integer newTime = jsonArray.getJSONObject(0).getJSONObject("desc").getInteger("timestamp");
                redisTemplate.opsForValue().set("dynamic:latest:time", newTime);
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
     * 解析动态
     * @param jsonObject
     * @return
     */
    private Dynamic analysis(JSONObject jsonObject){
        Dynamic dynamic = new Dynamic();
        JSONObject card = jsonObject.getJSONObject("card");
        JSONObject desc = jsonObject.getJSONObject("desc");
        Long dynamic_id = desc.getLong("dynamic_id");
        Integer type = desc.getInteger("type");

        // 开始解析
        dynamic.setId(dynamic_id);
        dynamic.setType(type);
        //番剧类型单独处理
        if (type == Const.Dynamic.MEDIA){
            JSONObject apiSeasonInfo = card.getJSONObject("apiSeasonInfo");
            //todo 把sessionId转换成mediaId
            dynamic.setAuthorId(apiSeasonInfo.getLong("season_id"));
            dynamic.setAuthorName(apiSeasonInfo.getString("title"));
            dynamic.setUrl(card.getString("url"));

            DynamicStat stat = new DynamicStat();
            stat.setTitle(card.getString("new_desc"));
            stat.setImg(card.getString("cover"));
            dynamic.setStat(stat);
        } else {
            JSONObject userInfo = desc.getJSONObject("user_profile").getJSONObject("info");
            dynamic.setAuthorId(userInfo.getLong("uid"));
            dynamic.setAuthorName(userInfo.getString("uname"));
            if (type == Const.Dynamic.NORMAL){
                dynamic.setContent(card.getJSONObject("item").getString("description"));
                dynamic.setUrl("https://t.bilibili.com/" + dynamic_id);
                dynamic.setImg(card.getJSONObject("item").getJSONArray("pictures").getJSONObject(0).getString("img_src"));
            } else if (type == Const.Dynamic.NORMAL_NO_IMG){
                dynamic.setContent(card.getJSONObject("item").getString("content"));
                dynamic.setUrl("https://t.bilibili.com/"+dynamic_id+"?tab=2");
            } else if (type == Const.Dynamic.FORWARD){
                dynamic.setContent(card.getJSONObject("item").getString("content"));
                dynamic.setUrl("https://t.bilibili.com/" + dynamic_id);

                //8 即为转发的是视频
                DynamicStat stat = new DynamicStat();
                JSONObject origin = card.getJSONObject("origin");
                if (desc.getInteger("orig_type") == 8){
                    stat.setTitle(origin.getString("title"));
                    stat.setImg(origin.getString("pic"));
                } else {
                    stat.setDescription(origin.getString("description"));
                    JSONObject item = origin.getJSONObject("item");
                    if (item.getJSONArray("pictures") != null && item.getJSONArray("pictures").size() > 0){
                        stat.setImg(item.getJSONArray("pictures").getJSONObject(0).getString("img_src"));
                    }
                }
                dynamic.setStat(stat);
            } else if (type == Const.Dynamic.VIDEO_UP){
                //设置专栏url
                dynamic.setContent(card.getString("dynamic"));
                dynamic.setUrl("https://www.bilibili.com/video/" + desc.getString("bvid"));

                DynamicStat stat = new DynamicStat();
                stat.setTitle(card.getString("title"));
                stat.setDescription(card.getString("desc"));
                stat.setImg(card.getString("pic"));
                dynamic.setStat(stat);
            } else if (type == Const.Dynamic.LIVE){
                JSONObject live_play_info = card.getJSONObject("live_play_info");
                dynamic.setUrl(live_play_info.getString("link"));
                dynamic.setType(4308);

                DynamicStat stat = new DynamicStat();
                stat.setTitle(live_play_info.getString("title"));
                //直播的description为直播标签
                stat.setDescription(live_play_info.getString("area_name"));
                dynamic.setStat(stat);
            } else if (type == Const.Dynamic.ARTICLE){
                //设置专栏url
                DynamicStat stat = new DynamicStat();
                stat.setTitle(card.getString("title"));
                stat.setDescription(card.getString("summary"));
                stat.setImg((String) card.getJSONArray("image_urls").get(0));
                dynamic.setStat(stat);
            }
        }

        return dynamic;
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
