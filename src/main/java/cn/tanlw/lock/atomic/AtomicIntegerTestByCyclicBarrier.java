package cn.tanlw.lock.atomic;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerTestByCyclicBarrier {
    public static final int SIZE = 2000;
    private static AtomicInteger myAtomicInteger = new AtomicInteger();
    private static Integer myInteger = new Integer("0");
    public static void main(String[] args) throws InterruptedException {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(SIZE);
        testAtomicInteger(cyclicBarrier);
        testInteger(cyclicBarrier);
        while (cyclicBarrier.getNumberWaiting()>0){
            ;
        }
        Thread.sleep(10);
        System.out.println("myInteger:"+myInteger);
        System.out.println("myAtomicInteger:"+myAtomicInteger);
        /**
         * OUTPUT:
         * myInteger:87
         * myAtomicInteger:2000
         */
    }

    private static void testInteger(CyclicBarrier cyclicBarrier) {
        for (int i = 0; i < SIZE; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        cyclicBarrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                    myInteger++;
                }
            }).start();
        }
    }

    private static void testAtomicInteger(CyclicBarrier cyclicBarrier) {
        for (int i = 0; i < SIZE; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        cyclicBarrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                    myAtomicInteger.addAndGet(1);
                }
            }).start();
        }
    }
}
