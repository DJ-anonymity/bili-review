package com.zfg.learn.thread;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zfg.learn.common.Const;
import com.zfg.learn.config.BeanContext;
import com.zfg.learn.model.bo.PublicTask;
import com.zfg.learn.until.CatchApi;
import com.zfg.learn.until.DynamicBlockingQueue;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.util.HashMap;

/**
 * type 1:动态转发 2：自己发表的动态 4：自己发表的无图片动态 8：视频投稿 动漫：512
 * 暂时一次只推送20条
 */
public class DynamicListener extends Thread{
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
        hashMap.put(Const.COOKIE, cookie);
        while (true){
            try {
                //2s监听一次
                Thread.sleep(2000);

                Long cursor = (Long) redisTemplate.opsForValue().get("dynamic:cursor");
                json = catchApi.getJsonFromApiByHeader(DYNAMIC_URL, hashMap);
                JSONObject data = JSONObject.parseObject(json).getJSONObject("data");
                //如果已经是最新的了 则跳出本次循环
                if (data.getLong("max_dynamic_id").equals(cursor)){
                    //当获取的内容是最新的时候 停止刷新 5000
                    Thread.sleep(5000);
                    continue;
                } else {
                    redisTemplate.opsForValue().set("dynamic:cursor", data.getLong("max_dynamic_id"));
                }

                JSONArray jsonArray = data.getJSONArray("cards");
                for (int i = 0;i < jsonArray.size();i++){
                    PublicTask task = new PublicTask();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    JSONObject desc = jsonObject.getJSONObject("desc");
                    //已经推送过了
                    Long dynamic_id = desc.getLong("dynamic_id");
                    if (dynamic_id.equals(cursor)){
                        break;
                    }

                    JSONObject card = jsonObject.getJSONObject("card");
                    if (desc.getInteger("type") == 2){
                        task.setPid(desc.getInteger("uid"));
                        task.setTitle(card.getJSONObject("item").getString("title"));
                        task.setUname(card.getJSONObject("user").getString("name"));
                        task.setDescription(card.getJSONObject("item").getString("description"));
                        task.setUrl("https://t.bilibili.com/"+dynamic_id+"?tab=2");
                    } else if (desc.getInteger("type") == 4){
                        task.setPid(desc.getInteger("uid"));
                        task.setTitle(card.getJSONObject("item").getString("content"));
                        task.setUname(card.getJSONObject("user").getString("uname"));
                        //task.setDescription(card.getJSONObject("item").getString("description"));
                        task.setDescription("");
                        task.setUrl("https://t.bilibili.com/"+dynamic_id+"?tab=2");
                    } else if (desc.getInteger("type") == 1){
                        task.setPid(desc.getInteger("uid"));
                        task.setTitle(card.getJSONObject("item").getString("content"));
                        task.setUname(card.getJSONObject("user").getString("uname"));
                        //task.setDescription(card.getJSONObject("item").getString("description"));
                        task.setDescription("");
                        task.setUrl("https://t.bilibili.com/"+dynamic_id+"?tab=2");
                    } else if (desc.getInteger("type") == 8){
                        task.setPid(desc.getInteger("uid"));
                        task.setTitle(card.getJSONObject("stat").getString("title"));
                        task.setUname(card.getJSONObject("owner").getString("name"));
                        //task.setDescription(card.getJSONObject("item").getString("description"));
                        task.setDescription("");
                        task.setUrl(card.getString("jump_url"));
                    } else if (desc.getInteger("type") == 512) {
                        //todo 影剧没有media_id o(╥﹏╥)o
                        task.setTitle(card.getJSONObject("apiSeasonInfo").getString("title"));
                        //task.setUname(card.getJSONObject("item").getString("name"));
                        task.setUname("");
                        task.setDescription(card.getString("new_desc"));
                        task.setUrl(card.getString("url"));
                    }

                    dynamicQueue.add(task);
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


}
