package com.zfg.learn.service;

import com.zfg.learn.common.ServerResponse;
import com.zfg.learn.model.bili.UserInfoBili;
import com.zfg.learn.model.bo.UserReviewBo;
import com.zfg.learn.model.para.UserPara;
import com.zfg.learn.model.po.User;

import javax.mail.MessagingException;
import javax.xml.ws.Service;
import java.io.IOException;

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
     * 通过cookie获取用户的b站信息
     * @return  Boolean
     */
    public UserInfoBili getBiliAcountByCookie(String loginCookie) throws IOException;

    /**
     * 获取当前浏览器的B站登陆Cookie
     * @return
     */
    public User getBiliCookie();

    /**
     * 注册
     * @return
     */
    public boolean register(UserPara user);

    /**
     * 登录
     * @return
     */
    public User login(User user);

    /**
     * 绑定b站账号
     * @return
     */
    public boolean bindBiliCount(User user);

    /**
     *  验证账号是否重复
     */
    public boolean checkName(String name);

    /**
     *  发送验证码
     * @param email
     */
    public void sendCheckNum(String email) throws MessagingException;

    Boolean checkEmail(String email);
}
