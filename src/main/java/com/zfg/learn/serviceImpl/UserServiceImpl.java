package com.zfg.learn.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.zfg.learn.common.Const;
import com.zfg.learn.common.RedisConst;
import com.zfg.learn.common.ResponseCode;
import com.zfg.learn.common.ServerResponse;
import com.zfg.learn.dao.LongReviewMapper;
import com.zfg.learn.dao.ShortReviewMapper;
import com.zfg.learn.dao.UserMapper;
import com.zfg.learn.model.bili.UserInfoBili;
import com.zfg.learn.model.bo.UserReviewBo;
import com.zfg.learn.model.po.User;
import com.zfg.learn.service.UserService;
import com.zfg.learn.until.CatchApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;

/**
 * 用户业务层, 主要处理user和biliUser
 * @Author bootzhong
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    LongReviewMapper longReviewMapper;
    @Autowired
    ShortReviewMapper shortReviewMapper;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    UserMapper userMapper;

    CatchApi catchApi = new CatchApi();

    /**
     * 获取用户发表的评论数量
     * @param mid
     * @return UserReviewBo
     */
    @Override
    public UserReviewBo getReviewQuantity(Integer mid) {
        UserReviewBo userReviewBo = new UserReviewBo();
        userReviewBo.setMid(mid);
        userReviewBo.setShortReviewQuantity(shortReviewMapper.selectReviewQuantityByMid(mid));
        userReviewBo.setLongReviewQuantity(longReviewMapper.selectReviewQuantityByMid(mid));
        return userReviewBo;
    }

    /**
     * 验证当前cookie是否可用
     * @return  Boolean
     */
    @Override
    public ServerResponse<UserInfoBili> checkBiliCookie(String loginCookie) throws IOException {

        //能用则直接存进redis中 并更新数据库

        //设置请求头参数
        HashMap<String, String> hashMap = new HashMap();
        hashMap.put(Const.COOKIE, loginCookie);

        //请求用户信息api
        String apiData = catchApi.getJsonFromApiByHeader(Const.Url.USER_INFO, hashMap);

        Integer code = JSONObject.parseObject(apiData).getInteger("code");
        if (code == ResponseCode.SUCCESS.getCode()){
            UserInfoBili userInfoBili = JSONObject.parseObject(apiData).getObject("data", UserInfoBili.class);
            return  ServerResponse.createBySuccess(userInfoBili);
        } else {
            String msg = (String) JSONObject.parseObject(apiData).get("message");
            return ServerResponse.createByErrorMessage(msg);
        }
    }

    /**
     * 获取当前浏览器的B站登陆Cookie
     * @return
     */
    @Override
    public User getBiliCookie() {
        return null;
    }

    /**
     * 注册
     */
    @Override
    public ServerResponse<User> register(User user) {
        userMapper.insert(user);
        return ServerResponse.createBySuccess();
    }

    /**
     * 登录
     */
    @Override
    public ServerResponse<User> login(User user) {
        User userVail = userMapper.selectUserByAccount(user.getAccount());
        if (userVail == null){
            return ServerResponse.createByErrorMessage("用户不存在");
        }

        if (userVail.getPassword().equals(user.getPassword())){
            return ServerResponse.createBySuccess(user);
        } else {
            return ServerResponse.createByErrorMessage("密码 错误");
        }
    }

    /**
     * 绑定B站账号
     */
    @Override
    public boolean bindBiliCount(User user) {
        Integer status = userMapper.bindBiliCount(user);
        if (status > 0){
            //更新redis信息 暂时redis没有用
            //redisTemplate.opsForValue().set(RedisConst.COOKIE + user.getMid(), user.getCookie());
            return true;
        } else {
            return false;
        }
    }

    /**
     * 验证账号是否重复
     * @param account
     * @return
     */
    @Override
    public boolean checkAccountIsAvailable(String account) {
        User originalUser = userMapper.selectUserByAccount(account);

        if (originalUser != null){
            return false;
        } else {
            return true;
        }
    }
}
