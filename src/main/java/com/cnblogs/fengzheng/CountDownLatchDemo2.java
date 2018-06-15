package com.cnblogs.fengzheng;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch的 count 增加到10
 * @author tanliwei
 * @create 2018/6/8
 */
public class CountDownLatchDemo2 {
    public static final int SIZE = 10;
    private static CountDownLatch latch = new CountDownLatch(SIZE);

    public static void main(String[] args) throws InterruptedException{
        System.out.println("主线程开始......");
        System.out.println("主线程等待......");
        for (int i = 0; i < SIZE; i++) {
            System.out.println(latch.toString());
            Thread thread = new Thread(new Worker());
            thread.start();
//            latch.await();//放在这里 程序一直等待
        }
        latch.await();//
        System.out.println(latch.toString());
        System.out.println("主线程继续.......");
    }

    public static class Worker implements Runnable {

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName()+":子线程任务正在执行");
            try {
                Thread.sleep(2000);
            }catch (InterruptedException e){
                System.out.println(e.toString());
            }finally {
                latch.countDown();
            }
        }
    }

}
