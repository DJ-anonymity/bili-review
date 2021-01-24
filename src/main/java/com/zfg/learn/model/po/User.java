package com.zfg.learn.model.po;

import io.swagger.annotations.ApiModelProperty;

/**
 * 用户实体类
 * @author bootzhong
 */
public class User {

    @ApiModelProperty(notes = "主键")
    private Integer uid;

    @ApiModelProperty(notes = "用户名")
    private String username;

    @ApiModelProperty(notes = "账号")
    private String account;

    @ApiModelProperty(notes = "密码")
    private String password;

    @ApiModelProperty(notes = "b站账号id")
    private String mid;

    @ApiModelProperty(notes = "b站登陆cookie")
    private String cookie;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", username='" + username + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", mid='" + mid + '\'' +
                ", cookie='" + cookie + '\'' +
                '}';
    }
}
