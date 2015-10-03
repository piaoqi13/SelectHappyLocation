package com.fuwei.selecthappylocation.util;

import android.util.Log;

/**
 * Created by collin on 2015-10-02.
 */
public class EasyLogger {
    public static boolean DEBUG_MODE = true;

    public static void e(String tag, String msg) {
        if (DEBUG_MODE) {
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (DEBUG_MODE) {
            Log.e(tag, msg, tr);
        }
    }

    public static void i(String tag, String msg) {
        if (DEBUG_MODE) {
            Log.i(tag, msg);
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (DEBUG_MODE) {
            Log.i(tag, msg, tr);
        }
    }

    public static void d(String tag, String msg) {
        if (DEBUG_MODE) {
            Log.d(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (DEBUG_MODE) {
            Log.d(tag, msg, tr);
        }
    }

    public static void w(String tag, String msg) {
        if (DEBUG_MODE) {
            Log.w(tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (DEBUG_MODE) {
            Log.w(tag, msg, tr);
        }
    }
}
