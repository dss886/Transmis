package com.dss886.transmis.view

import android.content.Context
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.dss886.transmis.R

/**
 * Created by dss886 on 2017/6/29.
 */
class SwitchItemView(context: Context) : BaseItemView(context) {

    init {
        inflate(getContext(), R.layout.view_switch_item, this)
    }

    private val mTitleView: TextView = findViewById(R.id.title)
    private val mSwitchView: SwitchCompat = findViewById(R.id.switcher)
    lateinit var mConfig: SwitchConfig

    fun bind(config: SwitchConfig): SwitchItemView {
        mConfig = config
        mTitleView.text = config.title
        mSwitchView.setOnCheckedChangeListener { _, isChecked ->
            mConfig.setSpValue(isChecked)
            onResume()
        }
        return this
    }

    override fun onResume() {
        val isChecked = mConfig.getSpValue()
        mConfig.onCheckedChangeListener?.onCheckedChanged(mSwitchView, isChecked)
        mSwitchView.isChecked = isChecked
        mTitleView.text = mConfig.title
    }

}