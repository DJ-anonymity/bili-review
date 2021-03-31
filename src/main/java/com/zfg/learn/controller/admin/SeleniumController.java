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
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 *
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

        return ServerResponse.createBySuccess();
    }

    @GetMapping("/selenium/cookie/send")
    public ServerResponse send(@RequestParam(value = "reload", defaultValue = "0") Integer reload){

        Set<Cookie> cookies;
        if (reload != 1 && redisTemplate.opsForValue().get("cookie:selenium") != null){
            String s  = (String) redisTemplate.opsForValue().get("cookie:selenium");
            cookies = new HashSet<>(JSONObject.parseArray(s, Cookie.class));
        } else {
            System.setProperty("webdriver.chrome.driver", "E:/zfg/chromedriver.exe");

            ChromeOptions chromeOptions = new ChromeOptions();
            WebDriver webDriver = new ChromeDriver(chromeOptions);
            String url = "https://www.bilibili.com/";
            webDriver.get(url); //
            // 与浏览器同步非常重要，必须等待浏览器加载完毕
            webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            //中间完成登录操作
            Scanner scanner = new Scanner(System.in);
            scanner.next();
            //获取cookie,再通过baocun函数进行序列化操作
            cookies = webDriver.manage().getCookies();

            //存进缓存中
            String s = JSONObject.toJSONString(cookies);
            redisTemplate.opsForValue().set("cookie:selenium", s);
        }

        //把token传送给监听器
        for (Cookie cookie:cookies){
            if (cookie.getName().equals("SESSDATA")){
                String token = cookie.getName()+"="+cookie.getValue();
                DynamicListener.getInstance().setCookie(token);
                break;
            }
        }

        //把cookie发送给selenium
        CatchApi catchApi = new CatchApi();
        try {
            catchApi.request("http://127.0.0.1:8082/bili/selenium/cookie", JSONObject.toJSONString(cookies));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
