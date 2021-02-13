package com.dss886.transmis.plugin.plugin

import android.text.TextUtils
import com.dss886.transmis.plugin.IPlugin
import com.dss886.transmis.utils.Logger
import com.dss886.transmis.utils.OkHttp
import com.dss886.transmis.utils.doAsync
import com.dss886.transmis.utils.handleUnified
import com.dss886.transmis.view.EditTextConfig
import com.dss886.transmis.view.IConfig
import com.dss886.transmis.view.SectionConfig
import com.dss886.transmis.view.SwitchConfig
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

private const val URL = "https://oapi.dingtalk.com/robot/send?access_token="

/**
 * Created by dss886 on 2021/02/11.
 */
class DingDingPlugin: IPlugin {

    private val mEnableConfig = SwitchConfig("插件开关", "ding_ding_enable")
    private val mTokenConfig = EditTextConfig("钉钉机器人Token", "ding_ding_token").apply {
        isPassword = true
    }

    override fun getName(): String {
        return "钉钉插件"
    }

    override fun isEnable(): Boolean {
        return mEnableConfig.getSpValue(false)
    }

    override fun getKey(): String {
        return "ding_ding"
    }

    override fun getConfigs(): List<IConfig> {
        return mutableListOf<IConfig>().apply {
            add(mEnableConfig)
            add(SectionConfig("参数设置"))
            add(mTokenConfig)
        }
    }

    override fun doNotify(title: String, content: String) {
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            return
        }

        val token = mTokenConfig.getSpValue(null)

        doAsync {
            try {
                val url = URL + token
                val sb = StringBuilder()
                sb.append(title).append("\n\n")
                for (line in content.split("\n").toTypedArray()) {
                    sb.append("> ").append(line).append("\n\n")
                }
                val message = JSONObject().apply {
                    put("msgtype", "markdown")
                    put("markdown", JSONObject().apply {
                        put("title", title)
                        put("text", sb.toString().trim { it <= ' ' })
                    })
                }
                val mediaType: MediaType = "application/json; charset=utf-8".toMediaType()
                val body = message.toString().toRequestBody(mediaType)
                val request = Request.Builder().url(url).post(body).build()
                val response = OkHttp.client.newCall(request).execute()
                val responseBody = response.body
                if (responseBody != null) {
                    Logger.d("DingDingPlugin", responseBody.string())
                }
            } catch (e: Exception) {
                e.handleUnified()
            }
        }
    }

}