package com.zfg.learn.dao;

import com.zfg.learn.model.po.Subscription;
import com.zfg.learn.model.query.SubscriptionQuery;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 订阅内容 持久化层
 */
@Repository
public interface SubscriptionMapper {
    /**
     * 插入
     * @param subscription
     */
    void insert(Subscription subscription);

    /**
     * 通过内容查找订阅者
     * @param fid
     * @return
     */
    List<Integer> selectFollowerByFid(@Param("fid") Integer fid);

    /**
     * 自定义条件查询 不建议经常使用
     * @return
     */
    List<Subscription> selectByMultiple(SubscriptionQuery subQuery);
}
