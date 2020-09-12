package com.zfg.learn.service;

import com.zfg.learn.bo.UserReviewBo;
import com.zfg.learn.common.ServerResponse;

public interface UserService {

    public UserReviewBo getReviewQuantity(Integer mid);
}
