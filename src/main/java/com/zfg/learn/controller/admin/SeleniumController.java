package com.zfg.learn.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.zfg.learn.common.ServerResponse;
import com.zfg.learn.until.CatchApi;
import com.zfg.learn.until.SeleniumBiliUntil;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@RestController
public class SeleniumController {
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
    public ServerResponse send(){
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
        Set<Cookie> cookies = webDriver.manage().getCookies();

        CatchApi catchApi = new CatchApi();
        try {
            catchApi.request("http://127.0.0.1:8081/bili/selenium/cookie", JSONObject.toJSONString(cookies));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
