package com.dss886.transmis.base;

import android.os.Bundle;

import com.dss886.transmis.R;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

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
