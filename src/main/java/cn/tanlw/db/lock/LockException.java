package cn.tanlw.db.lock;

/**
 * @author liwei.tan
 * @Date 2018/9/28 21:00
 */
public class LockException extends RuntimeException{
    public LockException(String s) {
        super(s);
    }
}
