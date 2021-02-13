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
import com.dss886.transmis.view.SwitchConfig
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.lang.ref.WeakReference


/**
 * Created by dss886 on 2021/02/14.
 */
class BarkPlugin: IPlugin {

    private val mHostConfig = EditTextConfig("Bark服务域名", "bark_host").apply {
        isRequired = false
    }
    private val mDeviceKeyConfig = EditTextConfig("Device Key", "bark_device_key")
    private val mPostConfig = SwitchConfig("使用兼容的Post请求", "bark_is_post")

    override fun getName(): String {
        return "Bark插件"
    }

    override fun getKey(): String {
        return "bark"
    }

    override fun getConfigs(): List<IConfig> {
        return listOf(
                SectionConfig("参数设置"),
                mHostConfig,
                mDeviceKeyConfig,
                mPostConfig,
        )
    }

    override fun doNotify(title: String, content: String, tester: WeakReference<PluginTester>?) {
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            return
        }

        val host = mHostConfig.getSpValue(null) ?: Constants.URL_BARK
        val key = mDeviceKeyConfig.getSpValue(null)
        val isPost = mPostConfig.getSpValue()

        doAsync {
            try {
                if (isPost) {
                    val message = JSONObject().apply {
                        put("key", key)
                        put("title", title)
                        put("body", content)
                    }
                    val mediaType: MediaType = "application/json; charset=utf-8".toMediaType()
                    val body = message.toString().toRequestBody(mediaType)
                    val request = Request.Builder().url(host).post(body).build()
                    val response = OkHttp.client.newCall(request).execute()
                    val responseBody = response.body
                    if (responseBody != null) {
                        Logger.d("BarkPlugin", responseBody.string())
                    }
                } else {
                    val url = "$host$key/$title/$content"
                    val request = Request.Builder().url(url).get().build()
                    val response = OkHttp.client.newCall(request).execute()
                    val responseBody = response.body
                    if (responseBody != null) {
                        Logger.d("BarkPlugin", responseBody.string())
                    }
                }
                tester?.get()?.success()
            } catch (e: Exception) {
                tester?.get()?.failure(e)
            }
        }
    }

}