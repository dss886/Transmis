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

private const val URL = "https://api.mailgun.net/v3/"
private const val BOUNDARY = "----WebKitFormBoundary7MA4YWxkTrZu0gW"

/**
 * Created by dss886 on 2021/02/11.
 */
class MailGunPlugin: IPlugin {

    private val mEnableConfig = SwitchConfig("插件开关", "mailgun_enable")
    private val mKeyConfig = EditTextConfig("密钥 Key", "mailgun_key")
    private val mDomainConfig = EditTextConfig("发件人邮箱域名", "mailgun_domain")
    private val mSendNameConfig = EditTextConfig("发件人昵称", "mailgun_name")
    private val mSendMailConfig = EditTextConfig("发件人邮箱", "mailgun_send_mail")
    private val mReceiveMailConfig = EditTextConfig("收件人邮箱", "mailgun_receive_mail")

    override fun getName(): String {
        return "MailGun插件"
    }

    override fun isEnable(): Boolean {
        return mEnableConfig.getSpValue(false)
    }

    override fun getSpKeyPrefix(): String {
        return "mailgun_"
    }

    override fun getConfigs(): List<IConfig> {
        return mutableListOf<IConfig>().apply {
            add(mEnableConfig)
            add(SectionConfig("服务器设置"))
            add(mKeyConfig)
            add(mDomainConfig)
            add(SectionConfig("发件人设置"))
            add(mSendNameConfig)
            add(mSendMailConfig)
            add(SectionConfig("收件人设置"))
            add(mReceiveMailConfig)
        }
    }

    override fun doNotify(title: String, content: String) {
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            return
        }

        val key = mKeyConfig.getSpValue(null)
        val domain = mDomainConfig.getSpValue(null)
        val sendName = mSendNameConfig.getSpValue(null)
        val sendMail = mSendMailConfig.getSpValue(null)
        val toMail = mReceiveMailConfig.getSpValue(null)

        doAsync {
            try {
                val url = "$URL$domain/messages"
                var text = "--$BOUNDARY\n"
                text += (
                        "Content-Disposition: form-data; name=\"from\"\r\n\r\n" +
                                sendName + "<" + sendMail + ">" + "\r\n" +
                                "--" + BOUNDARY + "\r\n" +
                                "Content-Disposition: form-data; name=\"to\"\r\n\r\n" +
                                toMail + "\r\n" +
                                "--" + BOUNDARY + "\r\n" +
                                "Content-Disposition: form-data; name=\"subject\"\r\n\r\n" +
                                title + "\r\n" +
                                "--" + BOUNDARY + "\r\n" +
                                "Content-Disposition: form-data; name=\"text\"\r\n\r\n" +
                                content + "\r\n" +
                                "--" + BOUNDARY + "--\r\n"
                )
                val mediaType: MediaType = "multipart/form-data; boundary=$BOUNDARY".toMediaType()
                val body = text.toRequestBody(mediaType)
                val request = Request.Builder()
                        .url(url)
                        .post(body)
                        .addHeader("content-type", "multipart/form-data; boundary=$BOUNDARY")
                        .addHeader("authorization", "Basic $key")
                        .addHeader("cache-control", "no-cache")
                        .build()
                val response: Response = OkHttp.client.newCall(request).execute()
                val responseBody = response.body
                if (responseBody != null) {
                    Logger.d("MailGunPlugin: $url")
                    Logger.d("MailGunPlugin: " + responseBody.string())
                }
            } catch (e: Exception) {
                App.mainHandler.post {
                    Toast.makeText(App.me(), e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}