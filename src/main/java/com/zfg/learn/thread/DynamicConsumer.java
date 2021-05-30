package com.zfg.learn.thread;

import com.zfg.learn.config.BeanContext;
import com.zfg.learn.model.po.Dynamic;
import com.zfg.learn.service.DynamicService;
import com.zfg.learn.until.DynamicBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 消费动态的线程
 */
public class DynamicConsumer extends Thread{
    private static DynamicConsumer dynamicConsumer;
    private ThreadPoolExecutor executor
            = new ThreadPoolExecutor(1, 20, 1, TimeUnit.MINUTES,
                    new SynchronousQueue<Runnable>(), new ThreadPoolExecutor.CallerRunsPolicy());
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

            //使用线程池 并发消费提高速度
            executor.submit(() -> DynamicService.pushDynamic(dynamic));
        }
    }

}
