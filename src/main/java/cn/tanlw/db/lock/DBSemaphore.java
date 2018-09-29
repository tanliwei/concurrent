package cn.tanlw.db.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

/**
 * TODO 自带可重入锁功能
 * @author liwei.tan@baozun.com
 * @Date 2018/9/28 20:47
 */

public class DBSemaphore implements Semaphore{

    private final Logger log = LoggerFactory.getLogger(getClass());

    ThreadLocal<HashSet<String>> lockOwners = new ThreadLocal<HashSet<String>>();
    private String expandedInsertSQL = "INSERT INTO TEST(NAME) VALUES ('ClusterScheduler')";
    private String expandedSQL= "SELECT * FROM TEST WHERE NAME = 'ClusterScheduler' FOR UPDATE";

    /**
     * Grants a lock on the identified resource to the calling thread (blocking
     * until it is available).
     *
     * @return true if the lock was obtained.
     */
    public boolean obtainLock(Connection connection, String lockName){
        if (!isLockOwner(lockName)) {

            executeSQL(connection, lockName, expandedSQL, expandedInsertSQL);

            if(log.isDebugEnabled()) {
                log.debug(
                        "Lock '" + lockName + "' given to: "
                                + Thread.currentThread().getName());
            }
            getThreadLocks().add(lockName);
            //getThreadLocksObtainer().put(lockName, new
            // Exception("Obtainer..."));
        } else if(log.isDebugEnabled()) {
            log.debug(
                    "Lock '" + lockName + "' Is already owned by: "
                            + Thread.currentThread().getName());
        }
        return true;
    }

    /**
     * Release the lock on the identified resource if it is held by the calling
     * thread.
     */
    public void releaseLock(String lockName){
        if (isLockOwner(lockName)) {
            if(getLog().isDebugEnabled()) {
                getLog().debug(
                        "Lock '" + lockName + "' returned by: "
                                + Thread.currentThread().getName());
            }
            getThreadLocks().remove(lockName);
            //getThreadLocksObtainer().remove(lockName);
        } else if (getLog().isDebugEnabled()) {
            getLog().warn(
                    "Lock '" + lockName + "' attempt to return by: "
                            + Thread.currentThread().getName()
                            + " -- but not owner!",
                    new Exception("stack-trace of wrongful returner"));
        }
    }

    @Override
    public boolean requiresConnection() {
        return true;
    }


    /**
     * Determine whether the calling thread owns a lock on the identified
     * resource.
     */
    public boolean isLockOwner(String lockName) {
        return getThreadLocks().contains(lockName);
    }


    private HashSet<String> getThreadLocks() {
        HashSet<String> threadLocks = lockOwners.get();
        if (threadLocks == null) {
            threadLocks = new HashSet<String>();
            lockOwners.set(threadLocks);
        }
        return threadLocks;
    }




    protected void executeSQL(Connection conn, final String lockName, final String expandedSQL, final String expandedInsertSQL)throws LockException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        SQLException initCause = null;

        // attempt lock two times (to work-around possible race conditions in inserting the lock row the first time running)
        int count = 0;
        do {
            count++;
            try {
                ps = conn.prepareStatement(expandedSQL);
//                ps.setString(1, lockName);

                if (getLog().isDebugEnabled()) {
                    getLog().debug(
                            "Lock '" + lockName + "' is being obtained: " +
                                    Thread.currentThread().getName());
                }
                rs = ps.executeQuery();
                if (!rs.next()) {
                    getLog().debug(
                            "Inserting new lock row for lock: '" + lockName + "' being obtained by thread: " +
                                    Thread.currentThread().getName());
                    rs.close();
                    rs = null;
                    ps.close();
                    ps = null;
                    ps = conn.prepareStatement(expandedInsertSQL);
//                    ps.setString(1, lockName);

                    int res = ps.executeUpdate();

                    if(res != 1) {
                        if(count < 3) {
                            // pause a bit to give another thread some time to commit the insert of the new lock row
                            try {
                                Thread.sleep(1000L);
                            } catch (InterruptedException ignore) {
                                Thread.currentThread().interrupt();
                            }
                            // try again ...
                            continue;
                        }

                        throw new SQLException();
                    }
                }

                return; // obtained lock, go
            } catch (SQLException sqle) {
                //Exception src =
                // (Exception)getThreadLocksObtainer().get(lockName);
                //if(src != null)
                //  src.printStackTrace();
                //else
                //  System.err.println("--- ***************** NO OBTAINER!");

                if(initCause == null)
                    initCause = sqle;

                if (getLog().isDebugEnabled()) {
                    getLog().debug(
                            "Lock '" + lockName + "' was not obtained by: " +
                                    Thread.currentThread().getName() + (count < 3 ? " - will try again." : ""));
                }

                if(count < 3) {
                    // pause a bit to give another thread some time to commit the insert of the new lock row
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException ignore) {
                        Thread.currentThread().interrupt();
                    }
                    // try again ...
                    continue;
                }

                throw new LockException("Failure obtaining db row lock: ");
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception ignore) {
                    }
                }
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception ignore) {
                    }
                }
            }
        } while(count < 4);

        throw new LockException("Failure obtaining db row lock, reached maximum number of attempts. Initial exception (if any) attached as root cause.");
    }


    protected Logger getLog() {
        return log;
    }
}
