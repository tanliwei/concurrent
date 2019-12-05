package cn.tanlw.lock.atomic;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerTest {
    public static final int SIZE = 2000;
    private static AtomicInteger myAtomicInteger = new AtomicInteger();
    private static Integer myInteger = new Integer("0");
    public static void main(String[] args) throws InterruptedException {
        testAtomicInteger();
        testInteger();
        /**
         * OUTPUT:
         * myAtomicInteger:2000
         * myInteger:1994
         */
    }

    private static void testInteger() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(SIZE);
        for (int i = 0; i < SIZE; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    countDownLatch.countDown();
                    myInteger++;
                }
            }).start();
        }
        countDownLatch.await();
        System.out.println("myInteger:"+myInteger);
    }

    private static void testAtomicInteger() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(SIZE);
        for (int i = 0; i < SIZE; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    countDownLatch.countDown();
                    myAtomicInteger.addAndGet(1);
                }
            }).start();
        }
        countDownLatch.await();
        System.out.println("myAtomicInteger:"+myAtomicInteger);
    }
}
