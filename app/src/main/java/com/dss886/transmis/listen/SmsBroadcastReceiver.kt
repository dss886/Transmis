package com.dss886.transmis.listen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.text.TextUtils
import com.dss886.transmis.R
import com.dss886.transmis.base.App
import com.dss886.transmis.plugin.PluginManager
import com.dss886.transmis.utils.*
import java.util.*

/**
 * Created by dss886 on 2017/6/29.
 */
class SmsBroadcastReceiver : BroadcastReceiver() {

    companion object {
        private const val ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED"
        private const val TAG = "SmsBroadcastReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        Logger.d(TAG, "SMS Received.")
        if (!TransmisManager.isGlobalEnable()) {
            Logger.d(TAG, "SMS Transmis has been disable by the global switch!")
            return
        }
        if (!TransmisManager.isSmsEnable()) {
            Logger.d(TAG, "SMS Transmis has been disable by the sms switch!")
            return
        }
        if (ACTION_SMS_RECEIVED == intent.action) {
            intent.extras?.let { bundle ->
                try {
                    val pdus = (bundle["pdus"] as Array<*>)
                    val messages = arrayOfNulls<SmsMessage>(pdus.size)
                    val format = bundle.getString("format")
                    for (i in messages.indices) {
                        messages[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray, format)
                    }
                    doNotify(messages)
                } catch (e: Exception) {
                    Logger.e(TAG, e.message ?: "")
                }
            }
        }
    }

    private fun doNotify(messages: Array<SmsMessage?>?) {
        if (messages == null || messages.isEmpty()) {
            return
        }
        val title = App.inst().sp.getString(Constants.SP_SMS_TITLE_REGEX, null) ?: App.inst().getString(R.string.sms_title_default)
        val contentRegex = App.inst().sp.getString(Constants.SP_SMS_CONTENT_REGEX, null) ?: App.inst().getString(R.string.sms_content_default)
        val content: String
        val sb = StringBuilder()
        content = if (App.inst().sp.getBoolean(Constants.SP_SMS_MERGE_LONG_TEXT, true)) {
            for (message in messages) {
                sb.append(message?.messageBody)
            }
            val callNumber = messages[0]?.originatingAddress
            val smsContent = sb.toString()
            if (tryFilter(callNumber, smsContent)) {
                return
            }
            String.format(Locale.CHINA, contentRegex, callNumber, smsContent)
        } else {
            for (message in messages) {
                val callNumber = message!!.originatingAddress
                val smsContent = message.messageBody
                if (tryFilter(callNumber, smsContent)) {
                    continue
                }
                sb.append(String.format(Locale.CHINA, contentRegex, callNumber, smsContent))
            }
            sb.toString()
        }
        if (TextUtils.isEmpty(content)) {
            return
        }
        Logger.d(TAG, "Try to notify SMS. title=${title}, content=${content.replace("\n", " ")}")
        PluginManager.plugins
                .filter { it.isEnable() }
                .apply {
                    Logger.d(TAG, "Plugin allCount=${PluginManager.plugins.size}, " +
                            "enableCount=${this.size}: ${this.joinToString { it.getName() }}")
                }
                .forEach { plugin ->
                    doAsync {
                        try { plugin.doNotify(title, content) } catch (ignore : Throwable) {}
                    }
                }
    }

    private fun tryFilter(callNumber: String?, content: String): Boolean {
        if (TextUtils.isEmpty(callNumber) || TextUtils.isEmpty(content)) {
            return true
        }
        val senderString = App.inst().sp.getString(Constants.SP_FILTER_VALUE_SMS_SENDER, null)
        val wordString = App.inst().sp.getString(Constants.SP_FILTER_VALUE_SMS_KEYWORD, null)
        val senderList = senderString?.stringToList() ?: emptyList()
        val wordList = wordString?.stringToList() ?: emptyList()
        for (number in senderList) {
            if (callNumber?.contains(number) == true) {
                Logger.d(TAG, "SMS has been filtered by sender: $callNumber, $content")
                return true
            }
        }
        for (word in wordList) {
            if (content.contains(word)) {
                Logger.d(TAG, "SMS has been filtered by content: $callNumber, $content")
                return true
            }
        }
        return false
    }

}