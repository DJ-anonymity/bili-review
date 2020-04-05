package com.zfg.learn.controller;

import com.zfg.learn.interesting.CatchBili;
import com.zfg.learn.pojo.LongReview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CatchAPIController {
    CatchBili catchBili = new CatchBili();
    @Autowired
    private RedisTemplate redisTemplate;


    @GetMapping("/commentary/long/list")
    public List<LongReview> getData() throws IOException {
        List<LongReview> longReviewList = (List<LongReview>) redisTemplate.opsForValue().get("longCommentaryList");
        List<LongReview> negativeLongReviewList = new ArrayList<>();
        for (LongReview longReview : longReviewList){
            if (longReview.getScore() <= 2) {
                negativeLongReviewList.add(longReview);
            }
        }
        return negativeLongReviewList;
    }

    @GetMapping("/commentary/short/list")
    public List<LongReview> getShortCommentary() throws IOException {
        List<LongReview> longReviewList = (List<LongReview>) redisTemplate.opsForValue().get("shortCommentaryList");
        List<LongReview> negativeLongReviewList = new ArrayList<>();
        for (LongReview longReview : longReviewList){
            if (longReview.getScore() <= 2) {
                negativeLongReviewList.add(longReview);
            }
        }
        return negativeLongReviewList;
    }
}
