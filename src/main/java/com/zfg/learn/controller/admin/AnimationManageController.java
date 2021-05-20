package com.zfg.learn.controller.admin;

import com.zfg.learn.common.ServerResponse;
import com.zfg.learn.model.po.Animation;
import com.zfg.learn.service.AnimationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequestMapping("/admin")
@RestController
public class AnimationManageController {
    @Autowired
    private AnimationService animationService;

    @PostMapping("/animation/pull")
    public ServerResponse pullAnimationFromApi(Integer media_id) throws IOException {
        if (media_id == null){
            return ServerResponse.createByErrorCodeMessage(2,"参数为空");
        }

        animationService.pullNewAnimation(media_id);
        return ServerResponse.createBySuccess();
    }
}
