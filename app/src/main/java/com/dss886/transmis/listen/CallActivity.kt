package com.dss886.transmis.listen

import com.dss886.transmis.R
import com.dss886.transmis.base.BaseSwitchActivity
import com.dss886.transmis.filter.FilterActivity
import com.dss886.transmis.filter.FilterType
import com.dss886.transmis.utils.Constants
import com.dss886.transmis.utils.TransmisManager
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
            add(TextButtonConfig("来电过滤", showRightArrow = true).apply {
                clickAction = {
                    FilterActivity.start(this@CallActivity, FilterType.CALL_SENDER)
                }
                resumeAction = getResumeAction(this, FilterType.CALL_SENDER)
            })
            add(SectionConfig("提醒设置"))
            add(EditTextConfig("提醒标题", Constants.SP_CALL_TITLE_REGEX).apply {
                hasDefault = true
            })
            add(EditTextConfig("提醒内容", Constants.SP_CALL_CONTENT_REGEX).apply {
                hasDefault = true
            })
            add(InfoConfig(getString(R.string.info_call_content)))
        }
    }

    @Suppress("SameParameterValue")
    private fun getResumeAction(config: TextButtonConfig, type: FilterType): () -> Unit {
        val count = TransmisManager.getFilterCount(type)
        val text = if (count == 0) "无" else "$count 项"
        return {
            config.content = text
        }
    }

}