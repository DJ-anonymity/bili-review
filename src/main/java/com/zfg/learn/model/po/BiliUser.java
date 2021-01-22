package com.zfg.learn.model.po;
/*
* 评论的作者
* */
public class BiliUser {
    //作者的头像
    private String avatar;
    private Integer mid;
    private String uname;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    @Override
    public String toString() {
        return "Author{" +
                "avatar='" + avatar + '\'' +
                ", mid=" + mid +
                ", uname='" + uname + '\'' +
                '}';
    }
}
