package com.zfg.learn.until;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 单例模式
 */
public class DynamicBlockingQueue<T> extends LinkedBlockingQueue<T> {
    private static final int DEFAULT_MAX_OPACITY = 100;
    private static DynamicBlockingQueue dynamicBlockingQueue;

    private DynamicBlockingQueue(){
        super(DEFAULT_MAX_OPACITY);
    }

    private DynamicBlockingQueue(int opacity){
        super(opacity);
    }

    /**
     * 单例获取入口
     * @return
     */
    public static DynamicBlockingQueue getInstance(){
        synchronized (DynamicBlockingQueue.class){
            if (dynamicBlockingQueue == null){
                dynamicBlockingQueue = new DynamicBlockingQueue();
            }
        }

        return dynamicBlockingQueue;
    }
}
