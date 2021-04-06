package com.zfg.learn.model.bo;

import java.util.List;

public class QQTask {
    private String txt;

    private String img;

    private List<Long> recList;

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public List<Long> getRecList() {
        return recList;
    }

    public void setRecList(List<Long> recList) {
        this.recList = recList;
    }
}
