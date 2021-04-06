package com.zfg.learn.thread;

import com.zfg.learn.config.BeanContext;
import com.zfg.learn.model.po.Dynamic;
import com.zfg.learn.service.DynamicService;
import com.zfg.learn.until.DynamicBlockingQueue;

/**
 * 消费动态的线程
 */
public class DynamicConsumer extends Thread{
    private static DynamicConsumer dynamicConsumer;
    private DynamicBlockingQueue<Dynamic> queue = DynamicBlockingQueue.getInstance();
    DynamicService DynamicService = BeanContext.getBean(DynamicService.class);

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
        while (true) {
            Dynamic dynamic;
            try {
                dynamic = queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
                continue;
            }

            DynamicService.pushDynamic(dynamic);
        }
    }
}
