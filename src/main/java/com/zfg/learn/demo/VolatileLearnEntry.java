package com.zfg.learn.demo;

/**
 * 共享资源
 */
public class VolatileLearnEntry {
    private volatile int i = 100;


    public synchronized void add(){
        i = i + 1;
    }


   /* public void add(){
        long start = System.currentTimeMillis();
        System.out.println("读取到缓存中-----: "+i+"现在时间是：");

        if (i != 100){

        }
        while (i == 100){
            i =  i;
        }

        long spendTime = System.currentTimeMillis() - start;
        System.out.println("读取内存成功 耗时"+spendTime);
    }

    public void addFirst(){
        System.out.println("读取到缓存中first-----:"+i);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        i = i+1;
        System.out.println("存到主存中first-------:"+i);

    }*/

}
