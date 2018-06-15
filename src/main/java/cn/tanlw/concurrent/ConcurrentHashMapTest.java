package cn.tanlw.concurrent;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 为什么Hashtable ConcurrentHashMap不支持key或者value为null?
 * 并发场景下允许get(key)返回null的时候，存在歧义：
 * 是value为null？ 还是map对象中没有对应的key-value键值对？
 * 要判断的话，可以用contains方法去判断，HashMap是非并发的可以用contains判断；
 * 如果并发场景的ConcurrentHashMap的话，在get之后，contains之前，map对象内的值可能已经发生改变了，
 * 两个操作（先get，后 contains）不是同一时刻内操作同一对象，会带来并发安全问题。
 * ConcurrentHashMap 通过禁用key或者value，就可以通过get(key)的返回值为null，判断出，当下对象内没有key的键值对。
 * @author tanliwei
 * @create 2018/6/14
 */
public class ConcurrentHashMapTest {
    public static void main(String[] args) {

        ConcurrentHashMap cHashMap = new ConcurrentHashMap();
        //if (key == null || value == null) throw new NullPointerException();
//        cHashMap.put("a",null);
        cHashMap.put("a","b");
        System.out.println(cHashMap.get("aa"));
    }
}
