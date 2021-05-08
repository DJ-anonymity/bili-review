package com.zfg.learn.model.po;

import io.swagger.annotations.ApiModelProperty;

/**
 * 评论
 */
public class Comment {
    @ApiModelProperty("评论id")
    private Long id;

    @ApiModelProperty("根评论节点id")
    private Long root;

    @ApiModelProperty("父评论节点id")
    private Long parent;

    @ApiModelProperty("父评论节点内容")
    private String parent_info;

    @ApiModelProperty("用户头像")
    private String uface;

    @ApiModelProperty("是否已经充电")
    private Integer is_elec;

    @ApiModelProperty("关系")
    private Integer relation;

    @ApiModelProperty("url")
    private String url;

    @ApiModelProperty("视频bv")
    private String BVId;

    @ApiModelProperty("创建时间")
    private String ctime;

    @ApiModelProperty("更新时间")
    private String mtime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getBVId() {
        return BVId;
    }

    public void setBVId(String bvId) {
        this.BVId = bvId;
    }
}
