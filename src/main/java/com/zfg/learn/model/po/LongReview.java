package com.zfg.learn.model.po;

/*
* 评论的内容
* */
public class LongReview {

    //文章的id
    private Integer article_id;
    //作者
    private BiliUser author;
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
    //看到第几集
    private String progress;
    //该review的id
    private Integer review_id;
    //评分
    private Integer score;
    //点赞和回复
    private Stat stat;
    //标题
    private String title;
    //url
    private String url;

    public Integer getArticle_id() {
        return article_id;
    }

    public void setArticle_id(Integer article_id) {
        this.article_id = article_id;
    }

    public BiliUser getAuthor() {
        return author;
    }

    public void setAuthor(BiliUser author) {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "LongReview{" +
                "article_id=" + article_id +
                ", author=" + author +
                ", content='" + content + '\'' +
                ", ctime=" + ctime +
                ", media_id=" + media_id +
                ", mid=" + mid +
                ", mtime=" + mtime +
                ", progress='" + progress + '\'' +
                ", review_id=" + review_id +
                ", score=" + score +
                ", stat=" + stat +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}'+"\n\r";
    }
}

