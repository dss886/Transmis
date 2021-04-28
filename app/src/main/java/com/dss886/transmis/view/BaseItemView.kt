package com.dss886.transmis.view

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.dss886.transmis.R

/**
 * Created by dss886 on 2017/7/1.
 */
abstract class BaseItemView(context: Context) : LinearLayout(context) {

    abstract fun onResume()

    fun hideDivider() {
        findViewById<View>(R.id.divider).visibility = View.GONE
    }

}