package cn.tanlw.ch8;

import java.util.function.Function;

/**
 * Created by tanlw on 2017-2-16.
 */
public class Lambda1 {
    public static void main(String[] args) {
        final int num = 2;
        Function<Integer,Integer> stringConverter = (frm) -> frm * num;
        System.out.println(stringConverter.apply(3));
    }
}
