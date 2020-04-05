package com.zfg.learn.until;

import com.zfg.learn.common.ServerResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/*
* 获取api的数据
*
* */
@SpringBootTest
public class CatchApi {

    //获取输入的api的数据
    @Test
    public String getJsonFromApi(String originalUrl) throws IOException {

        String result = "";
        URL url = new URL(originalUrl);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // 设置连接主机服务器超时时间：15000毫秒
        connection.setConnectTimeout(15000);
        // 设置读取主机服务器返回数据超时时间：60000毫秒
        connection.setReadTimeout(60000);
        //请求方式为get
        connection.setRequestMethod("GET");
        /*connection.setRequestProperty("Content-Type", "application/json");*/
        // 通过连接对象获取一个输入流，向远程读取
        if (connection.getResponseCode() == 200) {
            System.out.println("link ok");
            InputStream is = connection.getInputStream();
            // 对输入流对象进行包装:charset根据工作项目组的要求来设置
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            //使用字符缓冲流，
            StringBuffer sbf = new StringBuffer();
            String temp = null;
            // 循环遍历一行一行读取数据
            while ((temp = br.readLine()) != null) {
                sbf.append(temp);
                sbf.append("\r\n");
            }

            result = sbf.toString();
        } else {
            System.out.println("link fault");
        }

        return result;
    }
}
