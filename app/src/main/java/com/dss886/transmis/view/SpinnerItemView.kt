package com.dss886.transmis.view

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSpinner
import com.dss886.transmis.R

/**
 * Created by dss886 on 2021/02/11.
 */
class SpinnerItemView(context: Context) : BaseItemView(context), AdapterView.OnItemSelectedListener {

    init {
        inflate(getContext(), R.layout.view_spinner_item, this)
    }

    private val mTitleView: TextView = findViewById(R.id.title)
    private val mSpinner: AppCompatSpinner= findViewById(R.id.spinner)
    private lateinit var mConfig: SpinnerConfig

    fun bind(config: SpinnerConfig): SpinnerItemView {
        mConfig = config
        mTitleView.text = config.title
        mSpinner.apply {
            adapter = ArrayAdapter(context, R.layout.view_spinner_select, config.textList).apply {
                setDropDownViewResource(R.layout.view_spinner_drop)
            }
            config.valueList.indexOf(config.getSpValue(config.defaultValue)).takeIf { it > -1 }?.let {
                setSelection(it)
            }
            onItemSelectedListener = this@SpinnerItemView
        }
        return this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        mConfig.setSpValue(mConfig.valueList[position])
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // do noting
    }

    override fun onResume() {
        // do nothing
    }

}