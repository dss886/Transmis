package com.dss886.transmis.base;

import android.app.Application;

/**
 * Created by dss886 on 2017/6/29.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Settings.inst().init(this);
    }
}
