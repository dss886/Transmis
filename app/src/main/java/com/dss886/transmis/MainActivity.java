package com.dss886.transmis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import com.dss886.transmis.base.App;
import com.dss886.transmis.base.BaseActivity;
import com.dss886.transmis.call.CallActivity;
import com.dss886.transmis.listen.sms.SmsActivity;
import com.dss886.transmis.listen.mail.MailActivity;
import com.dss886.transmis.utils.DialogBuilder;
import com.dss886.transmis.utils.Tags;
import com.dss886.transmis.view.SectionItem;
import com.dss886.transmis.view.SwitchItem;
import com.dss886.transmis.view.TextItem;

public class MainActivity extends BaseActivity {

    private SwitchItem mGlobalSwitch;
    private TextItem mSmsItem;
    private TextItem mCallItem;
    private TextItem mMailItem;
    private TextItem mDingDingItem;
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
    protected void addViews() {
        mGlobalSwitch = new SwitchItem(this, "总开关", Tags.SP_GLOBAL_ENABLE, false);
        mSmsItem = new TextItem(this, "短信").showRightArrow()
                .setCallback(sp -> {
                    if (sp.getBoolean(Tags.SP_SMS_MAIL_ENABLE, true) ||
                            sp.getBoolean(Tags.SP_SMS_DING_ENABLE, false)) {
                        return "开";
                    }
                    return "关";
                });
        mCallItem = new TextItem(this, "未接电话").showRightArrow()
                .setCallback(sp -> {
                    if (sp.getBoolean(Tags.SP_MISSED_CALL_MAIL_ENABLE, true) ||
                            sp.getBoolean(Tags.SP_MISSED_CALL_DING_ENABLE, false)) {
                        return "开";
                    }
                    return "关";
                });
        mDingDingItem = new TextItem(this, "钉钉提醒").setCallback(sp -> {
            String value = sp.getString(Tags.SP_DING_TOKEN, null);
            return TextUtils.isEmpty(value) ? "未设置" : "已设置";
        });
        mMailItem = new TextItem(this, "邮件提醒").showRightArrow();
        mHelpItem = new TextItem(this, "使用帮助");
        mVersionItem = new TextItem(this, "检查更新", "当前版本 v" + BuildConfig.VERSION_NAME);
        mLicenseItem = new TextItem(this, "开源许可", "GNU v3.0");

        addView(mGlobalSwitch);
        addView(new SectionItem(this, "监听内容"));
        addView(mSmsItem);
        addView(mCallItem);
        addView(new SectionItem(this, "提醒插件"));
        addView(mMailItem);
        addView(mDingDingItem);
        addView(new SectionItem(this, "关于"));
        addView(mHelpItem);
        addView(mVersionItem);
        addView(mLicenseItem);
    }

    @Override
    protected void setListeners() {
        mGlobalSwitch.setOnClickListener(v -> {});
        mSmsItem.setOnClickListener(v -> startActivity(new Intent(this, SmsActivity.class)));
        mCallItem.setOnClickListener(v -> startActivity(new Intent(this, CallActivity.class)));
        mMailItem.setOnClickListener(v -> startActivity(new Intent(this, MailActivity.class)));
        mDingDingItem.setOnClickListener(v -> {
            String value = App.sp.getString(Tags.SP_DING_TOKEN, null);
            DialogBuilder.showEditTextDialog(this, "请设置钉钉机器人的Token", value, false, content -> {
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
        mHelpItem.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Tags.URL_README))));
        mVersionItem.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Tags.URL_RELEASE))));
        mLicenseItem.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Tags.URL_LICENSE))));
    }

}
