package com.zfg.learn.dao;

import com.zfg.learn.pojo.Animation;
import com.zfg.learn.pojo.ShortReview;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimationMapper {

    public List<Animation> selectAllAnimation(@Param("persistenceMark") Integer persistenceMark);

    public Animation selectAnimationByMedia_id(Integer media_id);

    public Integer selectLongReviewPersistenceMarkByMedia_id(Integer media_id);

    public Integer selectShortReviewPersistenceMarkByMedia_id(Integer media_id);


    public Integer updateLongReviewPersistenceMarkByMedia_id(@Param("persistenceMark") Integer persistenceMark, Integer media_id);

    public Integer updateShortReviewPersistenceMarkByMedia_id(@Param("persistenceMark") Integer persistenceMark, Integer media_id);

    public Integer insertAnimation(Animation animation);
}
