package com.zfg.learn.dao;

import com.zfg.learn.pojo.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    public User selectUserByMid(Integer mid);

    public Integer insertUser(User user);

    public Integer deleteUserByMid(Integer mid);
}
