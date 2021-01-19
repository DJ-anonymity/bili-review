package com.zfg.learn.common;

public class Const {
    //持久化状态
    public static final int PERSISTENCE = 1;
    public static final int NO_PERSISTENCE = 0;

    //工作或空闲状态
    public static final int IS_RUNNING = 1;
    public static final int IS_FREE = 0;

    //用户cookie
    public static final String COOKIE = "cookie:";

    //当前用户
    public static final String CURRENT_USER = "current_user";


    public interface SortReview{
        int DEFAULT = 0;
        int MTIME_ASC = 1;
        int MTIME_DESC = 2;
        int LIKES_ASC = 3;
    }

}
