package com.zfg.learn.model.po;

import io.swagger.annotations.ApiModelProperty;

/**
 * 评论
 */
public class Comment {
    @ApiModelProperty("评论id")
    private Long id;

    @ApiModelProperty("评论内容")
    private String message;

    @ApiModelProperty("点赞数")
    private Integer like;

    @ApiModelProperty("总回复数")
    private Integer count;

    @ApiModelProperty("根评论节点id")
    private Long root;

    @ApiModelProperty("父评论节点id")
    private Long parent;

    @ApiModelProperty("父评论节点内容")
    private String parent_info;

    @ApiModelProperty("用户头像")
    private String uface;

    @ApiModelProperty("用户名")
    private String replier;

    @ApiModelProperty("用户mid")
    private Integer mid;

    @ApiModelProperty("是否已经充电")
    private Integer is_elec;

    @ApiModelProperty("关系")
    private Integer relation;

    @ApiModelProperty("url")
    private String url;

    @ApiModelProperty("视频bv")
    private String bvid;

    @ApiModelProperty("视频封面")
    private String cover;

    @ApiModelProperty("视频标题")
    private String title;

    @ApiModelProperty("创建时间")
    private String ctime;

    @ApiModelProperty("更新时间")
    private String mtime;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getLike() {
        return like;
    }

    public void setLike(Integer like) {
        this.like = like;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public String getReplier() {
        return replier;
    }

    public void setReplier(String replier) {
        this.replier = replier;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getRoot() {
        return root;
    }

    public void setRoot(Long root) {
        this.root = root;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public String getParent_info() {
        return parent_info;
    }

    public void setParent_info(String parent_info) {
        this.parent_info = parent_info;
    }

    public String getUface() {
        return uface;
    }

    public void setUface(String uface) {
        this.uface = uface;
    }

    public Integer getIs_elec() {
        return is_elec;
    }

    public void setIs_elec(Integer is_elec) {
        this.is_elec = is_elec;
    }

    public Integer getRelation() {
        return relation;
    }

    public void setRelation(Integer relation) {
        this.relation = relation;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getMtime() {
        return mtime;
    }

    public void setMtime(String mtime) {
        this.mtime = mtime;
    }

    public String getBvid() {
        return bvid;
    }

    public void setBvid(String bvid) {
        this.bvid = bvid;
    }
}
