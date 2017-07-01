package com.dss886.transmis.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.dss886.transmis.R;

/**
 * Created by dss886 on 2017/7/1.
 */

@SuppressLint("ViewConstructor")
public class SectionItem extends BaseItem {

    public SectionItem(Context context, String title) {
        super(context);
        View.inflate(getContext(), R.layout.view_section_item, this);
        TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setText(title);
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
