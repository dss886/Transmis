package com.dss886.transmis.sms;

import android.content.SharedPreferences;
import android.text.TextUtils;
import com.dss886.transmis.R;
import com.dss886.transmis.base.App;
import com.dss886.transmis.base.BaseActivity;
import com.dss886.transmis.utils.DialogBuilder;
import com.dss886.transmis.utils.Tags;
import com.dss886.transmis.view.SwitchItem;
import com.dss886.transmis.view.TextItem;

public class SmsActivity extends BaseActivity {

    private SwitchItem mSmsSwitch;
    private SwitchItem mMergeSwitch;
    private TextItem mTitleItem;
    private TextItem mContentItem;

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
        mSmsSwitch = new SwitchItem(this, "短信提醒", Tags.SP_SMS_ENABLE, true);
        mMergeSwitch = new SwitchItem(this, "合并长短信", Tags.SP_SMS_MERGE_LONG_TEXT, true);
        mTitleItem = new TextItem(this, "短信提醒标题").setCallback(sp -> {
            String value = sp.getString(Tags.SP_SMS_TITLE_REGEX, "默认");
            return TextUtils.isEmpty(value) ? "未设置" : value;
        });
        mContentItem = new TextItem(this, "短信提醒内容").setCallback(sp -> {
            String value = sp.getString(Tags.SP_SMS_CONTENT_REGEX, "默认");
            return TextUtils.isEmpty(value) ? "未设置" : value;
        });

        addView(mSmsSwitch);
        addView(mMergeSwitch);
        addView(mTitleItem);
        addView(mContentItem);
    }

    @Override
    protected void setListeners() {
        mSmsSwitch.setOnClickListener(v -> {});
        mMergeSwitch.setOnClickListener(v -> {});
        setTextItemListener(mTitleItem, Tags.SP_SMS_TITLE_REGEX, "设置提醒标题");
        setTextItemListener(mContentItem, Tags.SP_SMS_CONTENT_REGEX, "设置提醒内容");
    }

    private void setTextItemListener(TextItem item, String key, String showTitle) {
        item.setOnClickListener(v -> {
            String value = App.sp.getString(key, null);
            DialogBuilder.showEditTextDialog(this, showTitle, value, false, content -> {
                SharedPreferences.Editor editor = App.sp.edit();
                if (TextUtils.isEmpty(content)) {
                    editor.remove(key);
                } else {
                    editor.putString(key, content);
                }
                editor.apply();
                item.onResume();
            });
        });
    }
}
