package com.dss886.transmis;

import android.content.Intent;
import com.dss886.transmis.base.BaseActivity;
import com.dss886.transmis.call.CallActivity;
import com.dss886.transmis.mail.MailActivity;
import com.dss886.transmis.sms.SmsActivity;
import com.dss886.transmis.utils.Tags;
import com.dss886.transmis.view.DetailItem;
import com.dss886.transmis.view.SwitchItem;

public class MainActivity extends BaseActivity {

    private SwitchItem mGlobalSwitch;
    private SwitchItem mSmsSwitch;
    private SwitchItem mCallSwitch;
    private DetailItem mMailDetail;

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
        mMailDetail = new DetailItem(this, "邮件参数设置");

        addView(mGlobalSwitch);
        addView(mSmsSwitch);
        addView(mCallSwitch);
        addView(mMailDetail);
    }

    @Override
    protected void setListeners() {
        mGlobalSwitch.setOnClickListener(v -> {});
        mGlobalSwitch.addOnCheckedChangeListener((buttonView, isChecked) -> {
            mSmsSwitch.setEnable(isChecked);
            mCallSwitch.setEnable(isChecked);
        });
        mSmsSwitch.setOnClickListener(v -> startActivity(new Intent(this, SmsActivity.class)));
        mCallSwitch.setOnClickListener(v -> startActivity(new Intent(this, CallActivity.class)));
        mMailDetail.setOnClickListener(v -> startActivity(new Intent(this, MailActivity.class)));
    }

}
