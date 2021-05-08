package com.zfg.learn.service;

import com.zfg.learn.model.po.Comment;
import com.zfg.learn.model.po.User;

import java.util.List;

/**
 * 未读消息处理业务层
 */
public interface UnreadCommentService {
    public List<Comment> list(Integer sort, Integer is_elec, Integer relation, User curUser);


    public List<Comment> search(String keyword, Integer sort, Integer condition, User curUser);
}
