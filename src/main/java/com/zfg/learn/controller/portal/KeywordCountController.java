package com.zfg.learn.controller.portal;

import com.zfg.learn.common.ServerResponse;
import com.zfg.learn.service.KeywordCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/portal/trending/search")
public class KeywordCountController {
    @Autowired
    KeywordCountService keywordCountService;

    @GetMapping("/mid/list")
    public ServerResponse listMidRank(){
        return ServerResponse.createBySuccess(keywordCountService.listMid());
    }

    @GetMapping("/keyword/list")
    public ServerResponse listKeywordRank(){
        return ServerResponse.createBySuccess(keywordCountService.listKeyword());
    }

    @GetMapping("/demo")
    public ServerResponse demo(){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", "zhong");
        hashMap.put("age","20");
        return ServerResponse.createBySuccess(hashMap);
    }

}
