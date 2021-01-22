package com.zfg.learn.dao;

import com.zfg.learn.model.po.BiliUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BiliUserMapper {

    public BiliUser selectUserByMid(Integer mid);

    public Integer insertUser(BiliUser user);

    public Integer insertUserList(@Param("userList") List<BiliUser> userList);

    public Integer deleteUserByMid(Integer mid);
}
