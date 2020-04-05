package com.zfg.learn.pojo;

public class Stat {

    //长评的id
    private Integer article_id;
    //短评的id
    private Integer review_id;
    //赞
    private Integer likes;
    //回复
    private Integer reply;

    public Integer getArticle_id() {
        return article_id;
    }

    public void setArticle_id(Integer article_id) {
        this.article_id = article_id;
    }

    public Integer getReview_id() {
        return review_id;
    }

    public void setReview_id(Integer review_id) {
        this.review_id = review_id;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getReply() {
        return reply;
    }

    public void setReply(Integer reply) {
        this.reply = reply;
    }

    @Override
    public String toString() {
        return "Stat{" +
                "article_id=" + article_id +
                ", review_id=" + review_id +
                ", likes=" + likes +
                ", reply=" + reply +
                '}';
    }
}
