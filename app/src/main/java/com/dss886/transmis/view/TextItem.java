package com.dss886.transmis.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import com.dss886.transmis.R;
import com.dss886.transmis.base.App;
import com.dss886.transmis.utils.DisplayUtil;

/**
 * Created by dss886 on 2017/6/29.
 */

@SuppressLint("ViewConstructor")
public class TextItem extends BaseItem {

    private TextView mContentView;
    private Callback mCallback;

    public TextItem(Context context, String title) {
        this(context, title, null);
    }

    public TextItem(Context context, String title, String content) {
        super(context);
        View.inflate(getContext(), R.layout.view_text_item, this);
        setMinimumHeight(DisplayUtil.dip2px(getContext(), 56));

        TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setText(title);
        mContentView = (TextView) findViewById(R.id.content);
        if (content != null) {
            mContentView.setText(content);
        }
    }

    public TextItem showRightArrow() {
        findViewById(R.id.right_arrow).setVisibility(VISIBLE);
        return this;
    }

    public TextItem setIsPassword(boolean isPassword) {
        if (isPassword) {
            mContentView.setInputType(InputType.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            mContentView.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        return this;
    }

    public TextItem setCallback(Callback callback) {
        this.mCallback = callback;
        return this;
    }

    @Override
    public void onResume() {
        if (mCallback != null) {
            mContentView.setText(mCallback.onResume(App.sp));
        }
    }

    public interface Callback {
        String onResume(SharedPreferences sp);
    }

}
