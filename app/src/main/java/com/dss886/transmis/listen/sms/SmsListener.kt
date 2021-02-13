package com.dss886.transmis.listen.sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.text.TextUtils
import com.dss886.transmis.R
import com.dss886.transmis.base.App
import com.dss886.transmis.plugin.PluginManager
import com.dss886.transmis.utils.Logger
import com.dss886.transmis.utils.Settings
import com.dss886.transmis.utils.StringUtils
import com.dss886.transmis.utils.Tags
import java.util.*

/**
 * Created by dss886 on 2017/6/29.
 */
class SmsListener : BroadcastReceiver() {

    companion object {
        private const val ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED"
        private const val TAG = "SmsListener"
    }

    override fun onReceive(context: Context, intent: Intent) {
        Logger.d(TAG, "SMS Received.")
        if (!Settings.`is`(Tags.SP_GLOBAL_ENABLE, false)) {
            Logger.d(TAG, "SMS Transmis has been disable!")
            return
        }
        if (ACTION_SMS_RECEIVED == intent.action) {
            val bundle = intent.extras
            if (bundle != null) {
                try {
                    val pdus = (bundle["pdus"] as Array<Any>?)!!
                    val messages = arrayOfNulls<SmsMessage>(pdus.size)
                    for (i in messages.indices) {
                        messages[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                    }
                    Logger.d(TAG, "Try To Notify.")
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
        val titleRegex = App.sp.getString(Tags.SP_SMS_TITLE_REGEX, null) ?: App.me().getString(R.string.sms_title_default)
        val contentRegex = App.sp.getString(Tags.SP_SMS_CONTENT_REGEX, null) ?: App.me().getString(R.string.sms_content_default)
        val content: String
        val sb = StringBuilder()
        content = if (App.sp.getBoolean(Tags.SP_SMS_MERGE_LONG_TEXT, true)) {
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
        PluginManager.plugins
                .filter { it.isEnable() }
                .apply {
                    Logger.d(TAG, "Plugin enable count = ${this.size}: ${this.joinToString { it.getName() }}")
                }
                .forEach { plugin ->
                    plugin.doNotify(titleRegex, content)
                }
    }

    private fun tryFilter(callNumber: String?, content: String): Boolean {
        if (TextUtils.isEmpty(callNumber) || TextUtils.isEmpty(content)) {
            return true
        }
        val senderString = App.sp.getString(Tags.SP_FILTER_VALUE_SMS_SENDER, null)
        val wordString = App.sp.getString(Tags.SP_FILTER_VALUE_SMS_KEYWORD, null)
        val senderList = StringUtils.parseToList(senderString)
        val wordList = StringUtils.parseToList(wordString)
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