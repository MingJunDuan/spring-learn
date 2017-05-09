package com.mjduan.project.util;

/**
 * Created by Duan on 2017/2/8.
 */
public final class Util {
    private Util(){}

    public static boolean blank(String ...args) {
        for (String arg : args) {
            if (null == arg || arg.trim().length() == 0) {
                return true;
            }
        }
        return false;
    }

}
