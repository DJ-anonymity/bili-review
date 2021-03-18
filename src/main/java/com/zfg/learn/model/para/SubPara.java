package com.zfg.learn.model.para;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 订阅参数
 */
public class SubPara {
    //订阅的内容id
    @NotBlank
    private String fid;

    //订阅的内容类型
    @NotNull
    private Integer type;

    //订阅级别
    private Integer level;

    //订阅状态
    private Integer status;

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
