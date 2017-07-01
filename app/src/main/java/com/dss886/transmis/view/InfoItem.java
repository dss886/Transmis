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
public class InfoItem extends BaseItem {

    public InfoItem(Context context, String content) {
        super(context);
        View.inflate(getContext(), R.layout.view_info_item, this);
        TextView contentView = (TextView) findViewById(R.id.content);
        contentView.setText(content);
    }

    @Override
    public void onResume() {
        // do nothing
    }
}
