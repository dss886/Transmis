package com.dss886.transmis.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import com.dss886.transmis.R;
import com.dss886.transmis.base.App;

/**
 * Created by dss886 on 2017/6/29.
 */

@SuppressLint("ViewConstructor")
public class SwitchItem extends BaseItem {

    private Switch mSwitchView;
    private String mKey;
    private boolean mDefaultValue;

    public SwitchItem(Context context, String title, String key, boolean defaultValue) {
        super(context);
        this.mKey = key;
        this.mDefaultValue = defaultValue;

        View.inflate(getContext(), R.layout.view_switch_item, this);

        TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setText(title);

        mSwitchView = (Switch) findViewById(R.id.switcher);
        mSwitchView.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = App.sp.edit();
            editor.putBoolean(key, isChecked);
            editor.apply();
        });
    }

    @Override
    public void onResume() {
        mSwitchView.setChecked(App.sp.getBoolean(mKey, mDefaultValue));
    }
}
