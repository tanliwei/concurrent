package cn.tanlw.ch8;

import java.util.Arrays;

/**
 * Created by tanlw on 2017-2-15.
 */
public class FunctionalExpression {
    public static void main(String[] args) {
        int arr[] = {1, 3, 5, 6, 2};
        Arrays.stream(arr).map((x) -> x = x + 1).forEach(System.out::println);
        System.out.println();
        Arrays.stream(arr).forEach(System.out::println);
        System.out.println();
        Arrays.stream(arr).map((x) -> ((x & 1) == 1 ? x+1 : x )).forEach(System.out::println);
//        System.out.println((3&1)==1);
    }
}
