package com.zfg.learn.controller.portal;

import com.zfg.learn.common.ServerResponse;
import com.zfg.learn.service.AnimationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequestMapping("/portal")
@RestController
public class AnimationController {
    @Autowired
    private AnimationService animationService;

    @GetMapping("/animation/{media_id}")
    public ServerResponse findAnimationById(@PathVariable Integer media_id) {
        if (media_id == null){
            return ServerResponse.createByErrorCodeMessage(2,"参数为空");
        }
        return animationService.findAnimationByMedia_id(media_id);
    }

    @GetMapping("/animation/{media_id}/review/quantity")
    public ServerResponse findAnimationReviewQuantity(@PathVariable Integer media_id) throws IOException {
        if (media_id == null){
            return ServerResponse.createByErrorCodeMessage(2,"参数为空");
        }
        return ServerResponse.createBySuccess(animationService.getReviewQuantity(media_id));
    }

    @GetMapping("/animation/list")
    public ServerResponse list(Integer persistenceMark,
                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "8") Integer pageSize){
        return animationService.list(persistenceMark, pageNum, pageSize);
    }


}
