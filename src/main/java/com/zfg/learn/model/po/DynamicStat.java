package com.zfg.learn.model.po;

import io.swagger.annotations.ApiModelProperty;

/**
 * 动态项 如视频内容  转发的原动态
 */
public class DynamicStat {

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("简介")
    private String description;

    @ApiModelProperty("图片")
    private String img;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
