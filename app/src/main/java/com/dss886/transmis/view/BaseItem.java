package com.dss886.transmis.view;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import com.dss886.transmis.R;

/**
 * Created by dss886 on 2017/7/1.
 */

public abstract class BaseItem extends LinearLayout {

    public BaseItem(Context context) {
        super(context);
    }

    public abstract void onResume();

    public void hideDivider() {
        View divider = findViewById(R.id.divider);
        if (divider != null) {
            divider.setVisibility(GONE);
        }
    }
}
