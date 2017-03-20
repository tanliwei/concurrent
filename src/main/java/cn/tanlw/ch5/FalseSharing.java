package cn.tanlw.ch5;

/**
 * Created by tanlw on 2017-1-16.
 */
public class FalseSharing implements Runnable {
    public static final int NUM_THREADS = 2;
    public static final long ITERATIONS = 500L * 1000L * 1000L;
    private int arrayindex;
    private static VolatileLong[] longs = new VolatileLong[NUM_THREADS];

    static {
        for (int i = 0; i < longs.length; i++) {
            longs[i] = new VolatileLong();
        }
    }

    public FalseSharing(final int arrayIndex) {
        this.arrayindex = arrayIndex;
    }

    public static void main(String[] args) throws InterruptedException {
        final long start = System.currentTimeMillis();
        runTest();
        System.out.println("Duration = " + (System.currentTimeMillis() - start));
    }

    private static void runTest() throws InterruptedException {
        Thread[] threads = new Thread[NUM_THREADS];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new FalseSharing(i));
        }
        for (Thread t : threads) {
            t.start();
            ;
        }
        for (Thread t : threads) {
            t.join();
        }
    }

    public void run() {
        long i = ITERATIONS + 1;
        while ( 0 != --i){
            longs[arrayindex].value = i;
        }
    }

    public final static class VolatileLong {
        public volatile long value = 0L;
//        public long p1, p2, p3, p4,p5,p6;// comment out
    }
}
