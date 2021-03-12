package com.zfg.learn.controller.portal;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zfg.learn.common.Const;
import com.zfg.learn.common.ResponseCode;
import com.zfg.learn.common.ServerResponse;
import com.zfg.learn.model.bili.UserInfoBili;
import com.zfg.learn.model.para.UserPara;
import com.zfg.learn.model.po.User;
import com.zfg.learn.service.UserService;
import com.zfg.learn.until.CatchApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 用户相关控制层
 * @author bootzhong
 */
@Api(tags = {"user"})
@RequestMapping("/portal/user")
@RestController
public class UserController {
    @Autowired
    UserService userService;
    CatchApi catchApi = new CatchApi();

    //获取校验码
    @PostMapping("/checkNum/send")
    public ServerResponse sendCheckNum(@RequestParam("email") String email, HttpSession session){
        if (email == null){
            return ServerResponse.createByErrorMessage("邮箱不能为空");
        }
        if (!userService.checkEmail(email)){
            return ServerResponse.createByErrorMessage("该邮箱已存在账号");
        }

        try {
            userService.sendCheckNum(email);
            return ServerResponse.createBySuccess();
        } catch (MessagingException e) {
            e.printStackTrace();
            return ServerResponse.createByErrorCodeMessage(ResponseCode.SERVERERROR.getCode(), "发送邮件过程runtimeexception");
        }
    }

    //注册
    @PostMapping("/register")
    public ServerResponse register(@RequestBody @Validated(User.Register.class) UserPara user){
        boolean result = userService.checkName(user.getUsername());
        Boolean result2 = userService.checkEmail(user.getEmail());

        if (result && result2){
            return userService.register(user);
        } else {
            return ServerResponse.createByErrorMessage("用户名称或邮箱已存在");
        }
    }

    //检查用户名是否重复
    @GetMapping("/name/check")
    public ServerResponse checkName(@RequestParam("username") String name, HttpSession session){
        if (name == null){
            return ServerResponse.createByErrorMessage("名字不能为空");
        }

        if (userService.checkName(name)){
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorMessage("用户名已存在");
        }
    }

    //登录
    @PostMapping("/login")
    public ServerResponse login(@RequestBody User user, HttpSession session){
        if (user.getEmail() == null || user.getPassword() == null){
            return ServerResponse.createByErrorMessage("请填写必填信息");
        }

        ServerResponse<User> serverResponse = userService.login(user);

        if (serverResponse.getStatus() == ResponseCode.SUCCESS.getCode()){
            session.setAttribute(Const.CURRENT_USER, serverResponse.getData());
        }
        return serverResponse;
    }

    //绑定B站账号
    @PostMapping("/bili/bind")
    public ServerResponse bindBiliAccount(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);

        //todo 考虑线程安全问题  是否会出现验证的账号跟session里面存的不一致的问题
        if (user != null && user.getMid() != null && user.getCookie() != null){
            boolean result = userService.bindBiliCount(user);

            if (result){
                return ServerResponse.createBySuccess();
            } else {
                return ServerResponse.createByErrorMessage("Fail 未知原因");
            }
        } else {
            return ServerResponse.createByErrorMessage("请先通过插件获取登录权限");
        }

    }

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
    public Object setCookie(@RequestBody String cookie, HttpSession session) throws IOException {
        JSONArray jsonArray = JSONObject.parseObject(cookie).getJSONArray("cookie");
        if (jsonArray == null || jsonArray.size() == 0){
            return ServerResponse.createByError();
        }
        JSONObject jSONObject = jsonArray.getJSONObject(0);

        //获取cookie
        String cookieName = jSONObject.getString("name");
        String cookieValue = jSONObject.getString("value");

        //连接起来 只在这里拼接一次
        String loginCookie = cookieName+"="+cookieValue;

        //验证cookie能不能用
        ServerResponse<UserInfoBili> serverResponse = userService.checkBiliCookie(loginCookie);
        if (serverResponse.getStatus() == ResponseCode.SUCCESS.getCode()){
            //存进session
            session.setAttribute(Const.COOKIE, loginCookie);
        }

        return serverResponse;
    }
}
