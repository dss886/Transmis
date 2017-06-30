package com.dss886.transmis.sms;

import com.dss886.transmis.R;
import com.dss886.transmis.base.BaseActivity;
import com.dss886.transmis.utils.Tags;
import com.dss886.transmis.view.SwitchItem;

public class SmsActivity extends BaseActivity {

    @Override
    protected int getToolbarTitle() {
        return R.string.sms_title;
    }

    @Override
    protected boolean showToolbarBackIcon() {
        return true;
    }

    @Override
    protected void addViews() {
        addView(new SwitchItem(this, "短信提醒", Tags.SP_SMS_ENABLE, false));
    }

    @Override
    protected void setListeners() {

    }
}
