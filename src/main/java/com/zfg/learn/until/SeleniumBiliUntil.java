package com.zfg.learn.until;

import com.zfg.learn.common.Path;
import com.zfg.learn.exception.SeleniumException;
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
    private String token;

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
        System.setProperty("webdriver.chrome.driver", Path.DRIVER);
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setHeadless(true);
        chromeOptions.addArguments("--no-sandbox");//root 权限
        chromeOptions.addArguments("--disable-gpu");//谷歌文档提到需要加上这个属性来规避bug
        chromeOptions.addArguments("--disable-dev-shm-usage");//

        webDriver = new ChromeDriver(chromeOptions);
        webDriver.get(INI_URL);
        //与浏览器同步非常重要，必须等待浏览器加载完毕
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        //添加cookie
        for (Cookie cookie:cookies){
            webDriver.manage().addCookie(cookie);

            if (cookie.getName().equals("SESSDATA"))
                token = cookie.getName()+"="+cookie.getValue();
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
    public void subscribe(Integer fid, Integer type) throws SeleniumException {
        if (!initialized){
            throw new SeleniumException("订阅失败 请先初始化sn");
        }

        String url;
        WebElement element;

        if (type.equals(Type.MEDIA)){
            url = "https://www.bilibili.com/bangumi/media/md"+fid;
        } else if (type.equals(Type.UP)){
            url = "https://space.bilibili.com/"+fid;
        } else {
            throw new SeleniumException("没有这种类型");
        }

        webDriver.get(url);
        // 与浏览器同步非常重要，必须等待浏览器加载完毕
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        if (type.equals(Type.MEDIA)){
            //TIP 选择器不能出现多类名
            element = webDriver.findElement(new By.ByClassName("btn-follow"));
        }else{
            element = webDriver.findElement(new By.ByCssSelector(".h-f-btn.h-follow"));
        }
        element.click();
    }

    public boolean isInitialized(){
        return initialized;
    }

    public interface Type{
        Integer MEDIA = 0;
        Integer UP = 1;
    }
}
