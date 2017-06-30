package com.dss886.transmis.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dss886.transmis.R;
import com.dss886.transmis.utils.DisplayUtil;

/**
 * Created by dss886 on 2017/6/29.
 */

@SuppressLint("ViewConstructor")
public class DetailItem extends LinearLayout {

    public DetailItem(Context context, String title) {
        super(context);
        View.inflate(getContext(), R.layout.view_right_item, this);
        setMinimumHeight(DisplayUtil.dip2px(getContext(), 56));
        TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setText(title);
    }

}
