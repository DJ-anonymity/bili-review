package com.zfg.learn.demo;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class SeleniumDemo {
    public static void main(String[] args) {

        System.setProperty("webdriver.chrome.driver", "E:/zfg/chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("http://www.itest.info");

        String title = driver.getTitle();
        System.out.printf(title);

        driver.close();
    }
}
