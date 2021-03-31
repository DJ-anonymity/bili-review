package com.zfg.learn.model.bo;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 推送任务
 */
public class PublicTask<T> {
    @ApiModelProperty("内容发表者Id")
    private Integer pid;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("用户名")
    private String uname;

    @ApiModelProperty("简介")
    private String description;

    @ApiModelProperty("链接")
    private String url;

    @ApiModelProperty("接收方集合")
    private List<T> recList;

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<T> getRecList() {
        return recList;
    }

    public void setRecList(List<T> recList) {
        this.recList = recList;
    }
}
