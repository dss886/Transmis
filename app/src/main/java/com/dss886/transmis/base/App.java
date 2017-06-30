package com.dss886.transmis.base;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by dss886 on 2017/6/29.
 */

public class App extends Application {

    private static App instance;
    public SharedPreferences sp;

    public static App me() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        sp = PreferenceManager.getDefaultSharedPreferences(this);
    }
}
