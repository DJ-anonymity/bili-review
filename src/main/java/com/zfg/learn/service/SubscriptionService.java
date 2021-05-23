package com.zfg.learn.service;

import com.github.pagehelper.PageInfo;
import com.zfg.learn.model.dto.SubscriptionDto;
import com.zfg.learn.model.po.Subscription;

/**
 * 订阅业务层
 */
public interface SubscriptionService {

    void modify(Subscription subscription);

    PageInfo<SubscriptionDto> list(Integer uid, Integer type, Integer pageNum, Integer pageSize);

    Subscription getRelation(Integer uid, Integer fid, Integer type);

    Integer getSubNum(Integer uid);
}
