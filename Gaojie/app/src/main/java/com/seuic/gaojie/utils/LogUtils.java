package com.seuic.gaojie.utils;

import android.util.Log;

/**
 * Created by Dell on 2017/3/17.
 */

public class LogUtils {

    private static boolean isDebug = true;
    private static String TAG = "tag";

    public static void i(String flag,String text) {
        if (isDebug) {
            Log.i(TAG, flag+text);
        }
    }
    public static void d(String flag,String text) {
        if (isDebug) {
            Log.d(TAG, flag+text);
        }
    }
    public static void e(String flag,String text) {
        if (isDebug) {
            Log.e(TAG, flag+text);
        }
    }
}
