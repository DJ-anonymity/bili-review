package com.zfg.learn.until;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpiderBaiKe {
    private static CatchApi catchApi = new CatchApi();

    public static List<String> get(List<String> nameList) throws IOException {
        String regex = "总部地点</dt>[\\s\\S]*?<dd class=\"basicInfo-item value\">[\\s\\S]*?</dd>";
        Pattern p = Pattern.compile(regex);
        List<String> finalResultList = new ArrayList<>();

        for (String name:nameList){
            String nameEncode = URLEncoder.encode(name,"utf-8");//对中文进行重新编码，不然链接有中文则会404
            String url = "https://baike.baidu.com/item/"+nameEncode;
            System.out.println(url);
            try{
                String content = catchApi.getJsonFromApi(url);
                Matcher matcher = p.matcher(content);
                if (matcher.find()){
                    String result = matcher.group();
                    System.out.println(result);
                    String regex2 = "[\\u4E00-\\u9FA5]+";//只要中文
                    Pattern pattern2 = Pattern.compile(regex2);
                    Matcher matcher2 = pattern2.matcher(result.substring(4));//去掉前面的中文
                    if (matcher2.find()){
                        String result2 = matcher2.group();
                        System.out.println(result2);
                        String finalResult = name+": "+result2;
                        finalResultList.add(finalResult);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("未找到公司:"+name);
            }
        }
        return finalResultList;
    }

    public static void main(String[] args) throws IOException {
        List<String> demoList = get(IOUntil.readTxt("C:\\Users\\zhong\\Desktop\\新建文本文档.txt"));
        for (String demo:demoList){
            System.out.println(demo);
        }
        IOUntil.save("C:\\Users\\zhong\\Desktop\\work.txt", demoList);

    }

}
