package com.zfg.learn.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.zfg.learn.common.ServerResponse;
import com.zfg.learn.thread.DynamicListener;
import com.zfg.learn.until.CatchApi;
import com.zfg.learn.until.SeleniumBiliUntil;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 获取机器人的cookie
 */
@RestController
public class SeleniumController {
    @Autowired
    RedisTemplate redisTemplate;
    private static final String SIGN = "zfgNB";

    @PostMapping("/selenium/cookie")
    public ServerResponse receiveSeleniumCookie(@RequestBody String cookies){
        //验证为空以
        if (StringUtils.isEmpty(cookies)){
            return ServerResponse.createByErrorMessage("cookie不能为空");
        }

        List<Cookie> cookieList = JSONObject.parseArray(cookies, Cookie.class);

        /*//简单的签名验证
        if (!SIGN.equals(sign)){
            return ServerResponse.createByErrorMessage("权限验证 fail");
        }*/

        //通过后初始化selenium
        SeleniumBiliUntil selenium = SeleniumBiliUntil.getInstance();
        selenium.initialized(new HashSet<>(cookieList));

        //把token传送给监听器
        Cookie sessData = cookieList.stream()
                .filter(e -> e.getName().equals("SESSDATA")).findFirst().get();
        String token = sessData.getName()+"="+sessData.getValue();
        DynamicListener.getInstance().setCookie(token);

        return ServerResponse.createBySuccess();
    }

}
