package com.dss886.transmis.viewnew

import android.content.Context
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.dss886.transmis.R
import com.dss886.transmis.base.App
import com.dss886.transmis.view.BaseItemView

/**
 * Created by dss886 on 2017/6/29.
 */
class SwitchItemView(context: Context) : BaseItemView(context) {

    init {
        inflate(getContext(), R.layout.view_switch_item, this)
    }

    private val mTitleView: TextView? = findViewById(R.id.title)
    private val mSwitchView: SwitchCompat? = findViewById(R.id.switcher)
    private lateinit var mConfig: SwitchConfig

    fun bind(config: SwitchConfig): SwitchItemView {
        mConfig = config
        mTitleView?.text = config.title
        mSwitchView?.setOnCheckedChangeListener { _, isChecked: Boolean ->
            val editor = App.sp.edit()
            editor.putBoolean(mConfig.spKey, isChecked)
            editor.apply()
            mConfig.onCheckedChangeListener?.onCheckedChanged(mSwitchView, isChecked)
        }
        return this
    }

    override fun onResume() {
        val isChecked = App.sp.getBoolean(mConfig.spKey, mConfig.defaultValue)
        mSwitchView?.isChecked = isChecked
        mConfig.onCheckedChangeListener?.onCheckedChanged(mSwitchView, isChecked)
    }

}