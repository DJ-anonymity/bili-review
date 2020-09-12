package com.zfg.learn.until;

import com.zfg.learn.common.Const;
import com.zfg.learn.common.ServerResponse;

public class SortUntil {

    public String convertToReviewSortType(Integer sort){
        String sortType;
        if (sort == Const.SortReview.MTIME_ASC){
            sortType = "MTIME ASC";
        } else if (sort == Const.SortReview.MTIME_DESC) {
            sortType = "MTIME DESC";
        } else if (sort == Const.SortReview.LIKES_ASC) {
            sortType = "stat.LIKES DESC";
        } else {
            sortType = "SCORE DESC";//为了不影响用户的体验，默认排序为分数最高排前
        }
        return sortType;
    }
}
