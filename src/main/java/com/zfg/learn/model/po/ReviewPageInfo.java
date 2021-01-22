package com.zfg.learn.model.po;


import java.util.List;

/*
* 存储评论的分页信息
* */
public class ReviewPageInfo {

    //该分页的内容
    private List<LongReview> list;
    //下一页的内容
    private Long  next;
    //总评论的总数
    private int total;

    public List<LongReview> getList() {
        return list;
    }

    public void setList(List<LongReview> list) {
        this.list = list;
    }

    public Long getNext() {
        return next;
    }

    public void setNext(Long next) {
        this.next = next;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "CommentaryPageInfo{" +
                "list=" + list +
                ", next=" + next +
                ", total=" + total +
                '}'+"\n\r";
    }
}
