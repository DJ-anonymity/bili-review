package com.zfg.learn.dao;

import com.zfg.learn.pojo.Animation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimationMapper {

    public Integer selectLongReviewPersistenceMarkByMedia_id(Integer media_id);

    public Integer selectShortReviewPersistenceMarkByMedia_id(Integer media_id);

    public Integer updateLongReviewPersistenceMarkByMedia_id(@Param("persistenceMark") Integer persistenceMark, Integer media_id);

    public Integer updateShortReviewPersistenceMarkByMedia_id(@Param("persistenceMark") Integer persistenceMark, Integer media_id);
}
