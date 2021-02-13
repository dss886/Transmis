package com.dss886.transmis.plugin

import com.dss886.transmis.BuildConfig
import com.dss886.transmis.base.App

/**
 * Created by dss886 on 2021/02/13.
 */
class PluginTester {

    var onSuccess: (() -> Unit)? = null
    var onFailure: ((Throwable) -> Unit)? = null

    fun success() {
        App.inst().mainHandler.post {
            onSuccess?.invoke()
        }
    }

    fun failure(reason: Throwable) {
        if (BuildConfig.DEBUG) {
            reason.printStackTrace()
        }
        App.inst().mainHandler.post {
            onFailure?.invoke(reason)
        }
    }

}