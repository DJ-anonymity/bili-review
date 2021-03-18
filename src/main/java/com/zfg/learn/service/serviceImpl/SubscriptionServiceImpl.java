package com.zfg.learn.service.serviceImpl;

import com.zfg.learn.dao.SubscriptionMapper;
import com.zfg.learn.model.po.Subscription;
import com.zfg.learn.service.SubscriptionService;
import com.zfg.learn.until.SeleniumBiliUntil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订阅
 */
@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    @Autowired
    SubscriptionMapper subscriptionMapper;

    @Override
    @Transactional
    public void sub(Subscription subscription) {
        //如果不存在 存到数据库中

        //同时使用 b站账号关注
        SeleniumBiliUntil selenium = SeleniumBiliUntil.getInstance();
        selenium.subscribe(subscription.getFid(), subscription.getType());

    }
}
