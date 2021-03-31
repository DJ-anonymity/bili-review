package com.zfg.learn.dao;

import com.zfg.learn.model.dto.SubscriptionDto;
import com.zfg.learn.model.po.Subscription;
import com.zfg.learn.model.query.SubscriptionQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import javax.websocket.server.PathParam;
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
     * 通过内容查找订阅者QQ
     * @param fid
     * @return
     */
    List<Long> selectFollowerByFid(@Param("fid") Integer fid);

    /**
     * 自定义条件查询 不建议经常使用
     * @return
     */
    List<Subscription> selectByMultiple(SubscriptionQuery subQuery);

    /**
     * 通过内容查找订阅者
     * @param fid
     * @return
     */
    Subscription selectRelation(@Param("uid") Integer uid, @PathParam("fid") Integer fid, @PathParam("type") Integer type);

    /**
     * 更新订阅状态
     * fid， mid， uid一定不能为空！！！
     * @param subscription
     */
    void updateStatus(Subscription subscription);

    void selectByUid(Integer uid, Integer type);

    /**
     * 查询用户订阅的所有media
     * @param uid
     */
    List<SubscriptionDto> selectMediaFollows(Integer uid);

    /**
     * 查询用户订阅的所有UP
     * @param uid
     */
    List<SubscriptionDto> selectUpFollows(Integer uid);
}
