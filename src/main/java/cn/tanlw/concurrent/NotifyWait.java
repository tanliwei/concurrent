package cn.tanlw.concurrent;

import static cn.tanlw.concurrent.NotifyWait.sigLock;

/**
 * @author liwei.tan@baozun.com
 * @Date 2018/9/28 16:48
 */
public class NotifyWait {
    final static Object sigLock = new Object();

    public static void main(String[] args) {

        Thread thread = new Thread(new MyThread());
        //注释掉下面这行， two 和 thr 之间 相差3秒, 否则相差1秒
//        thread.start();
        while(1 < 2){

            System.out.println("one " + System.currentTimeMillis() / 1000);
            synchronized (sigLock){
                try {
                    sigLock.wait(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("two " + System.currentTimeMillis() / 1000);
            synchronized(sigLock){

                try {
                    sigLock.wait(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("thr " + System.currentTimeMillis() / 1000);

        }
    }
}


class MyThread implements Runnable{

    @Override
    public void run() {
        while(1<2){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (sigLock) {
                sigLock.notifyAll();
            }
        }
    }
}