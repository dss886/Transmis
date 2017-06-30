package com.dss886.transmis.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dss886.transmis.R;
import com.dss886.transmis.base.App;
import com.dss886.transmis.utils.DisplayUtil;

/**
 * Created by dss886 on 2017/6/29.
 */

@SuppressLint("ViewConstructor")
public class TextItem extends LinearLayout implements Resumable {

    private final TextView mContentView;
    private String mKey;
    private OnValueChangeListener mListener;

    public TextItem(Context context, String title, String key, OnValueChangeListener listener) {
        super(context);
        this.mKey = key;
        this.mListener = listener;
        View.inflate(getContext(), R.layout.view_text_item, this);
        setMinimumHeight(DisplayUtil.dip2px(getContext(), 56));

        TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setText(title);
        mContentView = (TextView) findViewById(R.id.content);
    }

    @Override
    public void onResume() {
        if (mListener != null) {
            mContentView.setText(mListener.onChange(App.me().sp.getString(mKey, null)));
        }
    }

    public interface OnValueChangeListener {
        String onChange(String value);
    }

}
