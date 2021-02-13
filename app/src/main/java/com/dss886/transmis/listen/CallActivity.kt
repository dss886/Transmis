package com.dss886.transmis.listen

import com.dss886.transmis.R
import com.dss886.transmis.base.BaseSwitchActivity
import com.dss886.transmis.filter.FilterActivity
import com.dss886.transmis.utils.Constants
import com.dss886.transmis.view.*

class CallActivity : BaseSwitchActivity() {

    override fun getToolbarTitle(): String {
        return getString(R.string.call_title)
    }

    override fun showToolbarBackIcon(): Boolean {
        return true
    }

    override fun getConfigs(): List<IConfig> {
        return mutableListOf<IConfig>().apply {
            add(SwitchConfig("未接电话开关", Constants.SP_MISSED_CALL_ENABLE))
            add(SectionConfig("过滤"))
            // TODO: 2021/02/11 @duansishu show filter count outside
            add(TextButtonConfig("来电过滤", showRightArrow = true) {
                FilterActivity.start(this@CallActivity, FilterActivity.Type.CALL_SENDER)
            })
            add(SectionConfig("提醒模版设置"))
            add(EditTextConfig("提醒标题", Constants.SP_CALL_TITLE_REGEX))
            add(EditTextConfig("提醒内容模版", Constants.SP_CALL_CONTENT_REGEX))
            add(InfoConfig(getString(R.string.info_call_content)))
        }
    }

}