package com.zfg.learn.until;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
*io操作工具类
* */
public class IOUntil {


    public static List<String> readTxt(String path){
        List<String> contentList = new ArrayList<>();

        String str=new String();
        int count = 0 ;
        try {
            BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
            /*String regex = "[\\u4E00-\\u9FA5]+";//只要中文
            Pattern pattern = Pattern.compile(regex);*/
            while((str=br.readLine())!=null) {
                if (!str.contains("C")){
                    System.out.println(str);
                    count++;
                }
                /*Matcher matcher =pattern.matcher(str);
                if (matcher.find()){
                    System.out.println(matcher.group());
                    contentList.add(matcher.group());
                }*/
            }
            System.out.println("总共有"+count+"家公司评分为C以上");
            br.close();
            return contentList;
        }
        catch(IOException ioe) {
            System.out.println("错误"+ioe);
            return null;
        }
    }

    public static void save(String path, List<String> contentList){

        try
        {
            BufferedWriter bw=new BufferedWriter(new FileWriter(path));
            for (String content: contentList){
                System.out.println(content);
                bw.write(content);
                bw.newLine();
            }
            bw.flush();
            bw.close();
        }
        catch(IOException ioe)
        {
            System.out.println("错误"+ioe);
        }
    }

    public static void main(String[] args) {
        List<String> stringList= readTxt("C:\\Users\\zhong\\Desktop\\新建文本文档.txt");
    }
}
