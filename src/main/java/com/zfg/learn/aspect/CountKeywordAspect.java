package com.zfg.learn.aspect;

import com.zfg.learn.common.RedisConst;
import com.zfg.learn.until.JoinPointOperate;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

/*
* 使用redis+aop来记录搜索热词
* 并且使用异步的方式保证搜索速度
* */
@Component
@Aspect
public class CountKeywordAspect {

    @Autowired
    private RedisTemplate redisTemplate;

    /*keyword搜索切入点*/
    @Pointcut("execution(* com.zfg.learn.serviceImpl.*ReviewServiceImpl.searchReviewByKeyword(..))")
    private void keywordPoint(){}

    /*关键词搜索*/
    @AfterReturning(value = "keywordPoint()", returning = "result")
    public void AfterReturning(JoinPoint joinPoint, Object result) throws Throwable {
        ZSetOperations zSetOP = redisTemplate.opsForZSet();
        String keyword = (String) JoinPointOperate.getFields(joinPoint).get("keyword");
        //加上同步锁 防止并发访问时出现数据幻读
        synchronized (this){
            //如果该key不存在，则存进zset中，并设置初始值为1
            if (zSetOP.score("count:keyword", keyword) == null){
                zSetOP.add("count:keyword", keyword, 1d);
                //第一次存key的时候，设置过期时间
                redisTemplate.expire("count:keyword", 24, TimeUnit.HOURS);
            } else {
                //如果该key存在，则对该key统计进行+1
                zSetOP.incrementScore("count:keyword", keyword, 1d);
            }
        }
    }

}
