package com.zfg.learn.model.dto;

import com.zfg.learn.model.po.Animation;
import io.swagger.annotations.ApiModelProperty;

public class AnimationDto  extends Animation {
    @ApiModelProperty("是否已经追番")
    private Boolean is_follow;

    public Boolean getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(Boolean is_follow) {
        this.is_follow = is_follow;
    }
}
