package com.zfg.learn.dao;

import com.zfg.learn.pojo.ShortReview;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShortReviewMapper {

    public List<ShortReview> findShortReviewByMediaId(Integer mediaId);
    public ShortReview selectShortReviewByReview_id(Integer review_id);
    public List<Integer> selectAllReview_id();
    public ShortReview selectLatestReviewByMedia_id(Integer media_id);

    public Integer insertShortReview(ShortReview ShortReview);

    public Integer updateContentByReview_id(ShortReview shortReview);

    public Integer deleteShortReviewByReview_id(Integer review_id);


}
