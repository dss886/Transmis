package com.dss886.transmis.view

import android.content.Context
import android.graphics.Typeface.BOLD
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import com.dss886.transmis.R
import com.dss886.transmis.base.BaseActivity
import com.dss886.transmis.utils.DialogBuilder

/**
 * Created by dss886 on 2021/02/11.
 */
class FormItemView(context: Context) : BaseItemView(context) {

    init {
        inflate(getContext(), R.layout.view_form_item, this)
    }

    private val mInflater : LayoutInflater = LayoutInflater.from(context)
    private val mTitleView: TextView = findViewById(R.id.title)
    private val mContainer: LinearLayout = findViewById(R.id.container)
    private val mAddBtn: View = findViewById(R.id.add)
    private lateinit var mConfig: FormConfig
    private lateinit var mParams: MutableList<Pair<String, String>>

    fun bind(config: FormConfig): FormItemView {
        mConfig = config
        mParams = (mConfig.getSpValue(emptyList()) ?: emptyList()).toMutableList()
        if (mParams.isEmpty()) {
            mParams.add(Pair("", ""))
            mConfig.setSpValue(mParams)
        }
        mTitleView.text = config.title
        mContainer.getChildAt(0)?.let { line ->
            line.findViewById<TextView>(R.id.text_key)?.let { text ->
                text.setTypeface(null, BOLD)
                text.text = config.keyName
            }
            line.findViewById<TextView>(R.id.text_value)?.let { text ->
                text.setTypeface(null, BOLD)
                text.text = config.valueName
            }
            line.findViewById<View>(R.id.remove)?.visibility = View.INVISIBLE
        }
        mParams.forEach { pair ->
            addLine(pair.first, pair.second)
        }
        mAddBtn.setOnClickListener {
            mParams.add(Pair("", ""))
            mConfig.setSpValue(mParams)
            addLine("", "")
        }
        return this
    }

    private fun addLine(key: String, value: String) {
        val view = mInflater.inflate(R.layout.view_form_item_line, mContainer, false)
        view.findViewById<TextView>(R.id.text_key)?.apply {
            text = key
            setOnClickListener {
                onTextClick(view, this, text?.toString(), true)
            }
        }
        view.findViewById<TextView>(R.id.text_value)?.apply {
            text = value
            setOnClickListener {
                onTextClick(view, this, text?.toString(), false)
            }
        }
        view.findViewById<View>(R.id.remove)?.apply {
            setOnClickListener {
                if (getValueIndex(view) == 0 && mParams.size == 1) {
                    mParams[0] = Pair("", "")
                    mConfig.setSpValue(mParams)
                    visibility = INVISIBLE
                    view.findViewById<TextView>(R.id.text_key)?.text = ""
                    view.findViewById<TextView>(R.id.text_value)?.text = ""
                } else {
                    mParams.removeAt(getValueIndex(view))
                    mConfig.setSpValue(mParams)
                    mContainer.removeView(view)
                }
                onResume()
            }
        }
        mContainer.addView(view, MATCH_PARENT, WRAP_CONTENT)
        onResume()
    }

    private fun onTextClick(view: View, textView: TextView, text: String?, isKey: Boolean) {
        val activity = context as? BaseActivity ?: return
        val title = "请输入 " + if (isKey) "Key" else "Value"
        val hint = if (isKey) "Key" else "Value"
        val inputType = InputType.TYPE_CLASS_TEXT
        DialogBuilder.showEditTextDialog(activity, title, text, hint, inputType) { content: String ->
            textView.text = content
            val valueIndex = getValueIndex(view)
            if (isKey) {
                mParams[valueIndex] = Pair(content, mParams[valueIndex].second)
            } else {
                mParams[valueIndex] = Pair(mParams[valueIndex].first, content)
            }
            onResume()
            mConfig.setSpValue(mParams)
        }
    }

    private fun getValueIndex(view: View): Int {
        return mContainer.indexOfChild(view) - 1 // minus header
    }

    override fun onResume() {
        mContainer.children.forEachIndexed { index, view ->
            if (index == 1) {
                val isEmpty = mParams[0].first.isEmpty() && mParams[0].second.isEmpty()
                val isOnlyLine = mContainer.childCount == 2
                view.findViewById<View>(R.id.remove)?.visibility = if (isEmpty && isOnlyLine) INVISIBLE else VISIBLE
            } else if (index > 1) {
                view.findViewById<View>(R.id.remove)?.visibility = VISIBLE
            }
        }
    }

}