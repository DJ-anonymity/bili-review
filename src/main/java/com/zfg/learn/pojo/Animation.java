package com.zfg.learn.pojo;

public class Animation {
    private Integer season_id;
    private Integer media_id;
    private String title;
    //封面
    private String cover;
    private Integer is_finish;
    private Rating rating;

    //评价是否已经持久化的标志
    private Integer longReviewPersistenceMark;

    public Integer getSeason_id() {
        return season_id;
    }

    public void setSeason_id(Integer season_id) {
        this.season_id = season_id;
    }

    public Integer getMedia_id() {
        return media_id;
    }

    public void setMedia_id(Integer media_id) {
        this.media_id = media_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Integer getIs_finish() {
        return is_finish;
    }

    public void setIs_finish(Integer is_finish) {
        this.is_finish = is_finish;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public Integer getLongReviewPersistenceMark() {
        return longReviewPersistenceMark;
    }

    public void setLongReviewPersistenceMark(Integer longReviewPersistenceMark) {
        this.longReviewPersistenceMark = longReviewPersistenceMark;
    }

    @Override
    public String toString() {
        return "animation{" +
                "season_id=" + season_id +
                ", media_id=" + media_id +
                ", title='" + title + '\'' +
                ", cover='" + cover + '\'' +
                ", is_finish=" + is_finish +
                ", rating=" + rating +
                ", longReviewPersistenceMark=" + longReviewPersistenceMark +
                '}';
    }
}


