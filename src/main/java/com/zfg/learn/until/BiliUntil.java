package com.zfg.learn.until;

/**
 * bili until
 * @author bootzhong
 */
public class BiliUntil {
    private final String SESSDATA;
    private final String bili_jct;
    private final String buvid3;
    CatchApi catchApi = new CatchApi();

    public BiliUntil(String SESSDATA, String bili_jct, String buvid3){
        this.SESSDATA = SESSDATA;
        this.bili_jct = bili_jct;
        this.buvid3 = buvid3;
    }

    /**
     * 订阅博主
     */
    public void followUp(Long mid){

    }

    /**
     * 取订阅博主
     */
    public void unfollowUp(){

    }

    /**
     * 订阅番剧
     */
    public void followMd(Long mid){

    }

    /**
     * 取订阅番剧
     */
    public void unFollowMd(Long mid){

    }

    /**
     * API
     */
    public interface Api{
        String RELATION_MODIFY = "https://api.bilibili.com/x/relation/modify";
        String FOLLOW_MEDIA = "https://api.bilibili.com/pgc/web/follow/add";
        String UNFOLLOW_MEDIA = "https://api.bilibili.com/pgc/web/follow/del";
    }
}
