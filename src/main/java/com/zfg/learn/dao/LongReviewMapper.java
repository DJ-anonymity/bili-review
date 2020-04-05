package com.zfg.learn.dao;

import com.zfg.learn.pojo.LongReview;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LongReviewMapper {

    public List<LongReview> findLongReviewByMedia_id(Integer mediaId);
    public LongReview selectLongReviewByReview_id(Integer review_id);
    public LongReview selectLatestReviewByMedia_id(Integer media_id);

    public Integer insertLongReview(LongReview longReview);

    public Integer updateContentByReview_id(LongReview longReview);

    public Integer deleteLongReviewByReview_id(Integer review_id);


}
