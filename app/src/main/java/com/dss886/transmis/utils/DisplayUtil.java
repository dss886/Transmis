package com.dss886.transmis.utils;

import android.content.Context;

/**
 * Created by dss886 on 2017/6/29.
 */

public class DisplayUtil {
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
