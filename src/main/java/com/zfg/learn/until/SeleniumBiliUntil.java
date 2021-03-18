package com.zfg.learn.until;

import com.zfg.learn.exception.ServiceException;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class SeleniumBiliUntil {
    private WebDriver webDriver;
    private Boolean initialized = false;
    private final String INI_URL = "https://www.bilibili.com/";
    private static SeleniumBiliUntil seleniumBiliUntil;

    private SeleniumBiliUntil(){

    }

    public static SeleniumBiliUntil getInstance(){
        synchronized (SeleniumBiliUntil.class){
            if (seleniumBiliUntil == null){
                seleniumBiliUntil = new SeleniumBiliUntil();
            }
        }

        return seleniumBiliUntil;
    }

    /**
     * 初始化
     * @param cookies
     */
    public void initialized(Set<Cookie> cookies){
        webDriver = new ChromeDriver();
        webDriver.get(INI_URL);
        //与浏览器同步非常重要，必须等待浏览器加载完毕
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        //添加cookie
        for (Cookie cookie:cookies){
            webDriver.manage().addCookie(cookie);
        }

        //刷新
        webDriver.navigate().refresh();
        //设置初始化标志
        initialized = true;
    }

    /*public void initialized(Set<Cookie> cookies, ChromeOptions options){
        webDriver = new ChromeDriver(options);

        webDriver.get(INI_URL);
        //与浏览器同步非常重要，必须等待浏览器加载完毕
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        //添加cookie
        for (Cookie cookie:cookies){
            webDriver.manage().addCookie(cookie);
        }

        //刷新
        webDriver.navigate().refresh();
    }*/

    /**
     * 订阅
     * @param fid
     * @param type
     */
    public void subscribe(String fid, Integer type){
        if (!initialized){
            throw new ServiceException("订阅失败 请先初始化sn");
        }

        String url;
        WebElement element;

        if (type.equals(Type.MEDIA)){
            url = "https://space.bilibili.com/"+fid;
            element = webDriver.findElement(new By.ByClassName("h-f-btn h-follow"));
        } else if (type.equals(Type.UP)){
            url = "https://www.bilibili.com/bangumi/media/md"+fid;
            element = webDriver.findElement(new By.ByClassName("btn-follow"));
        } else {
            throw new ServiceException("没有这种类型");
        }

        webDriver.get(url);
        // 与浏览器同步非常重要，必须等待浏览器加载完毕
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        element.click();
    }

    public interface Type{
        Integer MEDIA = 0;
        Integer UP = 1;
    }
}
