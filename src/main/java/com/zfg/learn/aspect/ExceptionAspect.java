package com.zfg.learn.aspect;

import com.zfg.learn.LearnApplication;
import com.zfg.learn.common.ServerResponse;
import com.zfg.learn.exception.ServiceException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * 在控制层
 * 捕捉业务层抛出的异常
 * todo 考虑出现的问题
 */
@Component
@Aspect
public class ExceptionAspect {
    Logger logger = LoggerFactory.getLogger(LearnApplication.class);


    /*切入所有的controller*/
    @Pointcut("execution(* com.zfg.learn.controller..*.*(..))")
    private void controllerPoint(){}


    /*控制层异常处理*/
    @Around(value = "controllerPoint()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result;

        try{
            result   = proceedingJoinPoint.proceed();
        } catch (ServiceException serviceException){
            result  = ServerResponse.createByErrorMessage(serviceException.getMessage());
        }

        return result;
    }


}
