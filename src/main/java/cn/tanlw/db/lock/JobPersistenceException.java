package cn.tanlw.db.lock;

import java.sql.SQLException; /**
 * @author liwei.tan
 * @Date 2018/9/29 9:52
 */
public class JobPersistenceException extends Exception {
    public JobPersistenceException(String s, SQLException e) {
        super(s,e);
    }
}
