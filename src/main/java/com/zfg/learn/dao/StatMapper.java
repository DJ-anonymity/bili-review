package com.zfg.learn.dao;

import com.zfg.learn.pojo.LongReview;
import com.zfg.learn.pojo.Stat;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatMapper {
    public Integer insertShortReviewStat(Stat stat);
    public Integer insertLongReviewStat(Stat stat);
    public Integer insertShortReviewStatList(@Param("statList") List<Stat> statList);
    public Integer insertLongReviewStatList(@Param("statList") List<Stat> statList);

    public Integer updateStatByArticle_id(Stat stat);
    public Integer updateStatByReview_id(Stat stat);

    public Integer deleteStatByArticle_id(Integer Article_id);

    public Integer deleteStatByReview_id(Integer Review_id);
}
