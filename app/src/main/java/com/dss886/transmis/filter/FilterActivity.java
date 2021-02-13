package com.dss886.transmis.filter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;

import com.dss886.transmis.R;
import com.dss886.transmis.base.BaseActivity;
import com.dss886.transmis.utils.DialogBuilder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by duansishu on 2018/1/25.
 */

public class FilterActivity extends BaseActivity {

    private static final String BUNDLE_TYPE = "type";

    public static void start(Context context, FilterType type) {
        if (context == null || type == null) {
            return;
        }
        Intent starter = new Intent(context, FilterActivity.class);
        starter.putExtra(BUNDLE_TYPE, type);
        context.startActivity(starter);
    }

    private FilterType mType;
    private FilterAdapter mFilterAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mFilterAdapter = new FilterAdapter(this, mType);
        recyclerView.setAdapter(mFilterAdapter);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_base_recycler_view;
    }

    @Override
    protected void resolveIntentExtra(@NonNull Bundle bundle) {
        mType = (FilterType) bundle.getSerializable(BUNDLE_TYPE);
    }

    @NotNull
    @Override
    protected String getToolbarTitle() {
        return getString(mType.getTitleResId());
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
        int inputType = mType == FilterType.SMS_KEYWORD ? InputType.TYPE_CLASS_TEXT : InputType.TYPE_CLASS_NUMBER;
        DialogBuilder.showEditTextDialog(this, "请添加过滤项", "", inputType,
                content -> mFilterAdapter.add(content));
    }

}
