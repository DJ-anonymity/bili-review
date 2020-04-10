package com.zfg.learn.dao;

import com.zfg.learn.pojo.ShortReview;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShortReviewMapper {

    public List<ShortReview> selectReviewByMedia_id(@Param("media_id")Integer media_id, @Param("sortType") String sortType);
    public List<ShortReview> selectReviewByKeyWord(@Param("media_id") Integer media_id, @Param("keyword") String keyword, @Param("sortType")String sortType);
    public List<ShortReview> selectReviewByMid(@Param("mid") Integer mid, @Param("sortType") String sortType);
    public ShortReview selectShortReviewByReview_id(Integer review_id);
    public List<Integer> selectAllReview_id();

    public List<ShortReview> selectReviewByMtime(Long mtime);

    public ShortReview selectLatestReviewByMedia_id(Integer media_id);

    public Integer insertShortReview(ShortReview ShortReview);

    public Integer updateContentByReview_id(ShortReview shortReview);

    public Integer deleteShortReviewByReview_id(Integer review_id);


}
