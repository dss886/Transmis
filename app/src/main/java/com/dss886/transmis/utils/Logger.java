package com.dss886.transmis.utils;

import android.util.Log;
import com.dss886.transmis.BuildConfig;

/**
 * Created by dss886 on 2017/6/29.
 */

public class Logger {

    // TODO: 2021/02/11 @duansishu add Tag argument
    public static void d(String message) {
        if (BuildConfig.DEBUG) {
            Log.d("Transmis", message);
        }
    }

    public static void e(String message) {
        if (BuildConfig.DEBUG) {
            Log.e("Transmis", message);
        }
    }
}
