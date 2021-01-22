package com.zfg.learn.dao;

import com.zfg.learn.model.po.ShortReview;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShortReviewMapper {
    //不使用联合查询
    public List<ShortReview> findShortReviewByMediaId(Integer media_id);

    public List<ShortReview> selectReviewByMedia_id(@Param("media_id") Integer media_id, @Param("score") Integer score, @Param("sortType") String sortType);
    public List<ShortReview> selectReviewByKeyWord(@Param("media_id") Integer media_id, @Param("keyword") String keyword, @Param("score") Integer score, @Param("sortType")String sortType);
    public List<ShortReview> selectReviewByMid(@Param("mid") Integer mid, @Param("sortType") String sortType);
    public ShortReview selectShortReviewByReview_id(Integer review_id);
    public List<Integer> selectAllReview_id();
    public Integer selectReviewQuantityByMedia_id(Integer media_id);
    public Integer selectReviewQuantityByMid(Integer mid);

    public List<ShortReview> selectReviewByMtime(Long mtime);

    public ShortReview selectLatestReviewByMedia_id(Integer media_id);

    public Integer insertShortReview(ShortReview ShortReview);

    public Integer insertShortReviewList(@Param("reviewList") List<ShortReview> reviewList);

    public Integer updateContentByReview_id(ShortReview shortReview);

    public Integer deleteShortReviewByReview_id(Integer review_id);

    public Integer deleteShortReviewByMedia_id(Integer media_id);


}
