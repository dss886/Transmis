package com.dss886.transmis.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by dss886 on 2017/6/29.
 */

public class Settings {

    private static final String KEY_IS_ENABLE = "is_enable";

    private static Settings instance;

    public static Settings inst() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    private SharedPreferences sp;
    private boolean mIsEnable;

    public void init(Context context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        readData();
    }

    private void readData() {
        mIsEnable = sp.getBoolean(KEY_IS_ENABLE, false);
    }

    private void setData() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(KEY_IS_ENABLE, mIsEnable);
        editor.apply();
    }

    public boolean isEnable() {
        return mIsEnable;
    }

    public void setEnable(boolean enable) {
        if (mIsEnable != enable) {
            mIsEnable = enable;
            setData();
        }
    }
}
