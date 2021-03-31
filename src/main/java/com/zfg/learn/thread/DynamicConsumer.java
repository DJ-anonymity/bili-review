package com.zfg.learn.thread;

import com.zfg.learn.config.BeanContext;
import com.zfg.learn.model.bo.PublicTask;
import com.zfg.learn.service.SubscriptionService;
import com.zfg.learn.service.serviceImpl.SubscriptionServiceImpl;
import com.zfg.learn.until.DynamicBlockingQueue;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 消费动态的线程
 */
public class DynamicConsumer extends Thread{
    private static DynamicConsumer dynamicConsumer;
    private DynamicBlockingQueue<PublicTask> queue = DynamicBlockingQueue.getInstance();
    SubscriptionService subscriptionService = BeanContext.getBean(SubscriptionService.class);

    public static DynamicConsumer getInstance(){
        synchronized (DynamicListener.class){
            if (dynamicConsumer == null){
                dynamicConsumer = new DynamicConsumer();
            }
        }
        return dynamicConsumer;
    }

    @Override
    public void run() {
        while (true){
            PublicTask task;
            try {
                task = queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
                continue;
            }

            subscriptionService.pushDynamic(task);
        }
    }
}
