package com.dss886.transmis.view

import android.content.Context
import android.text.InputType
import android.view.View
import android.view.View.OnClickListener
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.dss886.transmis.R
import com.dss886.transmis.base.BaseActivity
import com.dss886.transmis.utils.DialogBuilder

/**
 * Created by dss886 on 2021/02/11.
 */
class EditTextItemView(context: Context): BaseItemView(context), OnClickListener {

    init {
        inflate(getContext(), R.layout.view_text_item, this)
    }

    private val mTitleView = findViewById<TextView?>(R.id.title)
    private val mContentView = findViewById<TextView?>(R.id.content)

    private lateinit var mConfig: EditTextConfig

    fun bind(config: EditTextConfig): EditTextItemView {
        mConfig = config
        mTitleView?.text = config.title
        mContentView.inputType = InputType.TYPE_CLASS_TEXT
        if (config.isPassword) {
            mContentView.inputType = mContentView.inputType or EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
        }
        setOnClickListener(this)
        return this
    }

    override fun onClick(v: View?) {
        val value = mConfig.getSpValue(null)
        val activity = context as? BaseActivity
        val editTitle = "设置" + mConfig.title
        DialogBuilder.showEditTextDialog(activity, editTitle, value, mContentView.inputType) { content: String? ->
            mConfig.setSpValue(content)
            onResume()
        }
    }

    override fun onResume() {
        mContentView?.text = mConfig.getSpValue("未设置")
    }

}