package com.zfg.learn.demo;

import java.util.concurrent.CountDownLatch;

/**
 * 学习volatile的使用
 * 以及关于线程读取数据是否真的在主存 缓存
 */
public class VolatileLearn {
    public int num = 0;

    public void addNum() {
        num = num + 60;
    }

    public static void main(String[] args) {
        VolatileLearn volatileVisibleDemo = new VolatileLearn();

        // t1线程对num就行更改操作
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t come in");

            try {
                // 模拟num更改操作耗时3m，并保证其他线程读取了num变量
                Thread.sleep(3000);

            } catch (InterruptedException e) {
                e.printStackTrace();

            }

            System.out.println(Thread.currentThread().getName() + "\t" + "num值已经被更改为：" + volatileVisibleDemo.num);

        }, "t1").start();

        while (volatileVisibleDemo.num == 0) {
            // main线程一直等待，直到num不等于0
        }

        System.out.println(Thread.currentThread().getName() + "\t mission is over");

}

}
