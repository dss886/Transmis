package com.dss886.transmis.view

import android.content.Context
import android.text.InputType
import android.text.TextUtils
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

    private val mTitleView: TextView = findViewById(R.id.title)
    private val mContentView: TextView = findViewById(R.id.content)
    private val mRequireView: TextView = findViewById(R.id.require)

    private lateinit var mConfig: EditTextConfig

    fun bind(config: EditTextConfig): EditTextItemView {
        mConfig = config
        mTitleView.text = config.title
        mRequireView.visibility = if (config.isRequired) VISIBLE else INVISIBLE
        setOnClickListener(this)
        return this
    }

    override fun onClick(v: View?) {
        val content = mConfig.getSpValue(null)
        val hint = if (mConfig.isRequired) "" else "默认"
        (context as? BaseActivity)?.let { activity ->
            val editTitle = "设置" + mConfig.title
            DialogBuilder.showEditTextDialog(activity, editTitle, content, hint, inputType = mContentView.inputType) { content: String? ->
                if (TextUtils.isEmpty(content)) {
                    mConfig.setSpValue(null)
                } else {
                    mConfig.setSpValue(content)
                }
                onResume()
            }
        }
    }

    override fun onResume() {
        val defaultContent = if (mConfig.isRequired) "未设置" else "默认"
        val content = mConfig.getSpValue(null)
        if (mConfig.isPassword && !TextUtils.isEmpty(content)) {
            mContentView.text = content ?: defaultContent
            mContentView.inputType = InputType.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
        } else {
            mContentView.text = content ?: defaultContent
            mContentView.inputType = InputType.TYPE_CLASS_TEXT
        }
    }

}