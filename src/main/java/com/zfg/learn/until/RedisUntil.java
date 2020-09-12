package com.zfg.learn.until;

import com.zfg.learn.common.RedisConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUntil {
    @Autowired
    private RedisTemplate redisTemplate;

    public boolean delMKeys(){
     return true;
    }
}
