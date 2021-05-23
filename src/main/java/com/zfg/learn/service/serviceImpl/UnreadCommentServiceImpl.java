package com.zfg.learn.service.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.zfg.learn.common.Const;
import com.zfg.learn.common.RedisConst;
import com.zfg.learn.model.po.Comment;
import com.zfg.learn.model.po.User;
import com.zfg.learn.service.UnreadCommentService;
import com.zfg.learn.until.CatchApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UnreadCommentServiceImpl implements UnreadCommentService {
    @Autowired
    private RedisTemplate redisTemplate;
    private CatchApi catchApi = new CatchApi();
    private static final int DEFAULT = 0;//全部
    private static final int IS_SUB = 1;//已关注
    private static final int ROOT = 0;//已充电
    private static final int SORT_TIME_DESC = 0; //最近发布
    private static final int SORT_LIKE_DESC = 1; //点赞最多
    private static final int SORT_REPLY_DESC = 2; //回复最多

    /**
     * 查找出未读消息列表  max=999
     * @param sort 排序方式
     * @param is_elec 是否充电
     * @param relation 好友关系
     * @param curUser  当前登录用户
     *                 todo 设计缓存算法
     * @return
     */
    @Override
    public List<Comment> list(String keyword, Integer sort, Integer is_elec, Integer relation, User curUser) {
        //先从缓存中获取
        List<Comment> commentList;
        if (redisTemplate.opsForValue().get(RedisConst.UNREAD_COMMENT + curUser.getMid()) != null){
            commentList = (List<Comment>) redisTemplate.opsForValue().get(RedisConst.UNREAD_COMMENT + curUser.getMid());
        } else {
            commentList = getUnreadComment(curUser);
        }

        List<Comment> resultList = new ArrayList<>();
        //根据查询条件进行过滤和排序
        for (Comment comment:commentList){
            //设置前往该评论的url
            Long replyId = comment.getParent() == ROOT ? comment.getId() : comment.getParent();
            comment.setUrl("https://www.bilibili.com/video/"+comment.getBvid()+"#reply"+replyId);

            //根据是否充电来过滤
            if (is_elec != null){
                if (comment.getIs_elec() != is_elec){
                    continue;
                }
            }
            //根据关系来过滤
            if (relation != null){
                if (comment.getRelation() != relation){
                    continue;
                }
            }
            //根据keyword过滤
            if (!StringUtils.isEmpty(keyword))
                if (!comment.getMessage().contains(keyword))
                    continue;

            resultList.add(comment);
        }

        if (resultList.size() > 0){
            if (sort == SORT_LIKE_DESC){
                resultList = resultList.stream()
                        .sorted(Comparator.comparingLong(Comment::getLike).reversed())
                        .collect(Collectors.toList());
            }else if (sort == SORT_REPLY_DESC){
                resultList = resultList.stream()
                        .sorted(Comparator.comparingLong(Comment::getCount).reversed())
                        .collect(Collectors.toList());
            }
        }

        return resultList;
    }



    /**
     * 获取当前用户的未读评论
     * @param curUser
     * @return
     */
    private List<Comment> getUnreadComment(User curUser) {
        //爬取数据  最多爬取1000条
        int ps = 100;
        int pn = 1;
        Long replyNum = 0L;
        Long count = 0L;
        List<Comment> commentList = new ArrayList<>();

        try {
            //获取未读消息数
            String response = catchApi.getJsonFromApiByCook(Const.Url.USER_UNREAD, curUser.getCookie());
            replyNum = JSONObject.parseObject(response).getJSONObject("data").getLong("reply");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //未读消息读取完就结束 todo 自己的发的消息怎么过滤呢
        while (replyNum >= count && replyNum != 0){
            try {
                String response = catchApi.getJsonFromApiByCook(Const.Url.REPLY + "&ps=100&pn=" + pn, curUser.getCookie());
                List<Comment> cl = JSONObject.parseObject(response).getJSONArray("data").toJavaList(Comment.class);
                /*new TypeReference<List<Comment>>(){}*/
                Long remain = replyNum - count;
                //根据未读消息的数量判断
                if (cl.size() > remain){
                    commentList.addAll(cl.subList(0, remain.intValue()));
                } else {
                    commentList.addAll(cl);
                }
                count += ps;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return commentList;
    }

}
