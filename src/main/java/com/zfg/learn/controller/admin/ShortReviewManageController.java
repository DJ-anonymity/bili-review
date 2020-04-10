package com.zfg.learn.controller.admin;

import com.zfg.learn.common.Const;
import com.zfg.learn.common.ServerResponse;
import com.zfg.learn.service.ShortReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequestMapping("/admin/review/short")
@RestController
public class ShortReviewManageController {
    @Autowired
    private ShortReviewService shortReviewService;
    @Autowired
    private RedisTemplate redisTemplate;

    //拉取某番的全部评论
    @PostMapping("/pullAll")
    public ServerResponse pullAllReviewByMedia_id(Integer media_id) throws IOException {
        //判断media_id是否为空
        if (media_id == null){
            return ServerResponse.createByErrorCodeMessage(2,"参数");
        }
        //先判断拉取功能是否空闲
        Integer status = (Integer) redisTemplate.opsForValue().get("pullAllShortReview");
        if (status == Const.IS_FREE) {
            return shortReviewService.pullAllShortReviewFromBiliApi(media_id);
        } else {
            return ServerResponse.createByErrorMessage("该功能正在使用中");
        }
    }

    //拉取某番的最新评价
    @PostMapping("/pullNew")
    public ServerResponse pullNewReviewByMedia_id(Integer media_id) throws IOException {
        //判断media_id是否为空
        if (media_id == null){
            return ServerResponse.createByErrorCodeMessage(2,"参数");
        }
        Integer status = (Integer) redisTemplate.opsForValue().get("pullNewShortReview");
        if (status == Const.IS_FREE) {
            return shortReviewService.pullNewShortReviewFromBiliApi(media_id);
        } else {
            return ServerResponse.createByErrorMessage("该功能正在使用中");
        }
    }
}
