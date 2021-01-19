package com.zfg.learn.controller.portal;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zfg.learn.common.Const;
import com.zfg.learn.common.ServerResponse;
import com.zfg.learn.service.UserService;
import com.zfg.learn.until.CatchApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 用户相关控制层
 * @author bootzhong
 */
@Api(tags = {"Catalog"})
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

    @ApiOperation(value = "获取Chrome插件传送过来的cookie")
    @PostMapping("/cookie")
    public Object getFollowers(@RequestBody String cookie, HttpSession session) throws IOException {
        JSONArray jsonArray = JSONObject.parseObject(cookie).getJSONArray("cookie");
        JSONObject jSONObject = jsonArray.getJSONObject(0);

        //获取cookie
        String loginCookie = jSONObject.getString("value");
        //验证cookie能不能用
        if (userService.checkBiliCookie(loginCookie)){
            //存进session
            session.setAttribute(Const.COOKIE, loginCookie);
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorMessage("该cookie已过期");
        }

    }
}
