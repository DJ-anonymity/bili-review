package com.zfg.learn.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
/*
@Component
@Aspect
public class RedisAspect {
    @Autowired
    private RedisTemplate redisTemplate;
    @Pointcut("execution(* com.zfg.learn.serviceImpl.ShortReviewServiceImpl.search*(..))")
    private void myPoint(){}

    @Around("myPoint()")
    public void Before(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("start");
        Object proceed = proceedingJoinPoint.proceed();
        System.out.println("end");
    }


}*/
