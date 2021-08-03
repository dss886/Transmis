package com.dss886.transmis.plugin.plugin

import android.text.TextUtils
import com.dss886.transmis.plugin.IPlugin
import com.dss886.transmis.utils.Constants
import com.dss886.transmis.utils.OkHttp
import com.dss886.transmis.utils.getBodyOrThrow
import com.dss886.transmis.view.EditTextConfig
import com.dss886.transmis.view.IConfig
import com.dss886.transmis.view.SectionConfig
import com.dss886.transmis.view.SwitchConfig
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


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

    override fun doNotify(title: String, content: String): String? {
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            return null
        }

        val host = mHostConfig.getSpValue(null) ?: Constants.URL_BARK
        val key = mDeviceKeyConfig.getSpValue(null)
        val isPost = mPostConfig.getSpValue(mPostConfig.defaultValue)

        val request = if (isPost) {
            val message = JSONObject().apply {
                put("key", key)
                put("title", title)
                put("body", content)
            }
            val mediaType: MediaType = "application/json; charset=utf-8".toMediaType()
            val body = message.toString().toRequestBody(mediaType)
            Request.Builder().url(host).post(body).build()
        } else {
            val url = "$host$key/$title/$content"
            Request.Builder().url(url).get().build()
        }

        return OkHttp.client.newCall(request).execute().getBodyOrThrow()
    }

}