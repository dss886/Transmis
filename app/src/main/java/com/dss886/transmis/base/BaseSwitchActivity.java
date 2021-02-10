package com.dss886.transmis.base;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.dss886.transmis.R;
import com.dss886.transmis.view.BaseItem;
import com.dss886.transmis.view.SectionItem;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created by duansishu on 2018/1/28.
 */

public abstract class BaseSwitchActivity extends BaseActivity {

    private List<BaseItem> mItems = new ArrayList<>();
    protected LinearLayout mContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContainer = findViewById(R.id.container);
        addItems();
        setListeners();
        build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (BaseItem item : mItems) {
            item.onResume();
        }
    }

    protected void addItem(BaseItem item) {
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
    protected int getLayout() {
        return R.layout.activity_base_switch;
    }

    protected abstract void addItems();

    protected abstract void setListeners();
}
