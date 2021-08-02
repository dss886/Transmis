package com.dss886.transmis.listen

import com.dss886.transmis.R
import com.dss886.transmis.base.App
import com.dss886.transmis.base.BaseConfigActivity
import com.dss886.transmis.filter.FilterActivity
import com.dss886.transmis.filter.FilterType
import com.dss886.transmis.utils.Constants
import com.dss886.transmis.utils.DialogBuilder
import com.dss886.transmis.utils.TransmisManager
import com.dss886.transmis.utils.countOccurrences
import com.dss886.transmis.view.*
import java.text.SimpleDateFormat
import java.util.*

class CallActivity : BaseConfigActivity() {

    private lateinit var mTitleConfig: EditTextConfig
    private lateinit var mContentConfig: EditTextConfig

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
            add(TextConfig("来电过滤", showRightArrow = true).apply {
                clickAction = {
                    FilterActivity.start(this@CallActivity, FilterType.CALL_SENDER)
                }
                resumeAction = getResumeAction(this, FilterType.CALL_SENDER)
            })
            add(SectionConfig("提醒设置"))
            add(EditTextConfig("提醒标题", Constants.SP_CALL_TITLE_REGEX).apply {
                isRequired = false
                mTitleConfig = this
            })
            add(EditTextConfig("提醒内容", Constants.SP_CALL_CONTENT_REGEX).apply {
                isRequired = false
                isMultiLine = true
                mContentConfig = this
            })
            add(InfoConfig(getString(R.string.info_call_content)))
            add(SectionConfig("测试"))
            add(TestConfig("点击进行测试").apply {
                onTest = { doTest(this) }
            })
        }
    }

    @Suppress("SameParameterValue")
    private fun getResumeAction(config: TextConfig, type: FilterType): () -> Unit {
        val count = TransmisManager.getFilterCount(type)
        val text = if (count == 0) "无" else "$count 项"
        return {
            config.content = text
        }
    }

    private fun doTest(config: TestConfig) {
        config.content = "测试中"
        val title = mTitleConfig.getSpValue(null) ?: App.inst().getString(R.string.call_title_default)
        var contentTemplate = mContentConfig.getSpValue(null)
        if (contentTemplate != null && contentTemplate.countOccurrences("%s") != 3) {
            config.content = "测试失败"
            config.reason = IllegalArgumentException("提醒内容参数有误")
            config.onFailure?.invoke()
            return
        }
        contentTemplate = contentTemplate ?: App.inst().getString(R.string.call_content_default)
        val time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(Date())
        val content = String.format(Locale.CHINA, contentTemplate, "123456789", time, "50")
        DialogBuilder.showAlertDialog(this, title, content.trim(), null)
        config.content = "测试成功"
        config.onSuccess?.invoke()
    }

}