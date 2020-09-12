package com.zfg.learn.serviceImpl;

import com.zfg.learn.bo.UserReviewBo;
import com.zfg.learn.common.ServerResponse;
import com.zfg.learn.dao.LongReviewMapper;
import com.zfg.learn.dao.ShortReviewMapper;
import com.zfg.learn.pojo.ShortReview;
import com.zfg.learn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    LongReviewMapper longReviewMapper;
    @Autowired
    ShortReviewMapper shortReviewMapper;

    @Override
    public UserReviewBo getReviewQuantity(Integer mid) {
        UserReviewBo userReviewBo = new UserReviewBo();
        userReviewBo.setMid(mid);
        userReviewBo.setShortReviewQuantity(shortReviewMapper.selectReviewQuantityByMid(mid));
        userReviewBo.setLongReviewQuantity(longReviewMapper.selectReviewQuantityByMid(mid));
        return userReviewBo;
    }
}
