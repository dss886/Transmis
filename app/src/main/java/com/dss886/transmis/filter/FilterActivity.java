package com.dss886.transmis.filter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;

import com.dss886.transmis.R;
import com.dss886.transmis.base.BaseRecyclerViewActivity;
import com.dss886.transmis.utils.DialogBuilder;
import com.dss886.transmis.utils.Tags;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by duansishu on 2018/1/25.
 */

public class FilterActivity extends BaseRecyclerViewActivity {

    private static final String BUNDLE_TYPE = "type";

    public enum Type {
        SMS_SENDER(R.string.sender_title, Tags.SP_FILTER_MODE_SMS_SENDER, Tags.SP_FILTER_VALUE_SMS_SENDER),
        SMS_KEYWORD(R.string.keyword_title, Tags.SP_FILTER_MODE_SMS_KEYWORD, Tags.SP_FILTER_VALUE_SMS_KEYWORD),
        CALL_SENDER(R.string.sender_call_title, Tags.SP_FILTER_MODE_CALL_SENDER, Tags.SP_FILTER_VALUE_CALL_SENDER);

        int titleResId;
        String modeSpKey;
        String valueSpKey;
        Type(int titleResId, String modeSpKey, String valueSpKey) {
            this.titleResId = titleResId;
            this.modeSpKey = modeSpKey;
            this.valueSpKey = valueSpKey;
        }
    }

    public static void start(Context context, Type type) {
        if (context == null || type == null) {
            return;
        }
        Intent starter = new Intent(context, FilterActivity.class);
        starter.putExtra(BUNDLE_TYPE, type);
        context.startActivity(starter);
    }

    private Type mType;
    private FilterAdapter mFilterAdapter;

    @Override
    protected void resolveIntentExtra(@NonNull Bundle bundle) {
        mType = (Type) bundle.getSerializable(BUNDLE_TYPE);
    }

    @Override
    protected void init(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mFilterAdapter = new FilterAdapter(this, mType);
        recyclerView.setAdapter(mFilterAdapter);
    }

    @Override
    protected int getToolbarTitle() {
        return mType.titleResId;
    }

    @Override
    protected boolean showToolbarBackIcon() {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            tryToAddFilterValue();
        }
        return super.onOptionsItemSelected(item);
    }

    private void tryToAddFilterValue() {
        int inputType = mType == Type.SMS_KEYWORD ? InputType.TYPE_CLASS_TEXT : InputType.TYPE_CLASS_NUMBER;
        DialogBuilder.showEditTextDialog(this, "请添加过滤项", "", inputType,
                content -> mFilterAdapter.add(content));
    }

}
