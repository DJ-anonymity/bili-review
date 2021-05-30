package com.zfg.learn.controller.portal;

import com.zfg.learn.common.Const;
import com.zfg.learn.common.ServerResponse;
import com.zfg.learn.model.dto.AnimationDto;
import com.zfg.learn.model.po.Animation;
import com.zfg.learn.model.po.Subscription;
import com.zfg.learn.model.po.User;
import com.zfg.learn.service.AnimationService;
import com.zfg.learn.service.SubscriptionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@RequestMapping("/portal")
@RestController
public class AnimationController {
    @Autowired
    private AnimationService animationService;
    @Autowired
    private SubscriptionService subService;

    @GetMapping("/animation/{media_id}")
    public ServerResponse findAnimationById(@PathVariable Integer media_id, HttpSession session) throws IOException {
        if (media_id == null){
            return ServerResponse.createByErrorCodeMessage(2,"参数为空");
        }

        Animation animation = animationService.findAnimationByMedia_id(media_id);
        if (animation == null){
            animation = animationService.pullNewAnimation(media_id);
            if (animation == null){
                return ServerResponse.createByErrorMessage("该影剧不存在");
            }
        }

        AnimationDto animationDto = new AnimationDto();
        BeanUtils.copyProperties(animation, animationDto);

        //显示订阅状态
        Object currentUser = session.getAttribute(Const.CURRENT_USER);
        if (currentUser != null){
            User user = (User) currentUser;
            Subscription relation = subService.getRelation(user.getUid(), media_id, Const.Sub.TYPE_MEDIA);
            if (relation != null && relation.getStatus() == Const.Sub.FOLLOW){
                animationDto.setIs_follow(true);
            } else {
                animationDto.setIs_follow(false);
            }
        }

        return ServerResponse.createBySuccess(animationDto);
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
