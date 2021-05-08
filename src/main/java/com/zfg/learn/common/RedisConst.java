package com.zfg.learn.common;

public class RedisConst {
    public static final String SHORT_REVIEW = "shortReview:";
    public static final String MEDIA_ID = "media_id:";
    public static final String KEYWORD = "keyword:";
    public static final String SCORE = "score:";
    public static final String SORT = "sort:";
    public static final String PAGE_NUM = "pageNum:";
    public static final String PAGE_SIZE = "pageSize:";
    public static final String MID = "mid:";
    public static final String COOKIE = "cookie:";
    public static final String UNREAD_COMMENT = "unread_comment:mid:";

    public static String key(Integer media_id){
        String key = SHORT_REVIEW;
        key += MEDIA_ID+media_id;
        return key;
    }

    /*kw搜索*/
    public static String key(Integer media_id, String keyword){
        String key = SHORT_REVIEW;
        key += MEDIA_ID+media_id;
        if (keyword != null && keyword != ""){
            key += KEYWORD+keyword;
        }
        return key;
    }


    /*通过用户id搜索的key*/
    public static String MKey(Integer mid){
        String key = SHORT_REVIEW;
        key += MID+mid;
        return key;
    }

    public static String hashKey(Integer score, Integer sort, Integer pageNum, Integer pageSize){
        String hashKey = "";
        if (score != null){
            hashKey += SCORE+score+":";
        }
        hashKey += SORT+sort+":"+
                PAGE_NUM+pageNum+":" +
                PAGE_SIZE+pageSize;
        return hashKey;
    }


    public static String hashKey(Integer sort, Integer pageNum, Integer pageSize){
        String hashKey = "";
        hashKey += SORT+sort+":"+
                PAGE_NUM+pageNum+":" +
                PAGE_SIZE+pageSize;
        return hashKey;
    }
}
