package com.dss886.transmis.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import com.dss886.transmis.R;
import com.dss886.transmis.view.Resumable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dss886 on 2017/6/30.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private List<Resumable> mResumables = new ArrayList<>();

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
    }

    protected void addView(View view) {
        mContainer.addView(view);
        if (view instanceof Resumable) {
            mResumables.add((Resumable) view);
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
        for (Resumable resumable : mResumables) {
            resumable.onResume();
        }
    }

    protected abstract int getToolbarTitle();

    protected abstract boolean showToolbarBackIcon();

    protected abstract void addViews();

    protected abstract void setListeners();
}
