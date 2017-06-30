package com.dss886.transmis.call;

import com.dss886.transmis.R;
import com.dss886.transmis.base.BaseActivity;
import com.dss886.transmis.utils.Tags;
import com.dss886.transmis.view.SwitchItem;

public class CallActivity extends BaseActivity {

    @Override
    protected int getToolbarTitle() {
        return R.string.call_title;
    }

    @Override
    protected boolean showToolbarBackIcon() {
        return true;
    }

    @Override
    protected void addViews() {
        addView(new SwitchItem(this, "未接电话提醒", Tags.SP_MISSED_CALL_ENABLE, false));
    }

    @Override
    protected void setListeners() {

    }
}
