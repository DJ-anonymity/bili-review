package com.zfg.learn.service;

import com.zfg.learn.common.ServerResponse;
import com.zfg.learn.model.po.LongReview;

import java.io.IOException;
import java.util.List;

public interface LongReviewService {

    public ServerResponse pullAllLongReviewFromBiliApi(Integer media_id) throws IOException;

    public ServerResponse pullNewLongReviewFromBiliApi(Integer media_id) throws IOException;

    public ServerResponse insertLongReviews(List<LongReview> longReviewList);

    public ServerResponse insertLongReview(LongReview longReview);

    public ServerResponse deleteLongReviewByReview_id(Integer review_id);

    public ServerResponse list(Integer media_id, Integer score, Integer sort, Integer pageNum, Integer pageSize);

    public ServerResponse searchReviewByKeyword(Integer media_id, String keyword, Integer score, Integer sort, Integer pageNum, Integer pageSize);

    public ServerResponse searchReviewByMid(Integer mid, Integer sort, Integer pageNum, Integer pageSize);

}
