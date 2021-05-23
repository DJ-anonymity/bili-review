package com.zfg.learn.common;

public class Const {
    //持久化状态
    public static final int PERSISTENCE = 1;
    public static final int NO_PERSISTENCE = 0;

    //工作或空闲状态
    public static final int IS_RUNNING = 1;
    public static final int IS_FREE = 0;

    //用户cookie
    public static final String COOKIE = "cookie";

    //用户seta
    public static final String SESSDATA = "SESSDATA=";

    //当前用户
    public static final String CURRENT_USER = "current_user";

    public interface SortReview{
        int DEFAULT = 0;
        int MTIME_ASC = 1;
        int MTIME_DESC = 2;
        int LIKES_ASC = 3;
    }

    public interface Sub{
        int TYPE_MEDIA= 0;
        int TYPE_UP = 1;

        int CANCEL = 0;
        int FOLLOW = 1;
    }

    /**
     * url常量
     */
    public interface Url{
        String USER_INFO = "https://api.bilibili.com/x/web-interface/nav";
        String USER_UNREAD = "https://api.bilibili.com/x/msgfeed/unread?mobi_app=web"; //未读消息
        String REPLY = "https://member.bilibili.com/x/web/replies?order=ctime&filter=-1&is_hidden=0&type=1";//回复
    }

    public interface Path{
        //String CHROME_EXTENSION = "C:\\Users\\zhong\\Desktop\\毕业设计\\毕业设计\\钟房桂\\谷歌插件\\chrome插件.zip";
        String CHROME_EXTENSION = "/usr/local/springboot/bili/插件/chrome插件.zip";
    }

    public interface Dynamic{
        int FORWARD = 1; //转发
        int NORMAL = 2; //自己发表的动态
        int NORMAL_NO_IMG = 4; //自己发表的没有照片的动态
        int VIDEO_UP = 8; //视频投稿
        int MEDIA = 512; //影剧动漫
        int LIVE = 4308; //直播
        int ARTICLE = 64; //专栏
        int TV_SHOW =  4099; //综艺
    }
}
