package com.dss886.transmis;

import android.content.Intent;
import com.dss886.transmis.base.BaseActivity;
import com.dss886.transmis.call.CallActivity;
import com.dss886.transmis.mail.MailActivity;
import com.dss886.transmis.sms.SmsActivity;
import com.dss886.transmis.utils.Tags;
import com.dss886.transmis.view.SwitchItem;
import com.dss886.transmis.view.TextItem;

public class MainActivity extends BaseActivity {

    private SwitchItem mGlobalSwitch;
    private TextItem mSmsItem;
    private TextItem mCallItem;
    private TextItem mMailItem;

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
        mGlobalSwitch = new SwitchItem(this, "总开关", Tags.SP_GLOBAL_ENABLE, false);
        mSmsItem = new TextItem(this, "短信提醒").showRightArrow()
                .setCallback(sp -> sp.getBoolean(Tags.SP_SMS_ENABLE, true) ? "开" : "关");
        mCallItem = new TextItem(this, "未接电话提醒").showRightArrow()
                .setCallback(sp -> sp.getBoolean(Tags.SP_MISSED_CALL_ENABLE, true) ? "开" : "关");
        mMailItem = new TextItem(this, "邮件参数设置").showRightArrow();

        addView(mGlobalSwitch);
        addView(mSmsItem);
        addView(mCallItem);
        addView(mMailItem);
    }

    @Override
    protected void setListeners() {
        mGlobalSwitch.setOnClickListener(v -> {});
        mSmsItem.setOnClickListener(v -> startActivity(new Intent(this, SmsActivity.class)));
        mCallItem.setOnClickListener(v -> startActivity(new Intent(this, CallActivity.class)));
        mMailItem.setOnClickListener(v -> startActivity(new Intent(this, MailActivity.class)));
    }

}
