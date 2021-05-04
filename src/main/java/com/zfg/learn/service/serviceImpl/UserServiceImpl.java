package com.zfg.learn.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zfg.learn.common.Const;
import com.zfg.learn.common.ResponseCode;
import com.zfg.learn.common.ServerResponse;
import com.zfg.learn.dao.BiliUserMapper;
import com.zfg.learn.dao.LongReviewMapper;
import com.zfg.learn.dao.ShortReviewMapper;
import com.zfg.learn.dao.UserMapper;
import com.zfg.learn.exception.ServiceException;
import com.zfg.learn.model.bili.UserInfoBili;
import com.zfg.learn.model.bo.UserReviewBo;
import com.zfg.learn.model.para.UserPara;
import com.zfg.learn.model.po.BiliUser;
import com.zfg.learn.model.po.User;
import com.zfg.learn.service.UserService;
import com.zfg.learn.until.CatchApi;
import com.zfg.learn.until.EmailUntil;
import org.apache.kafka.common.errors.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

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
    @Autowired
    BiliUserMapper biliUserMapper;

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
     * 通过cookie
     * 从b站api获取账号信息
     * @return  Boolean
     */
    @Override
    public UserInfoBili getBiliAcountByCookie(String loginCookie) throws IOException {
        UserInfoBili userInfoBili;
        //能用则直接存进redis中 并更新数据库

        //设置请求头参数
        HashMap<String, String> hashMap = new HashMap();
        hashMap.put(Const.COOKIE, loginCookie);

        //请求用户信息api
        String apiData = catchApi.getJsonFromApiByHeader(Const.Url.USER_INFO, hashMap);
        Integer code = JSONObject.parseObject(apiData).getInteger("code");

        if (code == ResponseCode.SUCCESS.getCode()){
            userInfoBili  = JSONObject.parseObject(apiData).getObject("data", UserInfoBili.class);
            return  userInfoBili;
        } else {
            //todo 考虑b站拉黑等情况 如果是拉黑情况直接抛出异常
            String msg = (String) JSONObject.parseObject(apiData).get("message");
            System.out.println(msg);
            return null;
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
    @Transactional
    public boolean register(UserPara user) {
        Integer checkNum = (Integer) redisTemplate.opsForValue().get("checkNum:email:" + user.getEmail());
        if (checkNum != null && checkNum.equals(user.getCheckNum())) {
            userMapper.insert(user);
            redisTemplate.delete("checkNum:email:" + user.getEmail());
            return true;
        }

        return false;
    }

    /**
     * 更新
     */
    @Override
    @Transactional
    public boolean update(UserPara user) {
        //更新之前先判断改qq是不是已经添加bot为好友
        Long qq = user.getQq();
        if (qq != null){
            try {
                String result = catchApi.getJsonFromApi("http://127.0.0.1:8081/qqbot/friend/"+qq, "POST");
                ServerResponse response = JSON.parseObject(result, ServerResponse.class);
                if (response.getStatus() != ResponseCode.SUCCESS.getCode()){
                    throw new ServiceException("请先添加我为好友");
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new ServiceException("qqbot服务访问失败");
            }
        }

        //执行更新操作
        userMapper.updateMultipleByUid(user);
        return true;
    }

    /**
     * 登录
     */
    @Override
    public User login(User user) {
        User userVail = userMapper.selectByEmail(user.getEmail());
        if (userVail == null){
            throw new ServiceException("用户不存在");
        }

        if (userVail.getPassword().equals(user.getPassword())){
            return userVail;
        } else {
            throw new ServiceException("密码错误");
        }
    }

    /**
     * 绑定B站账号
     */
    @Transactional
    @Override
    public boolean bindBiliCount(User user) {
        Integer status = userMapper.bindBiliCount(user);
        //绑定成功后修改用户权限
        if (status > 0){
            userMapper.updateRoleByUid(2, user.getUid());
            return true;
        } else {
            return false;
        }
    }

    /**
     * 验证用户名是否重复
     * @param username
     * @return
     */
    @Override
    public boolean checkName(String username) {
        User originalUser = userMapper.selectByName(username);

        if (originalUser != null){
            return false;
        } else {
            return true;
        }
    }

    /**
     * 发送验证码
     * @param email
     */
    @Override
    public void sendCheckNum(String email) throws MessagingException {
        Integer checkNum;

        //60s只能获取一次
        checkNum  = (Integer) redisTemplate.opsForValue().get("checkNum:email:" + email);
        if (checkNum != null){
            return;
        }

        checkNum = (int) (Math.random()*1000);
        try {
            EmailUntil.emailPost(email, checkNum);
            redisTemplate.opsForValue().set("checkNum:email:"+email, checkNum, 60, TimeUnit.SECONDS);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 验证邮箱是否可用
     * @param email
     * @return
     */
    @Override
    @Transactional
    public Boolean checkEmail(String email) {
        User user = userMapper.selectByEmail(email);
        if (user == null){
            return true;
        }

        return false;
    }

    /**
     * 通过mid
     * @param mid
     * @return
     */
    @Override
    public BiliUser getBiliAccountByMid(Integer mid) {
        BiliUser user = biliUserMapper.selectUserByMid(mid.intValue());

        //数据库没有的时候就去访问B站api
        if (user == null){
            String json;

            try {
                json = catchApi.getJsonFromApi("https://api.bilibili.com/x/space/acc/info?jsonp=jsonp&mid="+mid);
            } catch (IOException e) {
                e.printStackTrace();
                throw new ServiceException("服务器出现问题", 500);
            }

            JSONObject data = JSONObject.parseObject(json).getJSONObject("data");
            if (data != null){
                user = new BiliUser();
                user.setUname(data.getString("name"));
                user.setMid(data.getInteger("mid"));
                user.setAvatar(data.getString("face"));

                //插入数据库
                biliUserMapper.insertUser(user);
            } else {
                user = null;
            }
        }
        return user;
    }


}
