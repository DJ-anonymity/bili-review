package com.zfg.learn.model.dto;

import com.zfg.learn.model.po.Subscription;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 订阅内容 dto
 */
public class SubscriptionDto extends Subscription {
    @ApiModelProperty("内容名")
    private String name;

    @ApiModelProperty("封面")
    private String cover;

    @ApiModelProperty("简介")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
