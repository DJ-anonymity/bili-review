package com.zfg.learn.aspect;

import com.zfg.learn.until.JoinPointOperate;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/*
* 使用redis+aop来记录搜索热词（mid）
* 并且使用异步的方式保证搜索速度
* */
@Component
@Aspect
public class CountMidAspect {

    @Autowired
    private RedisTemplate redisTemplate;

    /*mid搜索切入点*/
    @Pointcut("execution(* com.zfg.learn.serviceImpl.*ReviewServiceImpl.searchReviewByMid(..))")
    private void midPoint(){}

    /*关键词搜索*/
    @AfterReturning(value = "midPoint()", returning = "result")
    public void AfterReturning(JoinPoint joinPoint, Object result) throws Throwable {
        ZSetOperations zSetOP = redisTemplate.opsForZSet();
        Integer mid = (Integer) JoinPointOperate.getFields(joinPoint).get("mid");

        //如果该key不存在，则存进zset中，并设置初始值为1
        if (zSetOP.score("count:mid", mid) == null){
            System.out.println("first");
            //第一次存key的时候，设置过期时间
            zSetOP.add("count:mid", mid,1d);//返回false也会覆盖掉旧的
            redisTemplate.expire("count:mid", 24, TimeUnit.HOURS);
        } else {
            System.out.println(zSetOP.score("count:mid", mid));
            //如果该key存在，则对该key统计进行+1
            zSetOP.incrementScore("count:mid", mid, 1d);
            System.out.println(zSetOP.score("count:mid", mid));
        }
    }

}
