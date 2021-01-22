package com.zfg.learn.model.bo;

public class KeywordCountBo {
    private String keyword;
    private Integer mid;
    private double num;

    public KeywordCountBo(){

    }
    public KeywordCountBo(String keyword, double num){
        this.keyword = keyword;
        this.num = num;
    }
    public KeywordCountBo(Integer mid, double num){
        this.mid = mid;
        this.num = num;
    }
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public double getNum() {
        return num;
    }

    public void setNum(double num) {
        this.num = num;
    }
}
