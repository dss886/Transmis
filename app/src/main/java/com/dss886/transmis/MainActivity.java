package com.dss886.transmis;

import android.content.Intent;
import android.net.Uri;
import com.dss886.transmis.base.BaseActivity;
import com.dss886.transmis.call.CallActivity;
import com.dss886.transmis.mail.MailActivity;
import com.dss886.transmis.sms.SmsActivity;
import com.dss886.transmis.utils.Tags;
import com.dss886.transmis.view.SectionItem;
import com.dss886.transmis.view.SwitchItem;
import com.dss886.transmis.view.TextItem;

public class MainActivity extends BaseActivity {

    private SwitchItem mGlobalSwitch;
    private TextItem mSmsItem;
    private TextItem mCallItem;
    private TextItem mMailItem;
    private TextItem mHelpItem;
    private TextItem mVersionItem;
    private TextItem mLicenseItem;

    @Override
    protected int getToolbarTitle() {
        return R.string.main_title;
    }

    @Override
    protected boolean showToolbarBackIcon() {
        return false;
    }

    @Override
    protected void addViews() {
        mGlobalSwitch = new SwitchItem(this, "总开关", Tags.SP_GLOBAL_ENABLE, false);
        mSmsItem = new TextItem(this, "短信提醒").showRightArrow()
                .setCallback(sp -> sp.getBoolean(Tags.SP_SMS_ENABLE, true) ? "开" : "关");
        mCallItem = new TextItem(this, "未接电话提醒").showRightArrow()
                .setCallback(sp -> sp.getBoolean(Tags.SP_MISSED_CALL_ENABLE, true) ? "开" : "关");
        mMailItem = new TextItem(this, "邮件参数设置").showRightArrow();
        mHelpItem = new TextItem(this, "使用帮助");
        mVersionItem = new TextItem(this, "检查更新", "当前版本 v" + BuildConfig.VERSION_NAME);
        mLicenseItem = new TextItem(this, "开源许可", "Apache 2.0");

        addView(mGlobalSwitch);
        addView(new SectionItem(this, "监听内容"));
        addView(mSmsItem);
        addView(mCallItem);
        addView(new SectionItem(this, "参数设置"));
        addView(mMailItem);
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
        mHelpItem.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Tags.URL_README))));
        mVersionItem.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Tags.URL_RELEASE))));
        mLicenseItem.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Tags.URL_LICENSE))));
    }

}
