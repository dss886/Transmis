package com.dss886.transmis.listen.call

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.text.TextUtils
import com.dss886.transmis.R
import com.dss886.transmis.base.App
import com.dss886.transmis.plugin.PluginManager
import com.dss886.transmis.utils.Logger
import com.dss886.transmis.utils.Settings
import com.dss886.transmis.utils.StringUtils
import com.dss886.transmis.utils.Tags
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by dss886 on 2017/6/29.
 */
class CallListener : BroadcastReceiver() {

    companion object {
        private const val ACTION_PHONE_STATE = "android.intent.action.PHONE_STATE"
        private const val TAG = "CallListener"
        private var sCallNumber: String? = null
        private var sRing = false
        private var sReceived = false
        private var sRingTime = 0L
    }

    override fun onReceive(context: Context, intent: Intent) {
        Logger.d(TAG, "Phone State Received.")
        if (!Settings.`is`(Tags.SP_GLOBAL_ENABLE, false)) {
            Logger.d(TAG, "Call Transmis has been disable!")
            return
        }
        if (ACTION_PHONE_STATE == intent.action) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val bundle = intent.extras
            if (state != null && bundle != null) {
                val callNumber = bundle.getString("incoming_number")
                if (state == TelephonyManager.EXTRA_STATE_RINGING) {
                    sRing = true
                    sReceived = false
                    sCallNumber = callNumber
                    sRingTime = System.currentTimeMillis()
                    Logger.d(TAG, "Call $sCallNumber is ringing.")
                } else if (state == TelephonyManager.EXTRA_STATE_OFFHOOK) {
                    Logger.d(TAG, "Call $sCallNumber is off-hook.")
                    sReceived = true
                } else if (state == TelephonyManager.EXTRA_STATE_IDLE) {
                    if (sRing and !sReceived) {
                        Logger.d(TAG, "Phone missed, try to notify.")
                        doNotify()
                        sRing = false
                        sReceived = false
                        sCallNumber = null
                        sRingTime = 0L
                    }
                }
            }
        }
    }

    private fun doNotify() {
        if (tryFilter()) {
            return
        }
        val ringTime = ((System.currentTimeMillis() - sRingTime) / 1000).toString()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
        val titleRegex = App.sp.getString(Tags.SP_CALL_TITLE_REGEX, null) ?: App.me().getString(R.string.call_title_default)
        val contentRegex = App.sp.getString(Tags.SP_CALL_CONTENT_REGEX, null) ?: App.me().getString(R.string.call_content_default)
        val content = String.format(Locale.CHINA, contentRegex, sCallNumber, sdf.format(Date(sRingTime)), ringTime)
        Logger.d(TAG, "mail content: $content")
        PluginManager.plugins
                .filter { it.isEnable() }
                .apply {
                    Logger.d(TAG, "Plugin enable count = ${this.size}: ${this.joinToString { it.getName() }}")
                }
                .forEach { plugin ->
                    plugin.doNotify(titleRegex, content)
                }
    }

    private fun tryFilter(): Boolean {
        val senderString = App.sp.getString(Tags.SP_FILTER_VALUE_CALL_SENDER, null)
        val senderList = StringUtils.parseToList(senderString)
        for (sender in senderList) {
            if (TextUtils.equals(sender, sCallNumber)) {
                Logger.d(TAG, "Call has been filtered: $sCallNumber")
                return true
            }
        }
        return false
    }

}