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
import okhttp3.Response
import org.json.JSONObject
import java.lang.ref.WeakReference

/**
 * Created by dss886 on 2021/02/11.
 */

class IFTTTPlugin: IPlugin {

    private val mKeyConfig = EditTextConfig("Key", "ifttt_webhooks_key")
    private val mEventConfig = EditTextConfig("Event", "ifttt_webhooks_event")

    override fun getName(): String {
        return "IFTTT插件"
    }

    override fun getKey(): String {
        return "ifttt_webhooks"
    }

    override fun getConfigs(): List<IConfig> {
        return listOf(
                SectionConfig("参数设置"),
                mKeyConfig,
                mEventConfig,
        )
    }

    override fun doNotify(title: String, content: String, tester: WeakReference<PluginTester>?) {
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
                val url = "${Constants.URL_IFTTT}$event/with/key/$key"
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
                    Logger.d("IftttWebhooksPlugin", url)
                    Logger.d("IftttWebhooksPlugin", responseBody.string())
                }
                tester?.get()?.success()
            } catch (e: Exception) {
                tester?.get()?.failure(e)
            }
        }
    }

}