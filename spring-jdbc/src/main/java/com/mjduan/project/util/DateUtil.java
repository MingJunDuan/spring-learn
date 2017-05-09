package com.mjduan.project.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Duan on 2017/2/8.
 */
public final class DateUtil {
    public static final String pattern1 = "yyyy-mm-dd HH-mm";
    public static final String pattern2 = "yyyy-mm-dd HH:mm:ss:SSS";

    private DateUtil() {
    }

    public static String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd HH-mm");
        return simpleDateFormat.format(new Date());
    }

    public static String getCurrentDate(String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(new Date());
    }

}
