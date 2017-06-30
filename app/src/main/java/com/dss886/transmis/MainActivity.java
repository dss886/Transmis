package com.dss886.transmis;

import android.content.Intent;
import com.dss886.transmis.base.BaseActivity;
import com.dss886.transmis.call.CallActivity;
import com.dss886.transmis.sms.SmsActivity;
import com.dss886.transmis.utils.Tags;
import com.dss886.transmis.view.SwitchItem;

public class MainActivity extends BaseActivity {

    private SwitchItem mGlobalSwitch;
    private SwitchItem mSmsSwitch;
    private SwitchItem mCallSwitch;

    @Override
    protected int getToolbarTitle() {
        return R.string.main_title;
    }

    @Override
    protected boolean showToolbarBackIcon() {
        return false;
    }

    @Override
    protected void addViews() {
        mGlobalSwitch = new SwitchItem(this, "Transmis 总开关", Tags.SP_GLOBAL_ENABLE, false);
        mSmsSwitch = new SwitchItem(this, "短信提醒", Tags.SP_SMS_ENABLE, false);
        mCallSwitch = new SwitchItem(this, "未接电话提醒", Tags.SP_MISSED_CALL_ENABLE, false);

        addView(mGlobalSwitch);
        addView(mSmsSwitch);
        addView(mCallSwitch);
    }

    @Override
    protected void setListeners() {
        mGlobalSwitch.addOnCheckedChangeListener((buttonView, isChecked) -> {
            mSmsSwitch.setEnable(isChecked);
            mCallSwitch.setEnable(isChecked);
        });
        mSmsSwitch.setOnClickListener(v -> startActivity(new Intent(this, SmsActivity.class)));
        mCallSwitch.setOnClickListener(v -> startActivity(new Intent(this, CallActivity.class)));
    }

}
