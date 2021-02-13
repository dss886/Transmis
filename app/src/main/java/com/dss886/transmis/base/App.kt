package com.dss886.transmis.base

import android.app.Application
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import androidx.preference.PreferenceManager
import com.dss886.transmis.plugin.PluginManager

/**
 * Created by dss886 on 2017/6/29.
 */
class App : Application() {

    companion object {
        private lateinit var inst: App

        @JvmStatic
        fun inst(): App {
            return inst
        }
    }

    lateinit var sp : SharedPreferences
    lateinit var mainHandler : Handler

    override fun onCreate() {
        super.onCreate()
        inst = this
        sp = PreferenceManager.getDefaultSharedPreferences(this)
        mainHandler = Handler(Looper.getMainLooper())
        PluginManager.init()
    }

}