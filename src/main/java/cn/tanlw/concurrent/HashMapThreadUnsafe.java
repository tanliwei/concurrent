package cn.tanlw.concurrent;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * How to prove that HashMap in java is not thread-safe
 * https://stackoverflow.com/questions/18542037/how-to-prove-that-hashmap-in-java-is-not-thread-safe
 * 
 * Ideologically
 * A hash map is based on an array, where each item represents a bucket. As more keys are added, the buckets grow and at a certain threshold the array is recreated with a bigger size, its buckets rearranged so that they are spread more evenly (performance considerations).
 *
 * Technically
 * It means that sometimes HashMap#put() will internally call HashMap#resize() to make the underlying array bigger.
 *
 * HashMap#resize() assigns the table field a new empty array with a bigger capacity and populates it with the old items. While this population happens, the underlying array doesn't contain all of the old items and calling HashMap#get() with an existing key may return null.
 */
public class HashMapThreadUnsafe {
    public static int i = 0;
    public static void main(String[] args){
        final Map<Integer, String> map = new HashMap<>();

        final Integer targetKey = 0b1111_1111_1111_1111; // 65 535
        final String targetValue = "v";
        map.put(targetKey, targetValue);

        new Thread(() -> {
            IntStream.range(0, targetKey).forEach(key -> map.put(key, "someValue"));
        }).start();


        while (true) {
            System.out.println("i:"+i++);
            if (!targetValue.equals(map.get(targetKey))) {
                throw new RuntimeException("HashMap is not thread safe.");
            }
        }
    }
}
