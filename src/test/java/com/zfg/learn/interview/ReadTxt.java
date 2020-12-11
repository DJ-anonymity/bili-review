package com.zfg.learn.interview;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadTxt {


    public static void main(String[] args) {

        String rex = "\".*?\"";
        System.out.println(readTxt("C:\\Users\\Administrator\\Desktop\\icon.txt", rex));

    }
    public static List readTxt(String path, String regex){
        // 使用ArrayList来存储每行读取到的字符串
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            FileReader fr = new FileReader(path);
            BufferedReader bf = new BufferedReader(fr);
            String str;
            // 按行读取字符串
            while ((str = bf.readLine()) != null) {
                if (regex != null){
                    Pattern pattern = Pattern.compile(regex);
                    Matcher m = pattern.matcher(str);
                    if (m.find()){
                        arrayList.add(m.group().substring(1, m.group().length()-1));
                    }
                } else {
                    arrayList.add(str);
                }
            }
            bf.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



        // 返回数组
        return arrayList;
    }
}
