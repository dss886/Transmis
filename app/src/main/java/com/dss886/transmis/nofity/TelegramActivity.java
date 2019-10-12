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
public class TelegramActivity extends BaseSwitchActivity {

    private TextItem mUrlItem;
    private TextItem mChatIdItem;
    private TextItem mTestItem;

    @Override
    protected int getToolbarTitle() {
        return R.string.telegram_title;
    }

    @Override
    protected boolean showToolbarBackIcon() {
        return true;
    }

    @Override
    protected void addItems() {
        mUrlItem = new TextItem(this, "包含 token 的完整接口 url").setCallback(sp -> {
            String value = sp.getString(Tags.SP_TELEGRAM_URL, null);
            return TextUtils.isEmpty(value) ? "未设置" : value;
        });
        mChatIdItem = new TextItem(this, "发送到的 chat id").setCallback(sp -> {
            String value = sp.getString(Tags.SP_TELEGRAM_CHAT_ID, null);
            return TextUtils.isEmpty(value) ? "未设置" : value;
        });
        mTestItem = new TextItem(this, "点击发送测试数据");

        addItem(new SectionItem(this, "服务器设置"));
        addItem(mUrlItem);
        addItem(new SectionItem(this, "收件人设置"));
        addItem(mChatIdItem);
        addItem(new SectionItem(this, "其他"));
        addItem(mTestItem);
    }

    @Override
    protected void setListeners() {
        setTextItemListener(mUrlItem, Tags.SP_TELEGRAM_URL, "设置包含 token 的完整接口 url", false);
        setTextItemListener(mChatIdItem, Tags.SP_TELEGRAM_CHAT_ID, "设置发送到的 chat id", false);
        mTestItem.setOnClickListener(v -> {
            new TelegramSender().send("Title Test", "Content Test");
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
