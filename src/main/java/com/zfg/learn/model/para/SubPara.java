package com.zfg.learn.model.para;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 订阅参数
 */
public class SubPara {
    //订阅的内容id
    @NotNull
    private Integer fid;

    //订阅的内容类型
    @NotNull
    private Integer type;

    //订阅状态
    @NotNull
    private Integer status;

    //订阅级别
    private Integer level;

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
