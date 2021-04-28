package com.dss886.transmis.plugin.plugin

import android.text.TextUtils
import com.dss886.transmis.plugin.IPlugin
import com.dss886.transmis.plugin.PluginTester
import com.dss886.transmis.utils.Constants
import com.dss886.transmis.utils.Logger
import com.dss886.transmis.utils.OkHttp
import com.dss886.transmis.utils.doAsync
import com.dss886.transmis.view.EditTextConfig
import com.dss886.transmis.view.IConfig
import com.dss886.transmis.view.SectionConfig
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.lang.ref.WeakReference

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

    override fun doNotify(title: String, content: String, tester: WeakReference<PluginTester>?) {
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            return
        }

        val token = mTokenConfig.getSpValue(null)

        doAsync {
            try {
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
                val response = OkHttp.client.newCall(request).execute()
                val responseBody = response.body
                if (responseBody != null) {
                    Logger.d("DingDingPlugin", responseBody.string())
                }
                tester?.get()?.success()
            } catch (e: Exception) {
                tester?.get()?.failure(e)
            }
        }
    }

}