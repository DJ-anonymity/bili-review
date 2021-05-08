package com.zfg.learn.model.bili;

/**
 * 接受B站url https://api.bilibili.com/x/web-interface/nav 的数据
 */
public class UserInfoBili {

    private Boolean isLogin;

    private String face;

    private Integer mid;

    public Boolean getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(Boolean isLogin) {
        this.isLogin = isLogin;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }
}
