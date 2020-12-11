package com.zfg.learn;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zfg.learn.common.Const;
import com.zfg.learn.common.RedisConst;
import com.zfg.learn.dao.*;
import com.zfg.learn.interview.ReadTxt;
import com.zfg.learn.pojo.*;
import com.zfg.learn.service.AnimationService;
import com.zfg.learn.service.KeywordCountService;
import com.zfg.learn.service.LongReviewService;
import com.zfg.learn.service.ShortReviewService;
import com.zfg.learn.until.CatchApi;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
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
    @Autowired
    private KeywordCountService keywordCountService;
    @Autowired
    private IconMapper iconMapper;

    private  int num = 1;
    //多线程
    private static BlockingQueue blockingQueue = new ArrayBlockingQueue(10);

    @Test
    public void demo() throws IOException {
        shortReviewService.searchReviewByMid(1654611,1,1,8);
        Set<ZSetOperations.TypedTuple<Object>> tuples = redisTemplate.opsForZSet().reverseRangeWithScores("count:mid",0,-1);

        Iterator<ZSetOperations.TypedTuple<Object>> iterator = tuples.iterator();
        while (iterator.hasNext())
        {
            ZSetOperations.TypedTuple<Object> typedTuple = iterator.next();
            System.out.println("value:" + typedTuple.getValue() + "score:" + typedTuple.getScore());
        }
        /*ShortReview shortReview = shortReviewMapper.selectShortReviewByReview_id(814706);
        shortReview.setMedia_id(0415);
        List<ShortReview> shortReviewList = new ArrayList<>();
        //插入500w数据到短评表
        for (int i = 35000001;i <= 40000000;i++){
           shortReview.setReview_id(i);
           ShortReview shortReview1 = new ShortReview();
            BeanUtils.copyProperties(shortReview, shortReview1);
           shortReviewList.add(shortReview1);
            if ((i-35000000)%1000 == 0){
                save(shortReviewList);//每1000个存取一次
                System.out.println("存取次数:"+(i-30000000)%1000);
                shortReviewList = new ArrayList<>();//重新创建一个集合
            }
        }*/
    }
    public void save(List<ShortReview> shortReviewList){
        for (ShortReview shortReview: shortReviewList){
            System.out.println(shortReview.getReview_id());
        }
        try {
            shortReviewMapper.insertShortReviewList(shortReviewList);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    //队列存取值
    public static void main1(String[] args) {

        Runnable saveQueue = new Runnable() {
            @Override
            public void run() {
                int index = 0;
                List<Integer> list = new ArrayList<>();
                for (int i = 0;i <= 10000;i++){
                    list.add(i);
                }
                while (index < list.size()){
                    try {
                        blockingQueue.put(list.get(index));
                        System.out.println("存值成功，存的值为:"+list.get(index));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    index++;
                }
            }
        };
        Runnable takeQueue = new Runnable() {
            @Override
            public void run() {
                int i =0;
                while (i<10000){
                    try {
                        System.out.println("取值成功，取的值为:"+blockingQueue.take());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    i++;
                }
            }
        };

        Thread saveThread = new Thread(saveQueue);
        Thread takeThread = new Thread(takeQueue);
        saveThread.start();
        System.out.println("存线程启动成功");
        takeThread.start();
        System.out.println("取线程启动成功");
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
    public void demo2() {
        System.out.println(redisTemplate.opsForHash().keys(RedisConst.key(8892)));
    }
    @Test
    public void demo3() {
        int medium;
        int[] a = new int[100];
        for (int i = 0;i < 100;i++){
            a[i] = (int) (Math.random()*1000);
        }

        //输出排序前的数组
       for (int i = 0;i < a.length;i++){
           System.out.println(a[i]);
       }
        System.out.println("开始排序————————");
       /*//选择排序法
        int max;
        for (int i = 0; i<a.length-1; i++){
            for (int j = 0;j < a.length-i-1;j++){
                if (a[j] > a[a.length-i-1]){
                    max = a[j];
                    a[j] = a[a.length-i-1];
                    a[a.length-i-1] = max;
                }
            }
        }*/

       /*//冒泡排序法
        int tem;
        for (int i = 0; i<a.length-1; i++) {
            for (int j = 0; j < a.length - i - 1; j++) {
                if (a[j] > a[j+1]) {
                    tem = a[j+1];
                    a[j] = a[j+1];
                    a[j+1] = tem;
                }
            }
        }

        a[0] = 0;

        int searchNum = 0;
        int index = a.length/2;
        int left = 0;
        int right = a.length-1;
        //二分查找法
        while(true){
            if (searchNum == a[index]){
                System.out.println(index);
                break;
            } else if (searchNum > a[index]){
                left = index;
            } else if (searchNum < a[index]){
                right = index;
            }
            index = (left+right)/2;
        }
*/
        //快速排序法
        quick(a);
        //输出排序后的数组
        for (int i = 0;i < a.length;i++){
            System.out.println(a[i]);
        }
    }


    public int getMiddle(int[] list, int low, int high) {
        int tmp = list[low];    //数组的第一个作为中轴
        while (low < high) {
            while (low < high && list[high] >= tmp) {
                high--;
            }
            list[low] = list[high];   //比中轴小的记录移到低端
            while (low < high && list[low] <= tmp) {
                low++;
            }
            list[high] = list[low];   //比中轴大的记录移到高端
        }
        list[low] = tmp;              //中轴记录到尾
        return low;                   //返回中轴的位置
    }

    public void _quickSort(int[] list, int low, int high) {
        if (low < high) {
            int middle = getMiddle(list, low, high);  //将list数组进行一分为二
            _quickSort(list, low, middle - 1);  //对低字表进行递归排序 *递归：这行运行完说明该middle节点左子树遍历完
            _quickSort(list, middle + 1, high);  //对高字表进行递归排序 *递归，这行运行完说明该middle节点左右都遍历完了，返回到右子树没有被遍历的节点
        }                                             //相当于先序遍历
    }

    public void quick(int[] a2) {
        if (a2.length > 0) {    //查看数组是否为空
            _quickSort(a2, 0, a2.length - 1);
        }
    }

    /*死锁*/
    public static void main2(String []args) throws InterruptedException {
        User user = new User();
        User user1 = new User();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                synchronized (user) {
                    System.out.println("user锁正在被线程2占用——————");
                    System.out.println("线程2正在努力获取user1锁");
                    synchronized (user1){
                        System.out.println("线程2获取user1锁成功");
                    }
                    System.out.println("线程2睡眠结束，释放锁user");
                }
            }
        };
        Thread thread1 = new Thread(runnable);
        thread1.start();
        synchronized (user1){
            System.out.println("user1锁正在被主线程使用————————");
            Thread.sleep(2000);
            System.out.println("主线程正在获取user锁-------");
            synchronized (user){
                System.out.println("主线程获取User锁成功");
            }
        }

    }

    public static  void  main(String[] args){
        Scanner scanner = new Scanner(System.in);
        int size = scanner.nextInt();//输入的数字个数
        int[] list = new int[size];//输入的数字集合
        int zeroNum = 0;//输入的0的个数
        int fiveNum = 0;//输入的5的个数

        //键盘输入数据
        for (int i =  0;i < size;i++){
            list[i] = scanner.nextInt();
            if (list[i] == 0){
                zeroNum++;
            } else if (list[i] == 5){
                fiveNum++;
            } else{
                System.out.println("请输入0和5");
                break;
            }
        }

        long startTime=System.currentTimeMillis();//开始计时
        System.out.print("录入的数字为: ");
        for (int i =  0;i < size;i++){
            System.out.print(list[i]+" ");
        }
        System.out.println("");

        //第一位必须是5,倒数第二位必须为5，最后一位必须为0，5xxxx50,550不行，5050和5500也不行,所以n<=4直接排除
        if (size <= 4|| zeroNum < 1 || fiveNum <2){
            System.out.println("没有能被50整除的数");
        }

        long maxNum = 0;//先把最大的数算出来
        for (int k = 1;k <= fiveNum-1;k++){ //-1是因为有一个5要放在倒数第二位
            maxNum = maxNum*10 + 5;
        }
        //最大数如下
        maxNum = (long) (maxNum*Math.pow(10, zeroNum+1)) + 50;//+1是因为前面的5没有算进去
        System.out.println("最大数为："+maxNum);
        long maxNumFormat = maxNum;//当位数减少一位的时候，用该数来求新的最大数，减过90和余数那个求不了

        //计算出最小数,第一位为5，最后两位必须为50，中5xx50，也就是xx中0靠前，5靠后即可
        long minNum =  min(size, zeroNum);
        System.out.println("最小数为："+minNum);

        //余数
        long remain = maxNum % 90;
        System.out.println("剩余量为: "+remain);
        //如果最大数能被整除，则直接返回
        if (remain == 0){
            System.out.println("已经找到最大能被整除的数: "+maxNum);
            return;
        }

        //剩余的数字，比如剩余的是45，则说明这个数需要减去一个45,然后通过不断减去90得到符合90倍数的数
        maxNum = maxNum - remain;
        while (size > 4){ //大于四是因为小于或等于四位数组成的数字没有90的倍数
            while (maxNum >= minNum){
                if (suit(maxNum, fiveNum, zeroNum)){ //suit方法检测该数字是否符合规则
                    long timeSpend = System.currentTimeMillis() - startTime;
                    System.out.println("花费时间为: "+ timeSpend);
                    System.out.println("已经找到最大能被整除的数: "+maxNum);
                    return;
                }
                maxNum = maxNum - 90;
            }
            //size-1
            size = size -1;
            //当减少一位的时候最大数为
            maxNumFormat = max(maxNumFormat);
            maxNum = maxNumFormat;
            //当减少一位的时候最小数为
            minNum = min(size, zeroNum);
            System.out.println("最小数的结果为: "+minNum);
        }

        System.out.println("没有符合的数字");
    }

    public static boolean suit(long max, int fiveNumTrue, int zeroNumTrue){
        String maxString = ""+max;//把当前数字转换成字符串
        int fiveNum = 0;//记录当前数字包含5的个数
        int zeroNum = 0;//记录当前数字包含0的个数

        for (int i = 0;i < maxString.length();i++){
            if (maxString.charAt(i) == '0'){
                zeroNum++;
            }
            if (maxString.charAt(i) == '5'){
                fiveNum++;
            }
            if (maxString.charAt(i) != '5' && maxString.charAt(i) != '0'){//不能出现0和1以外的数字
                return false;
            }
        }

        System.out.println("目前通过了一次筛选的数字为 "+max);
        System.out.println("5的个数不能超过"+fiveNumTrue+",目前数字有"+fiveNum+"个5; " +
                        "0的个数不能超过"+zeroNumTrue+",目前数字有"+zeroNum+"个0");
        //如果5的个数 和0的个数均符合要求，则通过
        if (fiveNum <= fiveNumTrue && zeroNum <= zeroNumTrue){
            return true;
        }
        return false;
    }

    //求最大数
    public static long max(long currentMaxNum){
        String  maxNum = ""+currentMaxNum;
        maxNum = maxNum.substring(0, maxNum.length()-3);
        maxNum = maxNum+"50";
        System.out.println("求最大数的结果为:"+maxNum);
        return Long.parseLong(maxNum);

    }
    //求最小数
    public static long  min(int size, int zeroNum){
        long minSize = size - 3;//减去5xx50确定的三位
        String minNum = "";
        if (zeroNum-1 >= minSize){//减去已经用去的1个0
            for (int i = 1;i <= minSize;i++){
                minNum = minNum+"0";
            }
            return Long.parseLong(5+minNum+50) ;
        } else {
            for (int i = 1;i <= minSize;i++){
                if (i <= zeroNum - 1){
                    minNum = minNum + "0";
                } else {
                    minNum = minNum + "5";
                }
            }
            return Long.parseLong(5+minNum+50);
        }
    }

    @Test
    public void demo500(){
        System.out.println(f(3));

    }
    //跳一个n级的台阶有多少种跳法
    int f(int n){
        //经过分析，f(2)=2也是一个临界条件。
        if(n <= 2){
            return n;
        }
        return f(n-1) + f(n-2);
    }

    @Test
    public void xlsRead(){
        List<String> iconList = ReadTxt.readTxt("C:\\Users\\Administrator\\Desktop\\icon.txt", "\".*?\"");
        List<String> menuNameList = ReadTxt.readTxt("C:\\Users\\Administrator\\Desktop\\菜单名称.txt", null);
        for (int i = 0;i < iconList.size();i++){
            //iconMapper.updateIcon(menuNameList.get(i), iconList.get(i));

            System.out.println(iconMapper.selectByName(menuNameList.get(i)));
        }


        //System.out.println(iconMapper.updateIcon("感知数据录管理","zfgnb"));
    }
}
