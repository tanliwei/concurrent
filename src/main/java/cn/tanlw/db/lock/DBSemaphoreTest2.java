package cn.tanlw.db.lock;

import cn.tanlw.db.lock.config.DataSourceConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;

/**
 *
 * TODO 模拟死锁情况
 * TODO 第一个线程插入记录获得锁和第二个for update互斥锁，可能并发。
 * @author liwei.tan
 * @Date 2018/9/29 9:48
 */
public class DBSemaphoreTest2 {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext configApplicationContext =
                new AnnotationConfigApplicationContext(TestApplication.class);
        DataSourceConfig dataSourceConfig = (DataSourceConfig)configApplicationContext.getBean("dataSourceConfig");

        /**
         * 输出
         获取锁成功1538189106044:Thread-92
         释放锁成功1538189106244:Thread-92
         10:45:06.244 [Thread-92] DEBUG cn.tanlw.db.lock.DBSemaphore - Lock 'DBLock' returned by: Thread-92
         10:45:06.244 [Thread-93] DEBUG cn.tanlw.db.lock.DBSemaphore - Lock 'DBLock' given to: Thread-93
         获取锁成功1538189106244:Thread-93
         释放锁成功1538189106445:Thread-93
         */
        CountDownLatch countDownLatch = new CountDownLatch(100);
        DBSemaphore dbSemaphore = new DBSemaphore();
        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread(new BizClass2(dbSemaphore,dataSourceConfig, "DBLock", countDownLatch));
            thread.start();
            countDownLatch.countDown();
        }

    }
}

