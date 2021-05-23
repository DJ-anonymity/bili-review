package com.zfg.learn.controller.portal;

import com.zfg.learn.common.Const;
import com.zfg.learn.common.ServerResponse;
import com.zfg.learn.model.po.Comment;
import com.zfg.learn.model.po.User;
import com.zfg.learn.service.UnreadCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 评论控制层
 */
@RequestMapping("/portal/comment")
@RestController
public class CommentController {
    @Autowired
    UnreadCommentService commentService;

    @GetMapping("/list")
    public ServerResponse list(@RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(value = "sort", defaultValue = "0") Integer sort,
                               @RequestParam(value = "is_elec", required = false)Integer is_elec,
                               @RequestParam(value = "relation", required = false)Integer relation, HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        List<Comment> list = commentService.list(keyword, sort, is_elec, relation, user);
        return ServerResponse.createBySuccess(list);
    }
}
