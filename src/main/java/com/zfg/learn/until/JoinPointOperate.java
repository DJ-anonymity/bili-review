package com.zfg.learn.until;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.HashMap;

/*
* aop通过joinpoint获取方法的参数
*
* */
public class JoinPointOperate {

    public static HashMap<String, Object> getFields(JoinPoint joinPoint){
        String[] argsName = ((MethodSignature)joinPoint.getSignature()).getParameterNames();//参数名
        Object[] args = joinPoint.getArgs();//参数的值
        HashMap<String, Object> argsList = new HashMap<>();
        for (int i = 0;i < argsName.length;i++){
            argsList.put(argsName[i], args[i]);
        }
        return argsList;
    }
}
