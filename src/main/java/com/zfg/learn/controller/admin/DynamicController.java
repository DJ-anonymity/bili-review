package com.zfg.learn.controller.admin;

import com.zfg.learn.common.ServerResponse;
import com.zfg.learn.thread.DynamicConsumer;
import com.zfg.learn.thread.DynamicListener;
import com.zfg.learn.until.SeleniumBiliUntil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin")
@RestController
public class DynamicController {
    private DynamicListener listener = DynamicListener.getInstance();
    private DynamicConsumer consumer = DynamicConsumer.getInstance();

    /**
     * 启动/继续 监听器
     * @return
     */
    @PostMapping("/dynamic/listener/start")
    public ServerResponse startListener(){
        if (SeleniumBiliUntil.getInstance().isInitialized()){
            //如果是被暂停了就继续
            if (listener.isAlive()){
                if (listener.isStop()){
                    listener.goOn();
                }
            } else {
                listener.start();
                consumer.start();
            }

            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorMessage("请先初始化sn机器人");
        }
    }

    @PostMapping("/dynamic/listener/stop")
    public ServerResponse stopListener(){
        listener.pause();
        return ServerResponse.createBySuccess();
    }
}
