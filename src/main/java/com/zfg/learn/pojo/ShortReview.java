package com.zfg.learn.pojo;

/*
* 评论的内容
* */
public class ShortReview {

    //作者
    private User author;
    //内容
    private String content;
    //猜测是时间
    private Long ctime;
    //番剧的id
    private Integer media_id;
    //作者的id
    private Integer mid;
    //最终修改时间
    private Long mtime;
    //所属动漫
    private Animation animation;
    //看到第几集
    private String progress;
    //该review的id
    private Integer review_id;
    //评分
    private Integer score;
    //点赞和回复
    private Stat stat;

    @Override
    public String toString() {
        return "ShortReview{" +
                "author=" + author +
                ", content='" + content + '\'' +
                ", ctime=" + ctime +
                ", media_id=" + media_id +
                ", mid=" + mid +
                ", mtime=" + mtime +
                ", progress='" + progress + '\'' +
                ", review_id=" + review_id +
                ", score=" + score +
                ", stat=" + stat +
                '}'+"\n\r";
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    public Integer getMedia_id() {
        return media_id;
    }

    public void setMedia_id(Integer media_id) {
        this.media_id = media_id;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public Long getMtime() {
        return mtime;
    }

    public void setMtime(Long mtime) {
        this.mtime = mtime;
    }

    public Animation getAnimation() {
        return animation;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public Integer getReview_id() {
        return review_id;
    }

    public void setReview_id(Integer review_id) {
        this.review_id = review_id;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Stat getStat() {
        return stat;
    }

    public void setStat(Stat stat) {
        this.stat = stat;
    }
}

