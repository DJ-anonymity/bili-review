package com.zfg.learn.service;

import com.zfg.learn.common.ServerResponse;
import com.zfg.learn.pojo.ShortReview;

import java.io.IOException;
import java.util.List;

public interface ShortReviewService {

    public ServerResponse pullAllShortReviewFromBiliApi(Integer media_id) throws IOException;

    public ServerResponse pullNewShortReviewFromBiliApi(Integer media_id) throws IOException;

    public ServerResponse insertShortReviews(List<ShortReview> ShortReviewList);
    public ServerResponse insertShortReview(ShortReview shortReview);

    public ServerResponse deleteShortReviewByReview_id(Integer review_id);


}
