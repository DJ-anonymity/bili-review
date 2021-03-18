package com.zfg.learn.demo;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class SeleniumDemo {
    static Set<Cookie> cookies;

    public static void main(String[] args) throws InterruptedException {

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

        //是否登录成功
        WebDriver webDriver2 = new ChromeDriver(chromeOptions);
        webDriver2.get(url);
        cookies.stream().forEach( e ->
                System.out.println(e.getValue() +": "+e.getName()));


        //webDriver2.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        //webDriver2.navigate().refresh();
        /*driver.get("https://space.bilibili.com/20736117");
        Cookie login = new Cookie("SESSDATA","5df72f33%2C1631161287%2Cf448a%2A31");
        driver.manage().addCookie(login);
        driver.navigate().refresh();

        //WebElement element = driver.findElement(new By.ByClassName("h-f-btn h-follow"));
        //element.click();

        //driver.close();*/
    }
}
