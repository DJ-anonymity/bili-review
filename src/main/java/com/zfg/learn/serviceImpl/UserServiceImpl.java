package com.zfg.learn.serviceImpl;

import com.zfg.learn.bo.UserReviewBo;
import com.zfg.learn.dao.LongReviewMapper;
import com.zfg.learn.dao.ShortReviewMapper;
import com.zfg.learn.pojo.User;
import com.zfg.learn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 用户业务层, 主要处理user和biliUser
 * @Author bootzhong
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    LongReviewMapper longReviewMapper;
    @Autowired
    ShortReviewMapper shortReviewMapper;
    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 获取用户发表的评论数量
     * @param mid
     * @return UserReviewBo
     */
    @Override
    public UserReviewBo getReviewQuantity(Integer mid) {
        UserReviewBo userReviewBo = new UserReviewBo();
        userReviewBo.setMid(mid);
        userReviewBo.setShortReviewQuantity(shortReviewMapper.selectReviewQuantityByMid(mid));
        userReviewBo.setLongReviewQuantity(longReviewMapper.selectReviewQuantityByMid(mid));
        return userReviewBo;
    }

    /**
     * 验证当前cookie是否可用
     * @return  Boolean
     */
    @Override
    public Boolean checkBiliCookie(String loginCookie) {

        //能用则直接存进redis中 并更新数据库

        return null;
    }

    /**
     * 获取当前浏览器的B站登陆Cookie
     * @return
     */
    @Override
    public User getBiliCookie() {
        return null;
    }
}
