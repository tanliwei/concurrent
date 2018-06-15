package com.wlm.test.concurrent;

import java.util.concurrent.CountDownLatch;

import javax.annotation.PostConstruct;


/**
 * @author wengliemiao
 */
public class WaitThreadTest {

    @PostConstruct
    public void init() {
        try {
            System.out.println("******************** WaitThreadTest started at:" + System.currentTimeMillis() + " ********************");
            CountDownLatch stopLatch = new CountDownLatch(3);

            for (int i = 0; i < 3; i ++) {
                new Thread(new Run(stopLatch)).start();
            }

            // 等待所有线程操作完成
            stopLatch.await();

            System.out.println("******************** WaitThreadTest ended at:" + System.currentTimeMillis() + " ********************");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class Run implements Runnable {

        private CountDownLatch stopLatch;

        public Run(CountDownLatch countDownLatch) {
            this.stopLatch = countDownLatch;
        }

        @Override
        public void run() {
            // 线程操作
            System.out.println(Thread.currentThread().getName() + " Handler...");
            try {                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 线程结束操作
            stopLatch.countDown();
        }
    }

    public static void main(String[] args) {
        WaitThreadTest test = new WaitThreadTest();
        test.init();
    }
}