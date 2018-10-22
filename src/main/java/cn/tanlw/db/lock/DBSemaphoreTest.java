package cn.tanlw.db.lock;

import cn.tanlw.db.lock.config.DataSourceConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;

/**
 *

 * @author liwei.tan
 * @Date 2018/9/29 9:48
 */
public class DBSemaphoreTest {

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
        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread(new BizClass(dataSourceConfig, "DBLock", countDownLatch));
            thread.start();
            countDownLatch.countDown();
        }

    }
}

class BizClass implements Runnable {
    private final String lockName;
    DataSourceConfig dataSourceConfig;
    CountDownLatch countDownLatch;
    public BizClass(DataSourceConfig dataSourceConfig, String lockName, CountDownLatch countDownLatch){
        this.dataSourceConfig = dataSourceConfig;
        this.lockName = lockName;
        this.countDownLatch = countDownLatch;
    }
    @Override
    public void run() {
        DBSemaphore dbSemaphore = new DBSemaphore();
        try {
            Connection connection = dataSourceConfig.dataSource().getConnection();
            countDownLatch.await();

            dbSemaphore.obtainLock(connection,lockName);
            System.out.println("获取锁成功"+System.currentTimeMillis()+":"+Thread.currentThread().getName());
            Thread.sleep(200);
            JobStoreSupport.commitConnection(connection);//设置连接池 手动commit
            System.out.println("释放锁成功"+System.currentTimeMillis()+":"+Thread.currentThread().getName());
            JobStoreSupport.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            dbSemaphore.releaseLock(lockName);
        }

    }
}

