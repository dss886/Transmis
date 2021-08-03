package com.dss886.transmis.plugin.plugin

import android.text.TextUtils
import com.dss886.transmis.plugin.IPlugin
import com.dss886.transmis.utils.Constants
import com.dss886.transmis.utils.OkHttp
import com.dss886.transmis.utils.getBodyOrThrow
import com.dss886.transmis.view.EditTextConfig
import com.dss886.transmis.view.IConfig
import com.dss886.transmis.view.SectionConfig
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

/**
 * Created by dss886 on 2021/02/11.
 */
class DingDingPlugin: IPlugin {

    private val mTokenConfig = EditTextConfig("钉钉机器人Token", "ding_ding_token").apply {
        isPassword = true
    }

    override fun getName(): String {
        return "钉钉插件"
    }

    override fun getKey(): String {
        return "ding_ding"
    }

    override fun getConfigs(): List<IConfig> {
        return listOf(
                SectionConfig("参数设置"),
                mTokenConfig
        )
    }

    override fun doNotify(title: String, content: String): String? {
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            return null
        }

        val token = mTokenConfig.getSpValue(null)

        val url = Constants.URL_DINGDING + token
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

        return OkHttp.client.newCall(request).execute().getBodyOrThrow()
    }

}