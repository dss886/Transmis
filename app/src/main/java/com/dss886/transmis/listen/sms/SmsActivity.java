package com.dss886.transmis.listen.sms;

import android.content.SharedPreferences;
import android.text.TextUtils;
import com.dss886.transmis.R;
import com.dss886.transmis.base.App;
import com.dss886.transmis.base.BaseActivity;
import com.dss886.transmis.utils.DialogBuilder;
import com.dss886.transmis.utils.Tags;
import com.dss886.transmis.view.InfoItem;
import com.dss886.transmis.view.SectionItem;
import com.dss886.transmis.view.SwitchItem;
import com.dss886.transmis.view.TextItem;

public class SmsActivity extends BaseActivity {

    private SwitchItem mMailSwitch;
    private SwitchItem mDingSwitch;
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
        mMailSwitch = new SwitchItem(this, "邮件提醒", Tags.SP_SMS_MAIL_ENABLE, true);
        mDingSwitch = new SwitchItem(this, "钉钉提醒", Tags.SP_SMS_DING_ENABLE, false);
        mMergeSwitch = new SwitchItem(this, "合并长短信", Tags.SP_SMS_MERGE_LONG_TEXT, true);
        mTitleItem = new TextItem(this, "提醒标题").setCallback(sp -> {
            String value = sp.getString(Tags.SP_SMS_TITLE_REGEX, "默认");
            return TextUtils.isEmpty(value) ? "未设置" : value;
        });
        mContentItem = new TextItem(this, "提醒内容模版").setCallback(sp -> {
            String value = sp.getString(Tags.SP_SMS_CONTENT_REGEX, "默认");
            return TextUtils.isEmpty(value) ? "未设置" : value;
        });

        addView(mMailSwitch);
        addView(mDingSwitch);
        addView(new SectionItem(this, "可选项"));
        addView(mMergeSwitch);
        addView(new SectionItem(this, "提醒模版设置"));
        addView(mTitleItem);
        addView(mContentItem);
        addView(new InfoItem(this, getString(R.string.info_sms_content)));
    }

    @Override
    protected void setListeners() {
        mMailSwitch.setOnClickListener(v -> {});
        mDingSwitch.setOnClickListener(v -> {});
        mMergeSwitch.setOnClickListener(v -> {});
        setTextItemListener(mTitleItem, Tags.SP_SMS_TITLE_REGEX, "设置标题");
        setTextItemListener(mContentItem, Tags.SP_SMS_CONTENT_REGEX, "设置内容模版");
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
