package com.dss886.transmis.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.dss886.transmis.R;

/**
 * Created by duansishu on 2018/1/28.
 */

public abstract class BaseRecyclerViewActivity extends BaseActivity {

    protected RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecyclerView = findViewById(R.id.recycler_view);
        init(mRecyclerView);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_base_recycler_view;
    }

    protected abstract void init(RecyclerView recyclerView);
}
