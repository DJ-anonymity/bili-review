package com.zfg.learn.dao;

import com.zfg.learn.pojo.LongReview;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LongReviewMapper {

    public List<LongReview> selectReviewByMedia_id(@Param("media_id")Integer media_id, @Param("score")Integer score, @Param("sortType")String sortType);
    public List<LongReview> selectReviewByKeyWord(@Param("media_id")Integer media_id, @Param("keyword")String keyword, @Param("score")Integer score, @Param("sortType")String sortType);
    public List<LongReview> selectReviewByMid(@Param("mid")Integer mid, @Param("sortType")String sortType);
    public LongReview selectLongReviewByReview_id(Integer review_id);
    public LongReview selectLatestReviewByMedia_id(Integer media_id);
    public Integer selectReviewQuantityByMedia_id(Integer media_id);
    public Integer selectReviewQuantityByMid(Integer mid);

    public Integer insertLongReview(LongReview longReview);

    public Integer insertLongReviewList(@Param("reviewList") List<LongReview> longReviewList);

    public Integer updateContentByReview_id(LongReview longReview);

    public Integer deleteLongReviewByReview_id(Integer review_id);


}
