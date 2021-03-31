package com.zfg.learn.model.po;

import io.swagger.annotations.ApiModelProperty;

/**
 * 订阅内容实体类
 */
public class Subscription {
    @ApiModelProperty(notes = "主键")
    private Integer id;

    @ApiModelProperty(notes = "用户ID")
    private Integer uid;

    @ApiModelProperty(notes = "订阅的内容id")
    private Integer fid;

    @ApiModelProperty(notes = "订阅的内容类型 0：影剧；1：up ")
    private Integer type;

    @ApiModelProperty(notes = "订阅的级别 待开发")
    private Integer level;

    @ApiModelProperty(notes = "订阅的状态 待开发")
    private Integer status;

    @ApiModelProperty(notes = "更新时间")
    private Long mtime;

    @ApiModelProperty(notes = "创建时间")
    private Long ctime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Long getMtime() {
        return mtime;
    }

    public void setMtime(Long mtime) {
        this.mtime = mtime;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }
}
