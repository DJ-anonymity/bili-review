package com.zfg.learn.controller.portal;

import com.github.pagehelper.PageInfo;
import com.zfg.learn.common.Const;
import com.zfg.learn.common.ServerResponse;
import com.zfg.learn.model.dto.SubscriptionDto;
import com.zfg.learn.model.para.SubPara;
import com.zfg.learn.model.po.Subscription;
import com.zfg.learn.model.po.User;
import com.zfg.learn.service.SubscriptionService;
import com.zfg.learn.thread.DynamicConsumer;
import com.zfg.learn.thread.DynamicListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * 订阅控制层
 */
@RestController
@RequestMapping("/sub")
public class SubscriptionController {
    @Autowired
    SubscriptionService subscriptionService;

    /**
     * 订阅/取订
     * @param subPara
     * @param session
     * @return
     * todo 取订完善
     */
    @PostMapping("/modify")
    public ServerResponse modify(@RequestBody @Validated SubPara subPara, HttpSession session){
        Subscription subscription = new Subscription();
        User user = (User) session.getAttribute(Const.CURRENT_USER);

        subscription.setUid(user.getUid());
        subscription.setFid(subPara.getFid());
        subscription.setType(subPara.getType());
        subscription.setStatus(subPara.getStatus());
        subscription.setMtime(System.currentTimeMillis());

        subscriptionService.modify(subscription);
        return ServerResponse.createBySuccess();
    }

    /**
     * 查看订阅的内容
     */
    @GetMapping("/list")
    public ServerResponse list(@RequestParam(value = "type", defaultValue = "0") Integer type,
                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "8") Integer pageSize,
                               HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);

        PageInfo<SubscriptionDto> list = subscriptionService.list(user.getUid(), type, pageNum, pageSize);
        return ServerResponse.createBySuccess(list);
    }

    /**
     * 订阅总数
     * @param session
     * @return
     */
    @GetMapping("/num")
    public ServerResponse getNum(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        Integer num = subscriptionService.getSubNum(user.getUid());
        return ServerResponse.createBySuccess(num);
    }

    /**
     * 推送任务开始
     */
    @GetMapping("/listener/start")
    public ServerResponse listenDynamicStart(){
        DynamicListener dynamicListener = DynamicListener.getInstance();
        dynamicListener.start();

        DynamicConsumer dynamicConsumer = DynamicConsumer.getInstance();
        dynamicConsumer.start();
        return ServerResponse.createBySuccess();
    }
}
