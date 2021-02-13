package com.dss886.transmis.view

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.dss886.transmis.R

/**
 * Created by dss886 on 2021/02/11.
 */
class TextButtonItemView(context: Context) : BaseItemView(context) {

    init {
        inflate(getContext(), R.layout.view_text_item, this)
    }

    private val mTitleView = findViewById<TextView?>(R.id.title)
    private val mContentView = findViewById<TextView?>(R.id.content)
    private val mArrowView = findViewById<ImageView>(R.id.right_arrow)
    private lateinit var mConfig: TextButtonConfig

    fun bind(config: TextButtonConfig): TextButtonItemView {
        mConfig = config
        mTitleView?.text = config.title
        mContentView?.text = config.content
        if (config.showRightArrow) {
            mArrowView?.visibility = VISIBLE
        }
        setOnClickListener {
            mConfig.clickAction.invoke()
        }
        return this
    }

    override fun onResume() {
        // do nothing
    }

}