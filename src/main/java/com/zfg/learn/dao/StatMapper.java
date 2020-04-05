package com.zfg.learn.dao;

import com.zfg.learn.pojo.LongReview;
import com.zfg.learn.pojo.Stat;
import org.springframework.stereotype.Repository;

@Repository
public interface StatMapper {
    public Integer insertStatByArticle_id(Stat stat);
    public Integer insertStatByReview_id(Stat stat);

    public Integer updateStatByArticle_id(Stat stat);
    public Integer updateStatByReview_id(Stat stat);

    public Integer deleteStatByArticle_id(Integer Article_id);

    public Integer deleteStatByReview_id(Integer Review_id);
}
