package cn.tanlw.ch5.cn.tanlw.ch5.oddEvenSort;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by tanlw on 2017-1-18.
 */
public class OddEvenSortTaskMain {
    static int[] arr = {1, 2, 3, 4, 5, 2, 3, 8, 9, 10, 2, 10, 2, 10, 2, 10, 2, 10, 2, 10, 2, 10, 2,  2, 10, 2, 10, 2};
    static ExecutorService pool = Executors.newCachedThreadPool();

    public static class OddEvenSortTask implements Runnable {
        int i;
        CountDownLatch latch;

        public OddEvenSortTask(int i, CountDownLatch latch) {
            this.i = i;
            this.latch = latch;
        }

        public void run() {
            if (arr[i] > arr[i + 1]) {
                int temp = arr[i];
                arr[i] = arr[i + 1];
                arr[i + 1] = temp;
                setExchFlag(1);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
//            System.out.print(" [BEFORE]i:" + i + " count:" + latch.getCount());
            latch.countDown();
//            System.out.println(" [AFTER]i:" + i + " count:" + latch.getCount());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final long start = System.currentTimeMillis();
//        pOddEvenSort(arr);
        bubbleSort(arr);
        System.out.println("Duration = " + (System.currentTimeMillis() - start));
        soutArr(arr);
    }

    public static void bubbleSort(int arr[]) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] > arr[j]) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }

    }

    private static void pOddEvenSort(int[] arr) throws InterruptedException {
        int start = 0;
        while (getExchFlag() == 1 || start == 1) {
            setExchFlag(0);
            System.out.println("start:" + start);
            //偶数 的 数组 长度, 当 start 为 1 时, 只有 len/ 2- 1 个 线程
            CountDownLatch latch = new CountDownLatch(arr.length / 2 - (arr.length % 2 == 0 ? start : 0));
            for (int i = start; i < arr.length - 1; i += 2) {
                pool.submit(new OddEvenSortTask(i, latch));
            }
            //等待所有线程结束
            latch.await();
//            System.out.print("arr:");
//            soutArr(arr);
            if (start == 0) {
                start = 1;
            } else {
                start = 0;
            }
        }
    }

    private static void soutArr(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    static int exchFlag = 1;

    static synchronized void setExchFlag(int v) {
        exchFlag = v;
    }

    static synchronized int getExchFlag() {
        return exchFlag;
    }
}
