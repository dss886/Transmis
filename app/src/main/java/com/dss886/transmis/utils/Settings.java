package com.dss886.transmis.utils;

import com.dss886.transmis.base.App;

/**
 * Created by dss886 on 2017/6/29.
 */

public class Settings {

    public static boolean is(String key, boolean defaultValue) {
        return App.me().sp.getBoolean(key, defaultValue);
    }

    public static String get(String key) {
        return App.me().sp.getString(key, null);
    }
}
