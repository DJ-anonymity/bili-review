package com.zfg.learn.service.serviceImpl;

import com.zfg.learn.dao.SubscriptionMapper;
import com.zfg.learn.exception.SeleniumException;
import com.zfg.learn.exception.ServiceException;
import com.zfg.learn.model.po.Subscription;
import com.zfg.learn.model.query.SubscriptionQuery;
import com.zfg.learn.service.SubscriptionService;
import com.zfg.learn.until.SeleniumBiliUntil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 订阅
 */
@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    @Autowired
    SubscriptionMapper subMapper;

    @Override
    @Transactional
    public void sub(Subscription subscription) {
        //如果不存在 存到数据库中
        SubscriptionQuery subQuery = new SubscriptionQuery();
        subQuery.setFid(subscription.getFid());
        subQuery.setUid(subscription.getUid());

        //加锁防止线程安全问题  ：多个线程同时执行查询到空的subList 就会造成重复插入
        synchronized (this){
            List<Subscription> subList = subMapper.selectByMultiple(subQuery);
            if (CollectionUtils.isEmpty(subList)){
                throw new ServiceException("不能重复订阅");
            }
            subMapper.insert(subscription);
        }

        //同时使用 b站账号关注 关注失败则抛出异常回滚事务
        try {
            SeleniumBiliUntil selenium = SeleniumBiliUntil.getInstance();
            selenium.subscribe(subscription.getFid(), subscription.getType());
        } catch (SeleniumException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }

    }
}
