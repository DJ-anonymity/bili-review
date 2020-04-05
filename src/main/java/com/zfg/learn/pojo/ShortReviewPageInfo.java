package com.zfg.learn.pojo;


import java.util.List;

/*
* 存储评论的分页信息
* */
public class ShortReviewPageInfo {

    //该分页的内容
    private List<ShortReview> list;
    //下一页的内容
    private Long  next;
    //总评论的总数
    private int total;

    public List<ShortReview> getList() {
        return list;
    }

    public void setList(List<ShortReview> list) {
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
