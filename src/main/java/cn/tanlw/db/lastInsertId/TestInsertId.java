package cn.tanlw.db.lastInsertId;

import cn.tanlw.db.lastInsertId.target.LastInsertIdUtil;
import cn.tanlw.db.lastInsertId.config.DataSourceConfig;
import cn.tanlw.db.lastInsertId.target.LastInsertIdUtil2;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.CountDownLatch;

/**
 * 100个连接并发,测试未关闭resultSet、statement的情况(LastInsertIdUtil , 正确 LastInsertIdUtil2)
 * 最大连接数设置为10
 * Exception：
 *  java.sql.SQLTransientConnectionException: HikariPool-1 - Connection is not available, request timed out after 30002ms.
 * @create 2018/8/27
 */

public class TestInsertId {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext configApplicationContext =
                new AnnotationConfigApplicationContext(TestApplication.class);
        DataSourceConfig dataSourceConfig = (DataSourceConfig)configApplicationContext.getBean("dataSourceConfig");
        CountDownLatch latch = new CountDownLatch(100);
        for (int i = 0; i < 100; i++) {
            new Thread(new MyTestRunnable(latch, dataSourceConfig)).start();
            latch.countDown();
        }
        System.out.println("Start");

    }

    private static class MyTestRunnable implements Runnable {
        private CountDownLatch latch;
        DataSourceConfig dataSourceConfig;
        public MyTestRunnable(CountDownLatch latch, DataSourceConfig dataSourceConfig) {
            this.latch = latch;
            this.dataSourceConfig = dataSourceConfig;
        }

        @Override
        public void run() {
            try {
                latch.await();
                System.out.println("result:"+ LastInsertIdUtil.getPrimaryId(dataSourceConfig.dataSource(), "a")+"Thread name:"+Thread.currentThread().getName());

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
