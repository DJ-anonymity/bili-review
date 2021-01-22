package com.zfg.learn.model.dto;

import com.zfg.learn.model.po.User;

/**
 * 用户dto
 * @author bootzhong
 */
public class UserDto extends User {
    private Boolean isLogin;

    public Boolean getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(Boolean login) {
        isLogin = login;
    }
}
