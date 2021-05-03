package com.zfg.learn.thread;

import com.zfg.learn.config.BeanContext;
import com.zfg.learn.model.po.ShortReview;
import com.zfg.learn.service.ShortReviewService;

import java.io.IOException;

public class SpiderThread implements Runnable {
    ShortReviewService shortReviewService = BeanContext.getBean(ShortReviewService.class);
    private final int media_id;

    public  SpiderThread(int media_id){
        this.media_id = media_id;
    }

    @Override
    public void run() {
        try {
            shortReviewService.pullAllShortReviewFromBiliApi(media_id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
