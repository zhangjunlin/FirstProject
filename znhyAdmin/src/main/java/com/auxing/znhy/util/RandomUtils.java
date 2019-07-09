package com.auxing.znhy.util;

import java.util.Random;

public class RandomUtils {
    public static String randomCode(int length) {
        String r = String.valueOf(Math.random());
        return r.substring(r.length() - length);
    }

    public static String randomStringCode(int length) {
        String[] arr = {"E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "3", "4", "5"
                , "6", "7", "8", "9"};
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(arr[r.nextInt(26)]);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(randomCode(6));
    }
}