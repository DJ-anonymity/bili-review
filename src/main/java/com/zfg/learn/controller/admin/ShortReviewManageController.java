package com.zfg.learn.controller.admin;

import com.zfg.learn.common.Const;
import com.zfg.learn.common.ServerResponse;
import com.zfg.learn.service.ShortReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

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
    public ServerResponse pullAllReviewByMedia_id(Integer media_id) {
        //判断media_id是否为空
        if (media_id == null){
            return ServerResponse.createByErrorCodeMessage(2,"参数");
        }

        ServerResponse serverResponse;
        //执行短评的爬取
        try{
            //爬取前先把设置该功能为执行状态
            redisTemplate.opsForValue().set("pullAllShortReview", Const.IS_RUNNING);
            serverResponse = shortReviewService.pullAllShortReviewFromBiliApi(media_id);
        }catch (Exception e){
            e.printStackTrace();
            serverResponse = ServerResponse.createByErrorMessage("爬取过程中出现问题");
        }finally {
            //无论爬取是否成功，最终设置该功能为空闲状态
            redisTemplate.opsForValue().set("pullAllShortReview", Const.IS_FREE);
        }
        return serverResponse;
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

    //获取还没有获取完的media_id
    @GetMapping("/mid/unfinished")
    public ServerResponse getUnfinishedMedia_id(){
       Integer media_id = (Integer) redisTemplate.opsForValue().get("unfinishedMedia_id");
       if (media_id != null){
           return ServerResponse.createBySuccess(media_id);
       } else {
           return ServerResponse.createByError();
       }
    }
}
