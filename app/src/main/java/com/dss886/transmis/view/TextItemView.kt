package com.dss886.transmis.view

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.dss886.transmis.R

/**
 * Created by dss886 on 2021/02/11.
 */
class TextItemView(context: Context) : BaseItemView(context) {

    init {
        inflate(getContext(), R.layout.view_text_item, this)
    }

    private val mTitleView: TextView = findViewById(R.id.title)
    private val mContentView: TextView = findViewById(R.id.content)
    private val mArrowView: ImageView = findViewById(R.id.right_arrow)
    private lateinit var mConfig: TextConfig

    fun bind(config: TextConfig): TextItemView {
        mConfig = config
        mTitleView.text = config.title
        mContentView.text = config.content
        if (config.showRightArrow) {
            mArrowView.visibility = VISIBLE
        }
        setOnClickListener {
            mConfig.clickAction?.invoke()
        }
        return this
    }

    override fun onResume() {
        mConfig.resumeAction?.invoke()
        mTitleView.text = mConfig.title
        mContentView.text = mConfig.content
        if (mConfig.showRightArrow) {
            mArrowView.visibility = VISIBLE
        }
    }

}