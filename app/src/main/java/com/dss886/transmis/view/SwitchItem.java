package com.dss886.transmis.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.dss886.transmis.R;
import com.dss886.transmis.base.App;

import androidx.appcompat.widget.SwitchCompat;

/**
 * Created by dss886 on 2017/6/29.
 */

public class SwitchItem extends BaseItem {

    private final TextView mTitle;
    private final SwitchCompat mSwitchView;

    private String mKey;
    private boolean mDefaultValue;
    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener;

    public SwitchItem(Context context) {
        super(context);
        View.inflate(getContext(), R.layout.view_switch_item, this);
        mTitle = findViewById(R.id.title);
        mSwitchView = findViewById(R.id.switcher);
    }

    @Override
    public void onResume() {
        boolean isChecked = App.sp.getBoolean(mKey, mDefaultValue);
        mSwitchView.setChecked(isChecked);
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(mSwitchView, isChecked);
        }
    }

    public SwitchItem setTitle(String title) {
        mTitle.setText(title);
        return this;
    }

    public SwitchItem setSpInfo(String key, boolean defaultValue) {
        mKey = key;
        mDefaultValue = defaultValue;
        mSwitchView.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = App.sp.edit();
            editor.putBoolean(key, isChecked);
            editor.apply();
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(mSwitchView, isChecked);
            }
        });
        return this;
    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }
}
