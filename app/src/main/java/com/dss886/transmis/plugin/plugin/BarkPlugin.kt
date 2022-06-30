package com.dss886.transmis.plugin.plugin

import android.text.TextUtils
import com.dss886.transmis.plugin.IPlugin
import com.dss886.transmis.utils.Constants
import com.dss886.transmis.view.EditTextConfig
import com.dss886.transmis.view.IConfig
import com.dss886.transmis.view.SectionConfig
import com.eatthepath.pushy.apns.ApnsClientBuilder
import com.eatthepath.pushy.apns.auth.ApnsSigningKey
import com.eatthepath.pushy.apns.util.SimpleApnsPayloadBuilder
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification
import java.nio.charset.StandardCharsets

/**
 * Created by dss886 on 2021/02/14.
 */
class BarkPlugin: IPlugin {

    private val mDeviceKeyConfig = EditTextConfig("Device Key", "bark_device_key")

    override fun getName(): String {
        return "Bark插件"
    }

    override fun getKey(): String {
        return "bark"
    }

    override fun getConfigs(): List<IConfig> {
        return listOf(
                SectionConfig("参数设置"),
                mDeviceKeyConfig,
        )
    }

    override fun doNotify(title: String, content: String): String? {
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            return null
        }

        val certInputStream = Constants.BARK_CERT.byteInputStream(StandardCharsets.UTF_8)
        val apnsClient = ApnsClientBuilder()
            .setApnsServer(ApnsClientBuilder.PRODUCTION_APNS_HOST)
            .setSigningKey(ApnsSigningKey.loadFromInputStream(certInputStream, Constants.BARK_TEAM_ID, Constants.BARK_APN_KEY))
            .build()
        val deviceToken = mDeviceKeyConfig.getSpValue("")
        val payload = SimpleApnsPayloadBuilder().setSound("1107")
            .setCategoryName("myNotificationCategory")
            .setAlertTitle(title)
            .setAlertBody(content)
            .setMutableContent(true).build()
        val notification = SimpleApnsPushNotification(deviceToken, "me.fin.bark", payload)
        val response = apnsClient.sendNotification(notification).get()
        return if (response.isAccepted) {
            "Notification accepted by the APNs gateway."
        } else {
            "Notification rejected by the APNs gateway: " + response.rejectionReason
        }
    }

}