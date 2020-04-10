package com.zfg.learn.service;

import com.zfg.learn.common.ServerResponse;

import java.io.IOException;

public interface AnimationService {

    public ServerResponse list(Integer pageNum, Integer pageSize);

    public ServerResponse findAnimationByMedia_id(Integer media_id);

    public ServerResponse pullNewAnimation(Integer media_id) throws IOException;
}
