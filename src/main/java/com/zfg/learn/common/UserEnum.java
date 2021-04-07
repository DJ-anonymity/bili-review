package com.zfg.learn.common;

import io.swagger.models.auth.In;

public enum  UserEnum {
    BOT(2, "18477411304@163.com", "bot123456");

    private Integer uid;
    private String email;
    private String password;

    UserEnum(Integer uid, String email, String password) {
        this.uid = uid;
        this.email = email;
        this.password = password;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
