package com.dss886.transmis.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import com.dss886.transmis.R;
import com.dss886.transmis.view.BaseItem;
import com.dss886.transmis.view.SectionItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dss886 on 2017/6/30.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private List<BaseItem> mItems = new ArrayList<>();

    protected Toolbar mToolbar;
    protected LinearLayout mContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mContainer = (LinearLayout) findViewById(R.id.container);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(showToolbarBackIcon());
        getSupportActionBar().setTitle(getToolbarTitle());
        addViews();
        setListeners();
        build();
    }

    protected void addView(BaseItem item) {
        mItems.add(item);
    }

    private void build() {
        for (int i = 0; i < mItems.size(); i++) {
            BaseItem item = mItems.get(i);
            if (i == 0 && item instanceof SectionItem) {
                ((SectionItem) item).hideTopPadding();
            }
            if (i > 0 && item instanceof SectionItem) {
                mItems.get(i - 1).hideDivider();
            }
            mContainer.addView(item);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (BaseItem item : mItems) {
            item.onResume();
        }
    }

    protected abstract int getToolbarTitle();

    protected abstract boolean showToolbarBackIcon();

    protected abstract void addViews();

    protected abstract void setListeners();
}
