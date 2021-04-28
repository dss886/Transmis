package com.dss886.transmis.view

import android.content.Context
import android.widget.TextView
import com.dss886.transmis.R

/**
 * Created by dss886 on 2021/02/11.
 */
class SectionItemView(context: Context) : BaseItemView(context) {

    init {
        inflate(getContext(), R.layout.view_section_item, this)
    }

    private val mTitleView: TextView = findViewById(R.id.title)
    private lateinit var mConfig: SectionConfig

    fun bind(config: SectionConfig): SectionItemView {
        mConfig = config
        mTitleView.text = config.title
        return this
    }

    fun hideTopPadding() {
        getChildAt(0)?.setPadding(0, 0, 0, 0)
    }

    override fun onResume() {
        // do nothing
    }

}