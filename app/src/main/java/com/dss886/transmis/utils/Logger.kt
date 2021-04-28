package com.dss886.transmis.utils

import android.util.Log
import com.dss886.transmis.BuildConfig

/**
 * Created by dss886 on 2017/6/29.
 */
object Logger {

    fun d(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message)
        }
    }

    fun e(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message)
        }
    }
}