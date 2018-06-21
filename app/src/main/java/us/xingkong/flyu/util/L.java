package us.xingkong.flyu.util;

import android.util.Log;

import us.xingkong.flyu.BuildConfig;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/20 17:41
 * @描述:
 * @更新日志:
 */
public class L {
    private static final boolean DEBUG = BuildConfig.isDebug;

    public static void v(String tag, String message) {
        if (DEBUG) {
            Log.v(tag, message);
        }
    }

    public static void d(String tag, String message) {
        if (DEBUG) {
            Log.d(tag, message);
        }
    }

    public static void i(String tag, String message) {
        if (DEBUG) {
            Log.i(tag, message);
        }
    }

    public static void w(String tag, String message) {
        if (DEBUG) {
            Log.w(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if (DEBUG) {
            Log.e(tag, message);
        }
    }
}
