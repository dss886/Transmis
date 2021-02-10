package com.dss886.transmis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.InputType;
import android.text.TextUtils;

import com.dss886.transmis.base.App;
import com.dss886.transmis.base.BaseSwitchActivity;
import com.dss886.transmis.listen.call.CallActivity;
import com.dss886.transmis.listen.sms.SmsActivity;
import com.dss886.transmis.nofity.IftttWebhooksActivity;
import com.dss886.transmis.nofity.MailActivity;
import com.dss886.transmis.nofity.MailGunActivity;
import com.dss886.transmis.nofity.TelegramActivity;
import com.dss886.transmis.utils.DialogBuilder;
import com.dss886.transmis.utils.Tags;
import com.dss886.transmis.view.SectionItem;
import com.dss886.transmis.view.SwitchItem;
import com.dss886.transmis.view.TextItem;

public class MainActivity extends BaseSwitchActivity {

    private TextItem mSmsItem;
    private TextItem mCallItem;
    private TextItem mMailItem;
    private TextItem mDingDingItem;
    private TextItem mMailGunItem;
    private TextItem mTelegramItem;
    private TextItem mIftttWebhooksItem;
    private TextItem mHelpItem;
    private TextItem mVersionItem;
    private TextItem mLicenseItem;

    @Override
    protected int getToolbarTitle() {
        return R.string.app_name;
    }

    @Override
    protected boolean showToolbarBackIcon() {
        return false;
    }

    @Override
    protected void addItems() {
        mSmsItem = new TextItem(this).setTitle("短信").showRightArrow()
                .setCallback(sp -> {
                    if (
                            sp.getBoolean(Tags.SP_SMS_MAIL_ENABLE, true) ||
                                    sp.getBoolean(Tags.SP_SMS_DING_ENABLE, false) ||
                                    sp.getBoolean(Tags.SP_SMS_MAILGUN_ENABLE, false) ||
                                    sp.getBoolean(Tags.SP_SMS_TELEGRAM_ENABLE, false) ||
                                    sp.getBoolean(Tags.SP_SMS_IFTTT_WEBHOOKS_ENABLE, false)
                    ) {
                        return "开";
                    }
                    return "关";
                });
        mCallItem = new TextItem(this).setTitle("未接电话").showRightArrow()
                .setCallback(sp -> {
                    if (sp.getBoolean(Tags.SP_MISSED_CALL_MAIL_ENABLE, true) ||
                            sp.getBoolean(Tags.SP_MISSED_CALL_DING_ENABLE, false) ||
                            sp.getBoolean(Tags.SP_MISSED_CALL_MAILGUN_ENABLE, false) ||
                            sp.getBoolean(Tags.SP_MISSED_CALL_TELEGRAM_ENABLE, false) ||
                            sp.getBoolean(Tags.SP_MISSED_CALL_IFTTT_WEBHOOKS_ENABLE, false)) {
                        return "开";
                    }
                    return "关";
                });
        mMailItem = new TextItem(this, "邮件提醒").showRightArrow();
        mDingDingItem = new TextItem(this, "钉钉提醒").setCallback(sp -> {
            String value = sp.getString(Tags.SP_DING_TOKEN, null);
            return TextUtils.isEmpty(value) ? "未设置" : "已设置";
        });
        
        mHelpItem = new TextItem(this, "使用帮助");
        mVersionItem = new TextItem(this, "检查更新", "当前版本 v" + BuildConfig.VERSION_NAME);
        mLicenseItem = new TextItem(this, "开源许可", "GNU v3.0");
        mDingDingItem = new TextItem(this).setTitle("钉钉提醒").setCallback(sp -> {
            String value = sp.getString(Tags.SP_DING_TOKEN, null);
            return TextUtils.isEmpty(value) ? "未设置" : "已设置";
        });
        mMailGunItem = new TextItem(this).setTitle("MailGun 提醒").showRightArrow();
        mTelegramItem = new TextItem(this).setTitle("Telegram 提醒").showRightArrow();
        mIftttWebhooksItem = new TextItem(this).setTitle("IftttWebhooks 提醒").showRightArrow();
        mMailItem = new TextItem(this).setTitle("邮件提醒").showRightArrow();
        mHelpItem = new TextItem(this).setTitle("使用帮助");
        mVersionItem = new TextItem(this).setTitle("检查更新").setContent("当前版本 v" + BuildConfig.VERSION_NAME);
        mLicenseItem = new TextItem(this).setTitle("开源许可").setContent("GNU v3.0");

        addItem(new SwitchItem(this).setTitle("总开关").setSpInfo(Tags.SP_GLOBAL_ENABLE, false));
        addItem(new SectionItem(this).setTitle("监听内容"));
        addItem(mSmsItem);
        addItem(mCallItem);
        addItem(new SectionItem(this).setTitle("提醒插件"));
        addItem(mMailItem);
        addItem(mDingDingItem);
        addItem(mMailGunItem);
        addItem(mTelegramItem);
        addItem(mIftttWebhooksItem);
        addItem(new SectionItem(this).setTitle("关于"));
        addItem(mHelpItem);
        addItem(mVersionItem);
        addItem(mLicenseItem);
    }

    @Override
    protected void setListeners() {
        mSmsItem.setOnClickListener(v -> startActivity(new Intent(this, SmsActivity.class)));
        mCallItem.setOnClickListener(v -> startActivity(new Intent(this, CallActivity.class)));
        mMailItem.setOnClickListener(v -> startActivity(new Intent(this, MailActivity.class)));
        mDingDingItem.setOnClickListener(v -> {
            String value = App.sp.getString(Tags.SP_DING_TOKEN, null);
            DialogBuilder.showEditTextDialog(this, "请设置钉钉机器人的Token", value, InputType.TYPE_CLASS_TEXT, content -> {
                SharedPreferences.Editor editor = App.sp.edit();
                if (TextUtils.isEmpty(content)) {
                    editor.remove(Tags.SP_DING_TOKEN);
                } else {
                    editor.putString(Tags.SP_DING_TOKEN, content);
                }
                editor.apply();
                mDingDingItem.onResume();
            });
        });
        mMailGunItem.setOnClickListener(v -> startActivity(new Intent(this, MailGunActivity.class)));
        mTelegramItem.setOnClickListener(v -> startActivity(new Intent(this, TelegramActivity.class)));
        mIftttWebhooksItem.setOnClickListener(v -> startActivity(new Intent(this, IftttWebhooksActivity.class)));
        mHelpItem.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Tags.URL_README))));
        mVersionItem.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Tags.URL_RELEASE))));
        mLicenseItem.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Tags.URL_LICENSE))));
    }

}
