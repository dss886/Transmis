package com.dss886.transmis.base

import android.os.Bundle
import android.widget.LinearLayout
import com.dss886.transmis.R
import com.dss886.transmis.utils.buildView
import com.dss886.transmis.utils.forEachChildren
import com.dss886.transmis.view.BaseItemView
import com.dss886.transmis.view.IConfig
import com.dss886.transmis.view.SectionItemView

/**
 * Created by duansishu on 2018/1/28.
 */
abstract class BaseSwitchActivity : BaseActivity() {

    private lateinit var mContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContainer = findViewById(R.id.container)
        buildView(getConfigs())
    }

    override fun onResume() {
        super.onResume()
        mContainer.forEachChildren {
            (it as? BaseItemView)?.onResume()
        }
    }

    private fun buildView(configs: List<IConfig>) {
        var lastView : BaseItemView? = null
        configs.forEachIndexed { index, config ->
            val view = config.buildView(this)
            if (index == 0 && view is SectionItemView) {
                view.hideTopPadding()
            }
            if (index > 0 && view is SectionItemView) {
                lastView?.hideDivider()
            }
            mContainer.addView(view)
            lastView = view
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_base_switch
    }

    protected abstract fun getConfigs(): List<IConfig>

}