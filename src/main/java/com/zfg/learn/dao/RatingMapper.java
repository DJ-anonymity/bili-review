package com.zfg.learn.dao;

import com.zfg.learn.model.po.Rating;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingMapper {

    public Integer insertRating(Rating rating);
}
