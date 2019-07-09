package com.auxing.znhy.util;

import java.util.UUID;

/**
 * Created by GY on 2016/4/1.
 */
public class UUIDUtil {

    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        StringBuilder stringBuilder = new StringBuilder();
        // 去掉"-"符号
        stringBuilder.append(str.substring(0, 8)).append(str.substring(9, 13)).append(str.substring(14, 18)).append(str.substring(19, 23)).append(str.substring(24));
        return stringBuilder.toString();
    }

    //获得指定数量的UUID
    public static String getUUID(int number) {
//        if (number < 1) {
//            return null;
//        }
//        String[] ss = new String[number];
//        for (int i = 0; i < number; i++) {
//            ss[i] = getUUID();
//        }
        return getUUID().substring(0, 9);
    }

}
