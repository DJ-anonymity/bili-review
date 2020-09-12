package com.zfg.learn.controller.portal;

import com.zfg.learn.common.ServerResponse;
import com.zfg.learn.service.LongReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/portal/review/long")
@RestController
public class LongReviewController {
    @Autowired
    private LongReviewService longReviewService;

    @GetMapping("/list")
    public ServerResponse list(Integer media_id,
                               @RequestParam(value = "score", required = false) Integer score,
                               @RequestParam(value = "sort", defaultValue = "0") Integer sort,
                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "8") Integer pageSize){
        if (media_id == null){
            return ServerResponse.createByErrorCodeMessage(2,"参数为null");
        }
        return longReviewService.list(media_id, score, sort, pageNum, pageSize);
    }

    //通过keyword搜索某番剧内的评论
    @GetMapping("/animation/{media_id}/search")
    public ServerResponse searchByKeywords(@PathVariable Integer media_id, String keyword,
                                           @RequestParam(value = "score", required = false) Integer score,
                                           @RequestParam(value = "sort", defaultValue = "0") Integer sort,
                                           @RequestParam(value = "pageNum", defaultValue = "1")Integer pageNum,
                                           @RequestParam(value = "pageSize", defaultValue = "8")Integer pageSize){
        if (media_id == null && keyword == null){
            return ServerResponse.createByErrorCodeMessage(2,"参数为null");
        }
        return longReviewService.searchReviewByKeyword(media_id, keyword, score, sort, pageNum ,pageSize);
    }

    //搜索某用户在所有番剧中的评价
    @GetMapping("/search")
    public ServerResponse searchByMid(@RequestParam(value = "mid", required = true) Integer mid,
                                      @RequestParam(value = "sort", defaultValue = "0") Integer sort,
                                      @RequestParam(value = "pageNum", defaultValue = "1")Integer pageNum,
                                      @RequestParam(value = "pageSize", defaultValue = "8")Integer pageSize){

        return longReviewService.searchReviewByMid(mid, sort, pageNum ,pageSize);
    }
}

