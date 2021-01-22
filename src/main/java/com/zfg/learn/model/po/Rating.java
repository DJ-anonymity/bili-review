package com.zfg.learn.model.po;

public class Rating {
    private Integer media_id;
    private Float score;
    private Integer count;

    public Integer getMedia_id() {
        return media_id;
    }

    public void setMedia_id(Integer media_id) {
        this.media_id = media_id;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "media_id=" + media_id +
                ", score=" + score +
                ", count=" + count +
                '}';
    }
}
