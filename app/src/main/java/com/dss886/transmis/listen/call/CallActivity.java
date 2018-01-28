package com.dss886.transmis.listen.call;

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

public class CallActivity extends BaseSwitchActivity {

    private SwitchItem mMailSwitch;
    private SwitchItem mDingSwitch;
    private TextItem mSenderItem;
    private TextItem mTitleItem;
    private TextItem mContentItem;

    @Override
    protected int getToolbarTitle() {
        return R.string.call_title;
    }

    @Override
    protected boolean showToolbarBackIcon() {
        return true;
    }

    @Override
    protected void addItems() {
        mMailSwitch = new SwitchItem(this, "邮件提醒", Tags.SP_MISSED_CALL_MAIL_ENABLE, true);
        mDingSwitch = new SwitchItem(this, "钉钉提醒", Tags.SP_MISSED_CALL_DING_ENABLE, false);
        mSenderItem = new TextItem(this, "来电过滤").showRightArrow();
        mTitleItem = new TextItem(this, "邮件标题").setCallback(sp -> {
            String value = sp.getString(Tags.SP_CALL_TITLE_REGEX, "默认");
            return TextUtils.isEmpty(value) ? "未设置" : value;
        });
        mContentItem = new TextItem(this, "邮件内容模版").setCallback(sp -> {
            String value = sp.getString(Tags.SP_CALL_CONTENT_REGEX, "默认");
            return TextUtils.isEmpty(value) ? "未设置" : value;
        });

        addItem(mMailSwitch);
        addItem(mDingSwitch);
        addItem(new SectionItem(this, "过滤"));
        addItem(mSenderItem);
        addItem(new SectionItem(this, "提醒模版设置"));
        addItem(mTitleItem);
        addItem(mContentItem);
        addItem(new InfoItem(this, getString(R.string.info_call_content)));
    }

    @Override
    protected void setListeners() {
        mSenderItem.setOnClickListener(v -> FilterActivity.start(this, FilterActivity.Type.CALL_SENDER));
        setTextItemListener(mTitleItem, Tags.SP_CALL_TITLE_REGEX, "设置标题");
        setTextItemListener(mContentItem, Tags.SP_CALL_CONTENT_REGEX, "设置内容模版");
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
