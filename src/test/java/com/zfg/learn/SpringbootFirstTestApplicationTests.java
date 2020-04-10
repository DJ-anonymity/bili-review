package com.zfg.learn;

import com.zfg.learn.common.Const;
import com.zfg.learn.dao.*;
import com.zfg.learn.pojo.*;
import com.zfg.learn.service.AnimationService;
import com.zfg.learn.service.LongReviewService;
import com.zfg.learn.service.ShortReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class SpringbootFirstTestApplicationTests {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private LongReviewMapper longReviewMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StatMapper statMapper;
    @Autowired
    private LongReviewService longReviewService;
    @Autowired
    private AnimationMapper animationMapper;
    @Autowired
    private ShortReviewMapper shortReviewMapper;
    @Autowired
    private ShortReviewService shortReviewService;
    @Autowired
    private AnimationService animationService;

    @Test
    public void demo() throws IOException {


        animationService.pullNewAnimation(28224080);
        //System.out.println(animationMapper.selectAnimationByMedia_id(28224080));
        //System.out.println(shortReviewMapper.selectReviewByMedia_id(28224080, "mtime DESC"));
       //System.out.println(longReviewMapper.selectReviewByMedia_id(28224080,"likes DESC"));
        //System.out.println(shortReviewService.pullNewShortReviewFromBiliApi(28224080));
        //redisTemplate.opsForValue().set("shortReviewCursor",78417529834612L);
        /*User user = new User();
        user.setMid(452622756);
        user.setUname("zhong");
        System.out.println(redisTemplate.opsForValue().get("shortReviewCursor"));
        System.out.println("zfgnb");*/
        /*List<Integer> integers = shortReviewMapper.selectAllReview_id();
        for (Integer i:integers){
            System.out.println(shortReviewService.deleteShortReviewByReview_id(i));
        }*/
        //System.out.println(shortReviewService.deleteShortReviewByReview_id(11656380).getStatus());
        //System.out.println(shortReviewService.pullAllShortReviewFromBiliApi(28224080).getMsg());
        //System.out.println(longReviewService.pullAllLongReviewFromBiliApi(28224080).getMsg());
    }

    @Test
    public void demo1() throws IOException {
        List<ShortReview> shortReviews = shortReviewMapper.selectReviewByMtime(1586355729L);
        System.out.println(shortReviews.size());
        int i =0;


    }

    @Test
    public void learnRex(){

        String content = "<a href=\"https://m.baidu.com/?from=1022670z\" target=\"blank\">"+
                         "<a href=\"https://m.baidu.com/?from=1022670z\" target=\"blank\">";
        String regex = "href=\".*?\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()){
            System.out.println(matcher.group());
        }
    }

    @Test
    public void demo2(){
        /*redisTemplate.delete("shortCommentaryList");
        redisTemplate.delete("shortCommentCursor");*/
        List<LongReview> longReviewList = (List<LongReview>) redisTemplate.opsForValue().get("shortCommentaryList");
        int i =0;
        for (LongReview longReview : longReviewList){
            if (longReview.getScore() <= 2) {
                System.out.println(i);
                i++;
            }
        }
    }
    @Test
    public void demo3(){

        List<LongReview> longReviewList = (List<LongReview>) redisTemplate.opsForValue().get("longCommentaryList");
        for (LongReview longReview : longReviewList){
            if (longReview.getScore() <= 2) {
                System.out.println(longReview);
            }
        }
    }
}
