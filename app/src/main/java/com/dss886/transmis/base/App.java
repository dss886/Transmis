package com.dss886.transmis.base;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;

import com.dss886.transmis.plugin.PluginManager;

/**
 * Created by dss886 on 2017/6/29.
 */

public class App extends Application {

    private static App instance;
    public static SharedPreferences sp;
    public static Handler mainHandler = new Handler(Looper.getMainLooper());

    public static App me() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        PluginManager.INSTANCE.init();
    }
}
