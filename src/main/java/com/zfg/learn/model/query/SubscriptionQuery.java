package com.zfg.learn.model.query;

import io.swagger.annotations.ApiModelProperty;

/**
 * 订阅内容 数据库查询条件类  用于构建多条件查询
 */
public class SubscriptionQuery {

    @ApiModelProperty(notes = "用户ID")
    private Integer uid;

    @ApiModelProperty(notes = "订阅的内容id")
    private Integer fid;

    @ApiModelProperty(notes = "订阅的内容类型 0：影剧；1：up ")
    private Integer type;

    @ApiModelProperty(notes = "订阅的级别 -待开发")
    private Integer level;

    @ApiModelProperty(notes = "订阅的状态 -待开发")
    private Integer status;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
