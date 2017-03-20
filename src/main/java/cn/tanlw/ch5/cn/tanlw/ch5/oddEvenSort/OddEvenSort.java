package cn.tanlw.ch5.cn.tanlw.ch5.oddEvenSort;

/**
 * Created by tanlw on 2017-1-18.
 */
public class OddEvenSort {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 2, 3, 8};
        System.out.println("arr:" + arr);
        oddEvenSort(arr);
        System.out.println("arr:" + arr);
    }

    private static void oddEvenSort(int[] arr) {
        int exchFlag = 1, start = 0;
        while (exchFlag == 1 || start == 1) {
            exchFlag = 0;
            for (int i = start; i < arr.length - 1; i += 2) {
                if (arr[i] > arr[i + 1]) {
                    int temp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = temp;
                    exchFlag = 1;
                }
            }
            if (start == 0) {
                start = 1;
            } else {
                start = 0;
            }
        }
    }
}
