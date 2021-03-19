package com.zfg.learn.model.query;

import io.swagger.annotations.ApiModelProperty;

/**
 * 订阅内容 数据库查询条件类  用于构建多条件查询
 */
public class SubscriptionQuery {

    @ApiModelProperty(notes = "用户ID")
    private Integer uid;

    @ApiModelProperty(notes = "订阅的内容id")
    private String fid;

    @ApiModelProperty(notes = "订阅的内容类型 0：影剧；1：up ")
    private Integer type;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}
