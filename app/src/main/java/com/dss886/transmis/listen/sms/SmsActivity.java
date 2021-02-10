package com.dss886.transmis.listen.sms;

import android.content.SharedPreferences;
import android.text.InputType;
import android.text.TextUtils;

import com.dss886.transmis.R;
import com.dss886.transmis.base.App;
import com.dss886.transmis.base.BaseSwitchActivity;
import com.dss886.transmis.filter.FilterActivity;
import com.dss886.transmis.utils.DialogBuilder;
import com.dss886.transmis.utils.Tags;
import com.dss886.transmis.view.InfoItem;
import com.dss886.transmis.view.SectionItem;
import com.dss886.transmis.view.SwitchItem;
import com.dss886.transmis.view.TextItem;

@SuppressWarnings("FieldCanBeLocal")
public class SmsActivity extends BaseSwitchActivity {

    private SwitchItem mMailSwitch;
    private SwitchItem mDingSwitch;
    private TextItem mSenderItem;
    private TextItem mKeyWordItem;
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
    protected void addItems() {
        mMailSwitch = new SwitchItem(this).setTitle("邮件提醒").setSpInfo(Tags.SP_SMS_MAIL_ENABLE, true);
        mDingSwitch = new SwitchItem(this).setTitle("钉钉提醒").setSpInfo(Tags.SP_SMS_DING_ENABLE, false);
        mSenderItem = new TextItem(this).setTitle("发件人过滤").showRightArrow();
        mKeyWordItem = new TextItem(this).setTitle("关键词过滤").showRightArrow();
        mMergeSwitch = new SwitchItem(this).setTitle("合并长短信").setSpInfo(Tags.SP_SMS_MERGE_LONG_TEXT, true);
        mTitleItem = new TextItem(this).setTitle("提醒标题").setCallback(sp -> {
            String value = sp.getString(Tags.SP_SMS_TITLE_REGEX, "默认");
            return TextUtils.isEmpty(value) ? "未设置" : value;
        });
        mContentItem = new TextItem(this).setTitle("提醒内容模版").setCallback(sp -> {
            String value = sp.getString(Tags.SP_SMS_CONTENT_REGEX, "默认");
            return TextUtils.isEmpty(value) ? "未设置" : value;
        });

        addItem(mMailSwitch);
        addItem(mDingSwitch);
        addItem(new SectionItem(this).setTitle("过滤"));
        addItem(mSenderItem);
        addItem(mKeyWordItem);
        addItem(new SectionItem(this).setTitle("可选项"));
        addItem(mMergeSwitch);
        addItem(new SectionItem(this).setTitle("提醒模版设置"));
        addItem(mTitleItem);
        addItem(mContentItem);
        addItem(new InfoItem(this).setContent(getString(R.string.info_sms_content)));
    }

    @Override
    protected void setListeners() {
        mSenderItem.setOnClickListener(v -> FilterActivity.start(this, FilterActivity.Type.SMS_SENDER));
        mKeyWordItem.setOnClickListener(v -> FilterActivity.start(this, FilterActivity.Type.SMS_KEYWORD));
        setTextItemListener(mTitleItem, Tags.SP_SMS_TITLE_REGEX, "设置标题");
        setTextItemListener(mContentItem, Tags.SP_SMS_CONTENT_REGEX, "设置内容模版");
    }

    private void setTextItemListener(TextItem item, String key, String showTitle) {
        item.setOnClickListener(v -> {
            String value = App.sp.getString(key, null);
            DialogBuilder.showEditTextDialog(this, showTitle, value, InputType.TYPE_CLASS_TEXT, content -> {
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
