package com.zfg.learn.service.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.zfg.learn.common.Const;
import com.zfg.learn.dao.SubscriptionMapper;
import com.zfg.learn.model.bo.QQTask;
import com.zfg.learn.model.po.Dynamic;
import com.zfg.learn.model.po.DynamicStat;
import com.zfg.learn.service.DynamicService;
import com.zfg.learn.until.CatchApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

/**
 * 动态业务层
 */
@Service
public class DynamicServiceImpl implements DynamicService {
    @Autowired
    SubscriptionMapper subMapper;

    @Override
    public void pushDynamic(Dynamic dynamic) {
        List<Long> qqList = subMapper.selectFollowerByFid(dynamic.getAuthorId());
        if (CollectionUtils.isEmpty(qqList)){
            return;
        }

        QQTask task = toQQTask(dynamic);
        task.setRecList(qqList);
        CatchApi catchApi = new CatchApi();

        //设置发送两次 todo 连续失败两次后  就把保存日志存在数据库中
        String result = "";
        Integer ReSendTimes = 2;
        try {
            catchApi.request("http://127.0.0.1:8081/qqbot/push", JSONObject.toJSONString(task));
            //推送失败的日志
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private QQTask toQQTask(Dynamic dynamic){
        QQTask task = new QQTask();
        String img = null;
        String txt = "";
        //设置id 用来去重
        task.setId(dynamic.getId());
        //设置内容标题
        txt+= dynamic.getAuthorName()+"\n";

        //设置动态内容
        if (!StringUtils.isEmpty(dynamic.getContent())){
            txt+= dynamic.getContent()+"\n";
        }
        //设置动态的图片
        if (dynamic.getType() == Const.Dynamic.NORMAL != StringUtils.isEmpty(dynamic.getImg())){
            img = dynamic.getImg();
        }

        //如果不是普通动态 设置内容项 如：转发 直播 投稿 专栏等 进行特殊处理
        if (dynamic.getType() != Const.Dynamic.NORMAL && dynamic.getType() != Const.Dynamic.NORMAL_NO_IMG){
            DynamicStat stat = dynamic.getStat();

            if (!StringUtils.isEmpty(stat.getTitle())){
                txt+= stat.getTitle()+"\n";
            }
            if (!StringUtils.isEmpty(stat.getDescription())){
                txt+= stat.getDescription()+"\n";
            }
            if (!StringUtils.isEmpty(stat.getImg())){
                img = stat.getImg();
            }
        }

        txt+= dynamic.getUrl();

        task.setTxt(txt);
        if (img != null)
            task.setImg(img);
        return task;
    }
}
