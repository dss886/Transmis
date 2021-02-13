package com.dss886.transmis.view

import android.content.Context
import android.widget.TextView
import com.dss886.transmis.R

/**
 * Created by dss886 on 2017/7/1.
 */
class InfoItemView(context: Context) : BaseItemView(context) {

    init {
        inflate(getContext(), R.layout.view_info_item, this)
    }

    private val mContentView = findViewById<TextView?>(R.id.content)
    private lateinit var mConfig: InfoConfig

    fun bind(config: InfoConfig): InfoItemView {
        mConfig = config
        mContentView?.text = config.content
        return this
    }

    override fun onResume() {
        // do nothing
    }

}