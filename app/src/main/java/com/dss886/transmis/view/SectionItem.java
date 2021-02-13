package com.dss886.transmis.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.dss886.transmis.R;

/**
 * Created by dss886 on 2017/7/1.
 */

public class SectionItem extends BaseItemView {

    private final TextView mTitleView;

    public SectionItem(Context context) {
        super(context);
        View.inflate(getContext(), R.layout.view_section_item, this);
        mTitleView = findViewById(R.id.title);
    }

    public SectionItem setTitle(String title) {
        mTitleView.setText(title);
        return this;
    }

    public void hideTopPadding() {
        View root = getChildAt(0);
        if (root != null) {
            root.setPadding(0, 0, 0,0);
        }
    }

    @Override
    public void onResume() {
        // do nothing
    }
}
