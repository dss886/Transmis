package com.dss886.transmis.plugin.plugin

import android.text.TextUtils
import android.widget.Toast
import com.dss886.transmis.base.App
import com.dss886.transmis.plugin.IPlugin
import com.dss886.transmis.utils.Logger
import com.dss886.transmis.utils.OkHttp
import com.dss886.transmis.utils.doAsync
import com.dss886.transmis.viewnew.EditTextConfig
import com.dss886.transmis.viewnew.IConfig
import com.dss886.transmis.viewnew.SectionConfig
import com.dss886.transmis.viewnew.SwitchConfig
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject

/**
 * Created by dss886 on 2021/02/11.
 */

private const val URL: String = "https://maker.ifttt.com/trigger/"

class IFTTTPlugin: IPlugin {

    private val mEnableConfig = SwitchConfig("插件开关", "ifttt_webhooks__enable")
    private val mKeyConfig = EditTextConfig("完整的接口Url", "ifttt_webhooks_key")
    private val mEventConfig = EditTextConfig("完整的接口Url", "ifttt_webhooks_event")

    override fun getName(): String {
        return "IFTTT插件"
    }

    override fun isEnable(): Boolean {
        return mEnableConfig.getSpValue(false)
    }

    override fun getSpKeyPrefix(): String {
        return "ifttt_webhooks_"
    }

    override fun getConfigs(): List<IConfig> {
        return mutableListOf<IConfig>().apply {
            add(mEnableConfig)
            add(SectionConfig("参数设置"))
            add(mKeyConfig)
            add(mEventConfig)
        }
    }

    override fun doNotify(title: String, content: String) {
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            return
        }

        val key = mKeyConfig.getSpValue(null)
        val event = mEventConfig.getSpValue(null)

        doAsync {
            try {
                val message = JSONObject().apply {
                    put("value1", "transmis")
                    put("value2", title)
                    put("value3", content)
                }
                val url = "$URL$event/with/key/$key"
                val mediaType: MediaType = "application/json".toMediaType()
                val body = message.toString().toRequestBody(mediaType)
                val request = Request.Builder()
                        .url(url)
                        .post(body)
                        .addHeader("content-type", "application/json")
                        .addHeader("cache-control", "no-cache")
                        .build()
                val response: Response = OkHttp.client.newCall(request).execute()
                val responseBody = response.body
                if (responseBody != null) {
                    Logger.d("IftttWebhooksPlugin: $url")
                    Logger.d("IftttWebhooksPlugin: " + responseBody.string())
                }
            } catch (e: Exception) {
                App.mainHandler.post {
                    Toast.makeText(App.me(), e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}