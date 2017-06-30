package com.dss886.transmis.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dss886.transmis.R;
import com.dss886.transmis.utils.DisplayUtil;

/**
 * Created by dss886 on 2017/6/29.
 */

public class TextItem extends LinearLayout implements Resumable {

    private TextView mTitle;
    private TextView mContent;

    public TextItem(Context context) {
        super(context);
        init();
    }

    public TextItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.view_text_item, this);
        mTitle = (TextView) findViewById(R.id.title);
        mContent = (TextView) findViewById(R.id.content);
        setMinimumHeight(DisplayUtil.dip2px(getContext(), 56));
    }

    public static TextItem get(Context context, String title, String content) {
        TextItem item = new TextItem(context);
        item.setTitle(title);
        item.setContent(content);
        return item;
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public void setContent(String content) {
        mContent.setText(content);
    }

    @Override
    public void onResume() {

    }
}
