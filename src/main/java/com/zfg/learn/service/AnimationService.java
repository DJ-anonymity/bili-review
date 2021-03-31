package com.zfg.learn.service;

import com.zfg.learn.model.bo.AnimationReviewBo;
import com.zfg.learn.common.ServerResponse;
import com.zfg.learn.model.po.Animation;

import java.io.IOException;

public interface AnimationService {

    public ServerResponse list(Integer persistenceMark, Integer pageNum, Integer pageSize);

    public AnimationReviewBo getReviewQuantity(Integer media_id) throws IOException;

    public Animation findAnimationByMedia_id(Integer media_id);

    public Animation pullNewAnimation(Integer media_id) throws IOException;
}
