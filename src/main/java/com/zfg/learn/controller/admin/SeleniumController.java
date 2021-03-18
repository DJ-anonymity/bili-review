package com.zfg.learn.controller.admin;

import com.zfg.learn.common.ServerResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
public class SeleniumController {


    @PostMapping("/selenium/cookie")
    public ServerResponse receiveSeleniumCookie(String cookie, String roleCheck){
        //验证为空以及权限是否通过

        //通过后初始化selenium

        return ServerResponse.createBySuccess();
    }
}
