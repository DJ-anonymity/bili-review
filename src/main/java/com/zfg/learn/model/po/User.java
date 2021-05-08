package com.zfg.learn.model.po;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * 用户实体类
 * @author bootzhong
 */
public class User {
    @ApiModelProperty(notes = "主键")
    private Integer uid;

    @NotBlank(groups = Register.class)
    @ApiModelProperty(notes = "用户名")
    private String username;

    @ApiModelProperty(notes = "账号")
    private String account;

    @NotBlank(groups = Register.class)
    @ApiModelProperty(notes = "密码")
    private String password;

    @Email(groups = Register.class)
    @ApiModelProperty(notes = "邮箱")
    private String email;

    @ApiModelProperty(notes = "qq")
    private Long qq;

    @ApiModelProperty(notes = "b站账号id")
    private Integer mid;

    @ApiModelProperty(notes = "b站登陆cookie")
    private String cookie;

    @ApiModelProperty(notes = "权限 1：未绑定b站 2：普通用户 666：管理员")
    private Integer role;

    public interface Register{

    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getQq() {
        return qq;
    }

    public void setQq(Long qq) {
        this.qq = qq;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
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
