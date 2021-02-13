package com.dss886.transmis.plugin.plugin

import android.text.TextUtils
import com.dss886.transmis.plugin.IPlugin
import com.dss886.transmis.utils.doAsync
import com.dss886.transmis.utils.handleUnified
import com.dss886.transmis.view.EditTextConfig
import com.dss886.transmis.view.IConfig
import com.dss886.transmis.view.SectionConfig
import com.dss886.transmis.view.SwitchConfig
import com.sun.mail.util.MailSSLSocketFactory
import java.util.*
import javax.mail.Address
import javax.mail.Message
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

/**
 * Created by dss886 on 2021/02/11.
 */
class MailPlugin: IPlugin {

    private val mEnableConfig = SwitchConfig("插件开关", "mail_enable")
    private val mHostConfig = EditTextConfig("SMTP服务器", "mail_host")
    private val mPortConfig = EditTextConfig("端口号", "mail_port")
    private val mNameConfig = EditTextConfig("发件人昵称", "mail_send_name")
    private val mEmailConfig = EditTextConfig("发件人邮箱", "mail_send_mail")
    private val mPasswordConfig = EditTextConfig("发件人密码/授权码", "mail_send_password").apply {
        isPassword = true
    }
    private val mReceiverConfig = EditTextConfig("收件人邮箱", "mail_receive_mail")

    override fun getName(): String {
        return "邮件插件"
    }

    override fun isEnable(): Boolean {
        return mEnableConfig.getSpValue(false)
    }

    override fun getKey(): String {
        return "mail"
    }

    override fun getConfigs(): List<IConfig> {
        return mutableListOf<IConfig>().apply {
            add(mEnableConfig)
            add(SectionConfig("服务器设置"))
            add(mHostConfig)
            add(mPortConfig)
            add(SectionConfig("发件人设置"))
            add(mNameConfig)
            add(mEmailConfig)
            add(mPasswordConfig)
            add(SectionConfig("收件人设置"))
            add(mReceiverConfig)
        }
    }

    override fun doNotify(title: String, content: String) {
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            return
        }

        val host = mHostConfig.getSpValue(null)
        val port = mPortConfig.getSpValue(null)
        val name = mNameConfig.getSpValue(null)
        val email = mEmailConfig.getSpValue(null)
        val password = mPasswordConfig.getSpValue(null)
        val receiver = mReceiverConfig.getSpValue(null)

        doAsync {
            try {
                val props = Properties().apply {
                    put("mail.transport.protocol", "smtp")
                    put("mail.host", host)
                    put("mail.smtp.auth", "true")
                    put("mail.smtp.port", port)
                    put("mail.smtp.socketFactory.port", port)
                    put("mail.smtp.ssl.enable", "true")
                    put("mail.smtp.ssl.socketFactory", MailSSLSocketFactory().apply {
                        isTrustAllHosts = true
                    })
                }
                val session = Session.getDefaultInstance(props, null)
                val msg: Message = MimeMessage(session).apply {
                    subject = title
                    setText(content)
                    setFrom(InternetAddress(email, name))
                }
                val transport: Transport = session.transport
                transport.connect(host, email, password)
                transport.sendMessage(msg, arrayOf<Address>(InternetAddress(receiver)))
                transport.close()
            } catch (e: Throwable) {
                e.handleUnified()
            }
        }
    }

}