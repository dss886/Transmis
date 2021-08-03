package com.dss886.transmis.plugin.plugin

import android.text.TextUtils
import com.dss886.transmis.plugin.IPlugin
import com.dss886.transmis.utils.*
import com.dss886.transmis.view.*
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*

/**
 * Created by dss886 on 2021/07/14.
 */
class WebHookPlugin: IPlugin {

    private val mUrlConfig = EditTextConfig("请求地址", "custom_webhook_url")
    private val mMethodConfig = SpinnerConfig("Method", listOf("GET", "POST"),
        listOf("get", "post"), "custom_webhook_method").apply {
            defaultValue = "get"
    }
    private val mBodyTypeConfig = SpinnerConfig("Body格式", listOf("x-www-form-urlencoded", "JSON"),
        listOf("form", "json"), "custom_webhook_body_type").apply {
        defaultValue = "form"
    }
    private val mParamsConfig = FormConfig("Url Params", "custom_webhook_params")
    private val mHeadersConfig = FormConfig("Headers", "custom_webhook_headers")
    private val mBodyConfig = FormConfig("Body", "custom_webhook_body")

    override fun getName(): String {
        return "WebHook插件"
    }

    override fun getKey(): String {
        return "custom_webhook"
    }

    override fun getConfigs(): List<IConfig> {
        return listOf(
            SectionConfig("基础设置"),
            mUrlConfig,
            mMethodConfig,
            mBodyTypeConfig,
            mParamsConfig,
            mHeadersConfig,
            mBodyConfig,
            InfoConfig("在value中使用 \$title 和 \$content 来表示标题和内容"),
        )
    }

    override fun doNotify(title: String, content: String): String? {
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            return null
        }

        val url = mUrlConfig.getSpValue("") ?: ""
        val method = mMethodConfig.getSpValue(null) ?: "get"
        val bodyType = mBodyTypeConfig.getSpValue(null) ?: "form"

        val transform: (Pair<String, String>) -> Pair<String, String> = {
            Pair(it.first, it.second.replace("\$title", title).replace("\$content", content))
        }

        val params = (mParamsConfig.getSpValue(null) ?: emptyList()).map(transform)
        val headers = (mHeadersConfig.getSpValue(null) ?: emptyList()).map(transform)
        val body = (mBodyConfig.getSpValue(null) ?: emptyList()).map(transform)

        val urlImpl = "${url}?${params.toUrlParams()}"
        var requestBody = if (bodyType == "form") body.toFormDataBody() else body.toJSONBody()
        if (method == "post" && requestBody == null) {
            requestBody = "".toRequestBody()       // Okhttp要求Post请求一定要有一个Body，什么鬼
        }
        val request = Request.Builder().url(urlImpl).method(method.toUpperCase(Locale.ROOT), requestBody)
        headers.toHeaders()?.let { request.headers(it) }

        return OkHttp.client.newCall(request.build()).execute().getBodyOrThrow()
    }

}