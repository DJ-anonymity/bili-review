package com.zfg.learn.dao;

import com.zfg.learn.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

    public User selectUserByMid(Integer mid);

    public Integer insertUser(User user);

    public Integer insertUserList(@Param("userList") List<User> userList);

    public Integer deleteUserByMid(Integer mid);
}
