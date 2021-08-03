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

/**
 * Created by dss886 on 2021/02/11.
 */
class MailGunPlugin: IPlugin {

    private val mKeyConfig = EditTextConfig("密钥 Key", "mailgun_key")
    private val mDomainConfig = EditTextConfig("发件人邮箱域名", "mailgun_domain")
    private val mSendNameConfig = EditTextConfig("发件人昵称", "mailgun_name").apply {
        isRequired = false
    }
    private val mSendMailConfig = EditTextConfig("发件人邮箱", "mailgun_send_mail")
    private val mReceiveMailConfig = EditTextConfig("收件人邮箱", "mailgun_receive_mail")

    override fun getName(): String {
        return "MailGun插件"
    }

    override fun getKey(): String {
        return "mailgun"
    }

    override fun getConfigs(): List<IConfig> {
        return listOf(
                SectionConfig("服务器设置"),
                mKeyConfig,
                mDomainConfig,
                SectionConfig("发件人设置"),
                mSendNameConfig,
                mSendMailConfig,
                SectionConfig("收件人设置"),
                mReceiveMailConfig,
        )
    }

    override fun doNotify(title: String, content: String): String? {
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            return null
        }

        val key = mKeyConfig.getSpValue(null)
        val domain = mDomainConfig.getSpValue(null)
        val sendName = mSendNameConfig.getSpValue(null) ?: "Transmis"
        val sendMail = mSendMailConfig.getSpValue(null)
        val toMail = mReceiveMailConfig.getSpValue(null)

        val url = "${Constants.URL_MAILGUN}$domain/messages"
        var text = "--${Constants.BOUNDARY_WEBKIT}\n"
        text += (
                "Content-Disposition: form-data; name=\"from\"\r\n\r\n" +
                        sendName + "<" + sendMail + ">" + "\r\n" +
                        "--" + Constants.BOUNDARY_WEBKIT + "\r\n" +
                        "Content-Disposition: form-data; name=\"to\"\r\n\r\n" +
                        toMail + "\r\n" +
                        "--" + Constants.BOUNDARY_WEBKIT + "\r\n" +
                        "Content-Disposition: form-data; name=\"subject\"\r\n\r\n" +
                        title + "\r\n" +
                        "--" + Constants.BOUNDARY_WEBKIT + "\r\n" +
                        "Content-Disposition: form-data; name=\"text\"\r\n\r\n" +
                        content + "\r\n" +
                        "--" + Constants.BOUNDARY_WEBKIT + "--\r\n"
                )
        val mediaType: MediaType = "multipart/form-data; boundary=${Constants.BOUNDARY_WEBKIT}".toMediaType()
        val body = text.toRequestBody(mediaType)
        val request = Request.Builder()
            .url(url)
            .post(body)
            .addHeader("content-type", "multipart/form-data; boundary=${Constants.BOUNDARY_WEBKIT}")
            .addHeader("authorization", "Basic $key")
            .addHeader("cache-control", "no-cache")
            .build()

        return OkHttp.client.newCall(request).execute().getBodyOrThrow()
    }

}