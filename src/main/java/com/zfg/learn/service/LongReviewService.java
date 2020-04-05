package com.zfg.learn.service;

import com.zfg.learn.common.ServerResponse;
import com.zfg.learn.pojo.LongReview;

import java.io.IOException;
import java.util.List;

public interface LongReviewService {

    public ServerResponse pullAllLongReviewFromBiliApi(Integer media_id) throws IOException;

    public ServerResponse pullNewLongReviewFromBiliApi(Integer media_id) throws IOException;

    public ServerResponse insertLongReviews(List<LongReview> longReviewList);
    public ServerResponse insertLongReview(LongReview longReview);

    public ServerResponse deleteLongReviewByReview_id(Integer review_id);


}
