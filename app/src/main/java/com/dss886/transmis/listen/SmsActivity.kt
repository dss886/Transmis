package com.dss886.transmis.listen

import com.dss886.transmis.R
import com.dss886.transmis.base.BaseSwitchActivity
import com.dss886.transmis.filter.FilterActivity
import com.dss886.transmis.filter.FilterType
import com.dss886.transmis.utils.Constants
import com.dss886.transmis.utils.TransmisManager
import com.dss886.transmis.view.*

class SmsActivity : BaseSwitchActivity() {

    override fun getToolbarTitle(): String {
        return getString(R.string.sms_title)
    }

    override fun showToolbarBackIcon(): Boolean {
        return true
    }

    override fun getConfigs(): List<IConfig> {
        return mutableListOf<IConfig>().apply {
            add(SwitchConfig("短信开关", Constants.SP_SMS_ENABLE))
            add(SectionConfig("过滤"))
            add(TextButtonConfig("发件人过滤", showRightArrow = true).apply {
                clickAction = {
                    FilterActivity.start(this@SmsActivity, FilterType.SMS_SENDER)
                }
                resumeAction = getResumeAction(this, FilterType.SMS_SENDER)
            })
            add(TextButtonConfig("关键词过滤", showRightArrow = true).apply {
                clickAction = {
                    FilterActivity.start(this@SmsActivity, FilterType.SMS_KEYWORD)
                }
                resumeAction = getResumeAction(this, FilterType.SMS_KEYWORD)
            })
            add(SectionConfig("可选项"))
            add(SwitchConfig("合并长短信", Constants.SP_SMS_MERGE_LONG_TEXT))
            add(SectionConfig("提醒模版设置"))
            add(EditTextConfig("提醒标题", Constants.SP_SMS_TITLE_REGEX))
            add(EditTextConfig("提醒内容模版", Constants.SP_SMS_CONTENT_REGEX))
            add(InfoConfig(getString(R.string.info_sms_content)))
        }
    }

    private fun getResumeAction(config: TextButtonConfig, type: FilterType): () -> Unit {
        val count = TransmisManager.getFilterCount(type)
        val text = if (count == 0) "无" else "$count 项"
        return {
            config.content = text
        }
    }

}