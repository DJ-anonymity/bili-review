package com.zfg.learn.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfg.learn.common.Const;
import com.zfg.learn.common.UserEnum;
import com.zfg.learn.dao.SubscriptionMapper;
import com.zfg.learn.exception.SeleniumException;
import com.zfg.learn.exception.ServiceException;
import com.zfg.learn.model.dto.SubscriptionDto;
import com.zfg.learn.model.po.Subscription;
import com.zfg.learn.service.SubscriptionService;
import com.zfg.learn.until.SeleniumBiliUntil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        // todo 同步锁失效
        synchronized (this){
            Subscription originalSub = subMapper.selectRelation(subscription.getUid(), subscription.getFid(), subscription.getType());
            if (originalSub == null){
                //如果是插入操作则设置创建时间
                subscription.setCtime(subscription.getMtime());
                subMapper.insert(subscription);
            } else {
                subMapper.updateStatus(subscription);
            }
        }

        //如果操作是关注 则使用B站Bot账号关注
        if (subscription.getStatus() == Const.Sub.FOLLOW){
            synchronized (this){
                //先查询Bot是否已经关注过该内容了 tip:这里默认空就是不订阅 以后拓展要修改
                Subscription sub = subMapper.selectRelation(UserEnum.BOT.getUid(), subscription.getFid(), subscription.getType());
                if (sub != null){
                    return;
                }

                try {
                    //通过sn操作bot账号去关注内容
                    SeleniumBiliUntil selenium = SeleniumBiliUntil.getInstance();
                    selenium.subscribe(subscription.getFid(), subscription.getType());

                    //关注成功后让bot订阅该内容然后插入数据库
                    Subscription botSub = subscription;
                    botSub.setUid(UserEnum.BOT.getUid());
                    subMapper.insert(botSub);
                } catch (SeleniumException e) {
                    e.printStackTrace();
                    throw new ServiceException(e.getMessage());
                }
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
    public Integer getSubNum(Integer uid) {
        return subMapper.selectSubNum(uid);
    }


}
