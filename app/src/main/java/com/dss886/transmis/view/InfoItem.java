package com.dss886.transmis.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.dss886.transmis.R;

/**
 * Created by dss886 on 2017/7/1.
 */

public class InfoItem extends BaseItemView {

    private final TextView mContentView;

    public InfoItem(Context context) {
        super(context);
        View.inflate(getContext(), R.layout.view_info_item, this);
        mContentView = findViewById(R.id.content);

    }

    public InfoItem setContent(String content) {
        mContentView.setText(content);
        return this;
    }

    @Override
    public void onResume() {
        // do nothing
    }
}
