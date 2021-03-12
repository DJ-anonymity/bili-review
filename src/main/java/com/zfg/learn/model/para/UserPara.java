package com.zfg.learn.model.para;


import com.zfg.learn.model.po.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 接收请求参数
 * @author bootzhong.top
 */
public class UserPara extends User {
    @NotNull(groups = Register.class)
    private Integer checkNum;

    public Integer getCheckNum() {
        return checkNum;
    }

    public void setCheckNum(Integer checkNum) {
        this.checkNum = checkNum;
    }
}
