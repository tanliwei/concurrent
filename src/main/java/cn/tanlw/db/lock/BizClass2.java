package cn.tanlw.db.lock;

import cn.tanlw.db.lock.config.DataSourceConfig;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;

class BizClass2 implements Runnable {
    private final String lockName;
    DataSourceConfig dataSourceConfig;
    CountDownLatch countDownLatch;
    DBSemaphore dbSemaphore;
    public BizClass2(DBSemaphore dbSemaphore,DataSourceConfig dataSourceConfig, String lockName, CountDownLatch countDownLatch){
        this.dbSemaphore = dbSemaphore;
        this.dataSourceConfig = dataSourceConfig;
        this.lockName = lockName;
        this.countDownLatch = countDownLatch;
    }
    @Override
    public void run() {
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

