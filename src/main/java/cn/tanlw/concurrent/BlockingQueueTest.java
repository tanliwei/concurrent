package cn.tanlw.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
@author liwei.tan@baozun.com
@Date 2018/10/25 14:33
 */
public class BlockingQueueTest {

    public static void main(String[] args) {

        BlockingQueue q = new LinkedBlockingQueue();
        Producer p = new Producer(q);
        Consumer c1 = new Consumer(q);
        Consumer c2 = new Consumer(q);
        new Thread(p).start();
        new Thread(c1).start();
        new Thread(c2).start();
    }
}

/**
生产者
 */
class Producer implements Runnable {
    private final BlockingQueue queue;

    Producer(BlockingQueue q) {
        queue = q;
    }

    public void run() {
        try {
            while (true) {
                queue.put(produce());
                Thread.sleep(300);
            }
        } catch (InterruptedException ex) {
            //... handle ...
        }
    }

    Object produce() {
        //...
        return new Object();
    }
}

class Consumer implements Runnable {
  private final BlockingQueue queue;
  Consumer(BlockingQueue q) { queue = q; }
  public void run() {
    try {
      while (true) {
          consume(queue.take());
      }
    } catch (InterruptedException ex) {
        //... handle ...
    }
  }
  void consume(Object x) {
      System.out.println(Thread.currentThread().getName()+":"+x);
  }
}
 
