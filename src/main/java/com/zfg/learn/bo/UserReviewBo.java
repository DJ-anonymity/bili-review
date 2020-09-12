package com.zfg.learn.bo;
/*
* 存放用户的评论相关信息
* */
public class UserReviewBo {
    private Integer mid;
    private Integer shortReviewQuantity;
    private Integer longReviewQuantity;

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public Integer getShortReviewQuantity() {
        return shortReviewQuantity;
    }

    public void setShortReviewQuantity(Integer shortReviewQuantity) {
        this.shortReviewQuantity = shortReviewQuantity;
    }

    public Integer getLongReviewQuantity() {
        return longReviewQuantity;
    }

    public void setLongReviewQuantity(Integer longReviewQuantity) {
        this.longReviewQuantity = longReviewQuantity;
    }

    @Override
    public String toString() {
        return "UserReviewBo{" +
                "mid=" + mid +
                ", shortReviewQuantity=" + shortReviewQuantity +
                ", longReviewQuantity=" + longReviewQuantity +
                '}';
    }
}
