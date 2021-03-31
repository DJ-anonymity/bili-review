package com.zfg.learn.service.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfg.learn.common.Const;
import com.zfg.learn.dao.SubscriptionMapper;
import com.zfg.learn.exception.SeleniumException;
import com.zfg.learn.exception.ServiceException;
import com.zfg.learn.model.bo.PublicTask;
import com.zfg.learn.model.dto.SubscriptionDto;
import com.zfg.learn.model.po.Subscription;
import com.zfg.learn.model.query.SubscriptionQuery;
import com.zfg.learn.service.SubscriptionService;
import com.zfg.learn.until.CatchApi;
import com.zfg.learn.until.SeleniumBiliUntil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订阅
 */
@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    @Autowired
    SubscriptionMapper subMapper;

    /**
     * 修改订阅状态
     * @param subscription
     */
    @Override
    @Transactional
    public void modify(Subscription subscription) {
        //同步锁 防止多线程同时查询出无订阅记录  导致重复插入
        synchronized (this){
            Subscription originalSub = subMapper.selectRelation(subscription.getUid(), subscription.getFid(), subscription.getType());

            if (originalSub == null){
                subscription.setCtime(subscription.getMtime());
                subMapper.insert(subscription);
            } else {
                subMapper.updateStatus(subscription);
            }
        }

        //已经关注的怎么处理

        if (subscription.getStatus() == Const.Sub.FOLLOW){
            //同时使用 b站账号关注 关注失败则抛出异常回滚事务 todo 关注不能太过频繁 待处理
            try {
                SeleniumBiliUntil selenium = SeleniumBiliUntil.getInstance();
                selenium.subscribe(subscription.getFid(), subscription.getType());
            } catch (SeleniumException e) {
                e.printStackTrace();
                throw new ServiceException(e.getMessage());
            }
        }
    }

    /**
     * 查询订阅列表
     * @param uid
     * @param type
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<SubscriptionDto> list(Integer uid, Integer type, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SubscriptionDto> subDtoList;

        if (type == Const.Sub.TYPE_MEDIA){
            subDtoList = subMapper.selectMediaFollows(uid);
        } else {
            subDtoList = subMapper.selectUpFollows(uid);
        }

        PageInfo<SubscriptionDto> pageInfo = new PageInfo(subDtoList);
        return pageInfo;
    }

    @Override
    public Subscription getRelation(Integer uid, Integer fid, Integer type) {
        return subMapper.selectRelation(uid, fid, type);
    }

    @Override
    public void pushDynamic(PublicTask task) {
        List<Long> qqList = subMapper.selectFollowerByFid(task.getPid());
        if (!CollectionUtils.isEmpty(qqList)){
            task.setRecList(qqList);
        }

        task.setRecList(qqList);

        CatchApi catchApi = new CatchApi();
        try {
            catchApi.request("http://127.0.0.1:8081/robot/push", JSONObject.toJSONString(task));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
