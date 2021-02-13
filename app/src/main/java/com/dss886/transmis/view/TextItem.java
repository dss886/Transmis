package com.dss886.transmis.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import com.dss886.transmis.R;
import com.dss886.transmis.base.App;

import androidx.annotation.Nullable;

/**
 * Created by dss886 on 2017/6/29.
 */

public class TextItem extends BaseItemView {

    private final TextView mTitleView;
    private final TextView mContentView;
    private Callback mCallback;

    public TextItem(Context context) {
        super(context);
        View.inflate(getContext(), R.layout.view_text_item, this);

        mTitleView = findViewById(R.id.title);
        mContentView = findViewById(R.id.content);
    }

    public TextItem setTitle(String title) {
        mTitleView.setText(title);
        return this;
    }

    public TextItem setContent(String content) {
        mContentView.setText(content);
        return this;
    }

    public TextItem showRightArrow() {
        findViewById(R.id.right_arrow).setVisibility(VISIBLE);
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
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

    public TextItem setOnClick(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
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
