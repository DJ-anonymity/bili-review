package com.zfg.learn.controller.portal;

import com.zfg.learn.common.Const;
import com.zfg.learn.common.ServerResponse;
import com.zfg.learn.model.para.SubPara;
import com.zfg.learn.model.po.Subscription;
import com.zfg.learn.model.po.User;
import com.zfg.learn.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * 订阅控制层
 */
@RestController
@RequestMapping("/")
public class SubscriptionController {
    @Autowired
    SubscriptionService subscriptionService;

    /**
     * 订阅
     * @param subPara
     * @param session
     * @return
     */
    @PostMapping("/subscribe")
    public ServerResponse subscribe(@RequestBody @Validated SubPara subPara, HttpSession session){
        Subscription subscription = new Subscription();
        User user = (User) session.getAttribute(Const.CURRENT_USER);

        subscription.setUid(user.getUid());
        subscription.setFid(subPara.getFid());
        subscription.setType(subPara.getType());
        subscription.setCtime(System.currentTimeMillis());

        subscriptionService.sub(subscription);
        return ServerResponse.createBySuccess();
    }
}
