package com.dss886.transmis.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.dss886.transmis.R;
import com.dss886.transmis.base.App;
import com.dss886.transmis.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dss886 on 2017/6/29.
 */

@SuppressLint("ViewConstructor")
public class SwitchItem extends LinearLayout implements Resumable {

    private Switch mSwitchView;
    private List<CompoundButton.OnCheckedChangeListener> mCheckedChangeListeners = new ArrayList<>();
    private String mKey;
    private boolean mDefaultValue;

    public SwitchItem(Context context, String title, String key, boolean defaultValue) {
        super(context);
        this.mKey = key;
        this.mDefaultValue = defaultValue;

        View.inflate(getContext(), R.layout.view_switch_item, this);
        setMinimumHeight(DisplayUtil.dip2px(getContext(), 56));

        TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setText(title);

        mSwitchView = (Switch) findViewById(R.id.switcher);
        mSwitchView.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = App.sp.edit();
            editor.putBoolean(key, isChecked);
            editor.apply();
            for (CompoundButton.OnCheckedChangeListener listener : mCheckedChangeListeners) {
                listener.onCheckedChanged(buttonView, isChecked);
            }
        });
    }

    public void addOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        mCheckedChangeListeners.add(listener);
    }

    public void setChecked(boolean checked) {
        mSwitchView.setChecked(checked);
    }

    public void setEnable(boolean enable) {
        mSwitchView.setEnabled(enable);
    }

    @Override
    public void onResume() {
        mSwitchView.setChecked(App.sp.getBoolean(mKey, mDefaultValue));
    }
}
