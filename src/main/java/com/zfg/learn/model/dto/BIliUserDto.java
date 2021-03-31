package com.zfg.learn.model.dto;

import com.zfg.learn.model.po.BiliUser;
import io.swagger.annotations.ApiModelProperty;

public class BIliUserDto extends BiliUser {
    @ApiModelProperty(notes = "是否已经关注")
    private Boolean is_follow;

    public Boolean getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(Boolean is_follow) {
        this.is_follow = is_follow;
    }
}
