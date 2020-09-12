package com.zfg.learn.controller.portal;

import com.zfg.learn.common.ServerResponse;
import com.zfg.learn.service.UserService;
import com.zfg.learn.until.CatchApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RequestMapping("/portal/user")
@RestController
public class UserController {
    @Autowired
    UserService userService;
    CatchApi catchApi = new CatchApi();

    //获取当前用户是否已经登录b站账号
    @GetMapping("/loginStatus")
    public String getLoginStatus(HttpServletRequest request) throws IOException {
        Cookie[] cookies = request.getCookies();
        String cookie = null;
        for (Cookie cookie1:cookies){
            cookie+= cookie1.getName()+"="+cookie1.getValue()+"; ";
        }
        System.out.println(cookie);
        String url = "https://api.bilibili.com/x/web-interface/nav";

        return catchApi.getJsonFromApiByCook(url, cookie);
    }

    //获取当前用户的好友列表
    @GetMapping("/followers")
    public Object getFollowers(Integer mid, HttpServletRequest request) throws IOException {
        if (mid == null){
            return ServerResponse.createByError();
        }
        String refer = "https://space.bilibili.com/"+mid+"/fans/fans";
        String api = "https://api.bilibili.com/x/relation/followers?pn=1&ps=20&order=desc&jsonp=jsonp&callback=__jp5&vmid="+mid;
        return catchApi.skipReferer(api, refer);
    }

    //获取用户发表的评论数量
    @GetMapping("/{mid}/review/quantity")
    public ServerResponse findAnimationReviewQuantity(@PathVariable Integer mid) throws IOException {
        if (mid == null){
            return ServerResponse.createByErrorCodeMessage(2,"参数为空");
        }
        return ServerResponse.createBySuccess(userService.getReviewQuantity(mid));
    }
}
