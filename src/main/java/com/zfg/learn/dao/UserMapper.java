package com.zfg.learn.dao;

import com.zfg.learn.model.para.UserPara;
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
     * @param username
     * @return
     */
    User selectByName(@Param("username") String username);

    /**
     * 通过用户名获取账号
     * @param email
     * @return
     */
    User selectByEmail(@Param("email") String email);

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

    /**
     * 修改用户的权限
     */
    Integer updateRoleByUid(@Param("role") Integer role, @Param("uid") Integer uid);

    /**
     * 多条件更新
     * @param user
     * @return
     */
    Integer updateMultipleByUid(UserPara user);
}
