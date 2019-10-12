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
public class IftttWebhooksActivity extends BaseSwitchActivity {

    private TextItem mKeyItem;
    private TextItem mEventItem;
    private TextItem mTestItem;

    @Override
    protected int getToolbarTitle() {
        return R.string.ifttt_webhooks_title;
    }

    @Override
    protected boolean showToolbarBackIcon() {
        return true;
    }

    @Override
    protected void addItems() {
        mKeyItem = new TextItem(this).setTitle("Key").setCallback(sp -> {
            String value = sp.getString(Tags.SP_IFTTT_WEBHOOKS_KEY, null);
            return TextUtils.isEmpty(value) ? "未设置" : value;
        });
        mEventItem = new TextItem(this).setTitle("Event").setCallback(sp -> {
            String value = sp.getString(Tags.SP_IFTTT_WEBHOOKS_EVENT, null);
            return TextUtils.isEmpty(value) ? "未设置" : value;
        });
        mTestItem = new TextItem(this).setTitle("点击发送测试数据");

        addItem(new SectionItem(this).setTitle("服务器设置"));
        addItem(mKeyItem);
        addItem(mEventItem);
        addItem(new SectionItem(this).setTitle("其他"));
        addItem(mTestItem);
    }

    @Override
    protected void setListeners() {
        setTextItemListener(mKeyItem, Tags.SP_IFTTT_WEBHOOKS_KEY, "设置 Key", false);
        setTextItemListener(mEventItem, Tags.SP_IFTTT_WEBHOOKS_EVENT, "设置 Event", false);
        mTestItem.setOnClickListener(v -> {
            new IftttWebhooksSender().send("Title Test", "Content Test");
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
