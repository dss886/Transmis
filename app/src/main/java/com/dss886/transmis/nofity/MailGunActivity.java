package com.dss886.transmis.nofity;

import android.content.SharedPreferences;
import android.text.InputType;
import android.text.TextUtils;

import com.dss886.transmis.R;
import com.dss886.transmis.base.App;
import com.dss886.transmis.base.BaseSwitchActivity;
import com.dss886.transmis.utils.DialogBuilder;
import com.dss886.transmis.utils.Tags;
import com.dss886.transmis.view.SectionItem;
import com.dss886.transmis.view.TextItem;

/**
 * Created by dray on 2019/10/13.
 */
public class MailGunActivity extends BaseSwitchActivity {

    private TextItem mKeyItem;
    private TextItem mDomainItem;
    private TextItem mSendNameItem;
    private TextItem mSendMailItem;
    private TextItem mReceiveItem;
    private TextItem mTestItem;

    @Override
    protected int getToolbarTitle() {
        return R.string.mailgum_title;
    }

    @Override
    protected boolean showToolbarBackIcon() {
        return true;
    }

    @Override
    protected void addItems() {
        mKeyItem = new TextItem(this).setTitle("密钥 Key").setCallback(sp -> {
            String value = sp.getString(Tags.SP_MAILGUN_KEY, null);
            return TextUtils.isEmpty(value) ? "未设置" : value;
        });
        mDomainItem = new TextItem(this).setTitle("发件人邮箱域名 Domain").setCallback(sp -> {
            String value = sp.getString(Tags.SP_MAILGUN_DOMAIN, null);
            return TextUtils.isEmpty(value) ? "未设置" : value;
        });
        mSendNameItem = new TextItem(this).setTitle("发件人昵称").setCallback(sp -> {
            String value = sp.getString(Tags.SP_MAILGUN_SEND_NAME, "默认");
            return TextUtils.isEmpty(value) ? "未设置" : value;
        });
        mSendMailItem = new TextItem(this).setTitle("发件人邮箱").setCallback(sp -> {
            String value = sp.getString(Tags.SP_MAILGUN_SEND_MAIL, null);
            return TextUtils.isEmpty(value) ? "未设置" : value;
        });
        mReceiveItem = new TextItem(this).setTitle("收件人邮箱").setCallback(sp -> {
            String value = sp.getString(Tags.SP_MAILGUN_RECEIVE_MAIL, null);
            return TextUtils.isEmpty(value) ? "未设置" : value;
        });
        mTestItem = new TextItem(this).setTitle("点击发送测试邮件");

        addItem(new SectionItem(this).setTitle("服务器设置"));
        addItem(mKeyItem);
        addItem(mDomainItem);
        addItem(new SectionItem(this).setTitle("发件人设置"));
        addItem(mSendNameItem);
        addItem(mSendMailItem);
        addItem(new SectionItem(this).setTitle("收件人设置"));
        addItem(mReceiveItem);
        addItem(new SectionItem(this).setTitle("其他"));
        addItem(mTestItem);
    }

    @Override
    protected void setListeners() {
        setTextItemListener(mKeyItem, Tags.SP_MAILGUN_KEY, "设置密钥 key", false);
        setTextItemListener(mSendNameItem, Tags.SP_MAILGUN_SEND_NAME, "设置发件人昵称", false);
        setTextItemListener(mDomainItem, Tags.SP_MAILGUN_DOMAIN, "设置发件人邮箱域名 Domain", false);
        setTextItemListener(mSendMailItem, Tags.SP_MAILGUN_SEND_MAIL, "设置发件人邮箱", false);
        setTextItemListener(mReceiveItem, Tags.SP_MAILGUN_RECEIVE_MAIL, "设置收件人邮箱", false);
        mTestItem.setOnClickListener(v -> {
            new MailGunSender().send("Title Test", "Content Test");
        });
    }

    private void setTextItemListener(TextItem item, String key, String showTitle, boolean isPassword) {
        item.setOnClickListener(v -> {
            String value = App.sp.getString(key, null);
            int inputType = InputType.TYPE_CLASS_TEXT;
            if (isPassword) {
                inputType |= InputType.TYPE_TEXT_VARIATION_PASSWORD;
            }
            DialogBuilder.showEditTextDialog(this, showTitle, value, inputType, content -> {
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
