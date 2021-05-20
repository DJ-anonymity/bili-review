package com.zfg.learn.component.listener;

import com.zfg.learn.common.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class MyListener implements ServletContextListener {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        redisTemplate.opsForValue().set("pullAllLongReview", Const.IS_FREE);
        redisTemplate.opsForValue().set("pullAllShortReview",Const.IS_FREE);
        redisTemplate.opsForValue().set("pullNewShortReview",Const.IS_FREE);
    }
}
