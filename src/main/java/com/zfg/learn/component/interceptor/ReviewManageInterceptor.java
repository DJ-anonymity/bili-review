package com.zfg.learn.component.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.zfg.learn.common.Const;
import com.zfg.learn.common.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


public class ReviewManageInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        redisTemplate.opsForValue().get("pullAllShortReview");
        Integer status = (Integer) redisTemplate.opsForValue().get("pullAllShortReview");
        //爬取短评功能使用中则任何爬取功能都无法使用
        if (status != null && status == Const.IS_RUNNING) {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println(JSONObject.toJSONString(ServerResponse.createByErrorMessage("该功能正在使用中")));
            return false;
        }
        return true;
    }
}
