package com.dss886.transmis.listen

import com.dss886.transmis.R
import com.dss886.transmis.base.App
import com.dss886.transmis.base.BaseSwitchActivity
import com.dss886.transmis.filter.FilterActivity
import com.dss886.transmis.filter.FilterType
import com.dss886.transmis.utils.Constants
import com.dss886.transmis.utils.DialogBuilder
import com.dss886.transmis.utils.TransmisManager
import com.dss886.transmis.utils.countOccurrences
import com.dss886.transmis.view.*
import java.util.*

class SmsActivity : BaseSwitchActivity() {

    private lateinit var mTitleConfig: EditTextConfig
    private lateinit var mContentConfig: EditTextConfig

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
            add(TextConfig("发件人过滤", showRightArrow = true).apply {
                clickAction = {
                    FilterActivity.start(this@SmsActivity, FilterType.SMS_SENDER)
                }
                resumeAction = getResumeAction(this, FilterType.SMS_SENDER)
            })
            add(TextConfig("关键词过滤", showRightArrow = true).apply {
                clickAction = {
                    FilterActivity.start(this@SmsActivity, FilterType.SMS_KEYWORD)
                }
                resumeAction = getResumeAction(this, FilterType.SMS_KEYWORD)
            })
            add(SectionConfig("提醒设置"))
            add(EditTextConfig("提醒标题", Constants.SP_SMS_TITLE_REGEX).apply {
                isRequired = false
                mTitleConfig = this
            })
            add(EditTextConfig("提醒内容", Constants.SP_SMS_CONTENT_REGEX).apply {
                isRequired = false
                isMultiLine = true
                mContentConfig = this
            })
            add(InfoConfig(getString(R.string.info_sms_content)))
            add(SectionConfig("可选项"))
            add(SwitchConfig("合并长短信", Constants.SP_SMS_MERGE_LONG_TEXT))
            add(InfoConfig(getString(R.string.info_sms_merge_long)))
            add(SectionConfig("测试"))
            add(TestConfig("点击进行测试").apply {
                onTest = { doTest(this) }
            })
        }
    }

    private fun getResumeAction(config: TextConfig, type: FilterType): () -> Unit {
        val count = TransmisManager.getFilterCount(type)
        val text = if (count == 0) "无" else "$count 项"
        return {
            config.content = text
        }
    }

    private fun doTest(config: TestConfig) {
        config.content = "测试中"
        val title = mTitleConfig.getSpValue(null) ?: App.inst().getString(R.string.sms_title_default)
        var contentTemplate = mContentConfig.getSpValue(null)
        if (contentTemplate != null && contentTemplate.countOccurrences("%s") != 2) {
            config.content = "测试失败"
            config.reason = IllegalArgumentException("提醒内容参数有误")
            config.onFailure?.invoke()
            return
        }
        contentTemplate = contentTemplate ?: App.inst().getString(R.string.sms_content_default)
        val content = String.format(Locale.CHINA, contentTemplate, "123456789", "smsContent")
        DialogBuilder.showAlertDialog(this, title, content.trim(), null)
        config.content = "测试成功"
        config.onSuccess?.invoke()
    }

}