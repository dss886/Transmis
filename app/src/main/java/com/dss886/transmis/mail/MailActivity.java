package com.dss886.transmis.mail;

import android.content.SharedPreferences;
import android.text.TextUtils;
import com.dss886.transmis.R;
import com.dss886.transmis.base.App;
import com.dss886.transmis.base.BaseActivity;
import com.dss886.transmis.utils.DialogBuilder;
import com.dss886.transmis.utils.Tags;
import com.dss886.transmis.view.TextItem;

public class MailActivity extends BaseActivity {

    private TextItem mHostItem;
    private TextItem mPortItem;
    private TextItem mSendMailItem;
    private TextItem mSendPasswordItem;
    private TextItem mReceiveItem;

    @Override
    protected int getToolbarTitle() {
        return R.string.mail_title;
    }

    @Override
    protected boolean showToolbarBackIcon() {
        return true;
    }

    @Override
    protected void addViews() {
        mHostItem = new TextItem(this, "SMTP服务器", Tags.SP_MAIL_HOST,
                value -> TextUtils.isEmpty(value) ? "未设置" : value);
        mPortItem = new TextItem(this, "端口号", Tags.SP_MAIL_PORT,
                value -> TextUtils.isEmpty(value) ? "未设置" : value);
        mSendMailItem = new TextItem(this, "发件人邮箱", Tags.SP_MAIL_SEND_MAIL,
                value -> TextUtils.isEmpty(value) ? "未设置" : value);
        mSendPasswordItem = new TextItem(this, "发件人密码/授权码", Tags.SP_MAIL_SEND_PASSWORD,
                value -> TextUtils.isEmpty(value) ? "未设置" : value);
        mReceiveItem = new TextItem(this, "收件人邮箱", Tags.SP_MAIL_RECEIVE_MAIL,
                value -> TextUtils.isEmpty(value) ? "未设置" : value);

        addView(mHostItem);
        addView(mPortItem);
        addView(mSendMailItem);
        addView(mSendPasswordItem);
        addView(mReceiveItem);
    }

    @Override
    protected void setListeners() {
        setTextItemListener(mHostItem, Tags.SP_MAIL_HOST, "设置服务器");
        setTextItemListener(mPortItem, Tags.SP_MAIL_PORT, "设置端口号");
        setTextItemListener(mSendMailItem, Tags.SP_MAIL_SEND_MAIL, "设置发件人邮箱");
        setTextItemListener(mSendPasswordItem, Tags.SP_MAIL_SEND_PASSWORD, "设置发件人密码/授权码");
        setTextItemListener(mReceiveItem, Tags.SP_MAIL_RECEIVE_MAIL, "设置收件人邮箱");
    }

    private void setTextItemListener(TextItem item, String key, String showTitle) {
        item.setOnClickListener(v -> {
            String value = App.me().sp.getString(key, null);
            DialogBuilder.showEditTextDialog(this, showTitle, value, content -> {
                SharedPreferences.Editor editor = App.me().sp.edit();
                editor.putString(key, content);
                editor.apply();
                item.onResume();
            });
        });
    }

}
