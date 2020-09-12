package com.zfg.learn.serviceImpl;

import com.zfg.learn.bo.KeywordCountBo;
import com.zfg.learn.service.KeywordCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class KeywordCountServiceImpl implements KeywordCountService {
    @Autowired
    RedisTemplate redisTemplate;

    /*例举出前10的*/
    @Override
    public List<KeywordCountBo> listMid() {
        /*Set<Long>  midList = redisTemplate.opsForZSet().reverseRange("count:mid",0,7);*/
        List<KeywordCountBo> rankList = new ArrayList();
        Set<ZSetOperations.TypedTuple<Object>> tuples = redisTemplate.opsForZSet().reverseRangeWithScores("count:mid",0,7);
        Iterator<ZSetOperations.TypedTuple<Object>> iterator = tuples.iterator();
        while (iterator.hasNext())
        {
            ZSetOperations.TypedTuple<Object> typedTuple = iterator.next();
            rankList.add(new KeywordCountBo((Integer) typedTuple.getValue(), typedTuple.getScore()));
        }
        return rankList;
    }

    @Override
    public List<KeywordCountBo> listKeyword() {
        List<KeywordCountBo> rankList = new ArrayList();
        Set<ZSetOperations.TypedTuple<Object>> tuples = redisTemplate.opsForZSet().reverseRangeWithScores("count:keyword",0,7);
        Iterator<ZSetOperations.TypedTuple<Object>> iterator = tuples.iterator();
        while (iterator.hasNext())
        {
            ZSetOperations.TypedTuple<Object> typedTuple = iterator.next();
            rankList.add(new KeywordCountBo((String) typedTuple.getValue(), typedTuple.getScore()));
        }
        return rankList;
    }
}
