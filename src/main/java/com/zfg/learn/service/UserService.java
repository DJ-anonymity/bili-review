package com.zfg.learn.service;

import com.zfg.learn.bo.UserReviewBo;
import com.zfg.learn.pojo.User;

/**
 * 用户业务层, 主要处理user和biliUser
 * @Author bootzhong
 */
public interface UserService {

    /**
     * 获取用户发表的评论数量
     * @param mid
     * @return UserReviewBo
     */
    public UserReviewBo getReviewQuantity(Integer mid);

    /**
     * 验证当前用户的cookie是否存在，若存在是否可用
     * @return  Boolean
     */
    public Boolean checkBiliCookie(String loginCookie);


    /**
     * 获取当前浏览器的B站登陆Cookie
     * @return
     */
    public User getBiliCookie();
}
