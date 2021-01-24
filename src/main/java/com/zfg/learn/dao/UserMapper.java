package com.zfg.learn.dao;

import com.zfg.learn.model.po.User;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 用户持久化层
 * @author bootzhong
 */
@Mapper
@Repository
public interface UserMapper {

    /**
     * 通过用户名获取账号
     * @param account
     * @return
     */
    User selectUserByAccount(@Param("account") String account);

    /**
     * 增加用户
     * @param user
     * @return
     */
    Integer insert(User user);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    Integer updateCookie(User user);

    /**
     * 绑定b站账号
     */
    Integer bindBiliCount(User user);
}
