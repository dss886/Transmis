package com.dss886.transmis.listen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.text.TextUtils
import com.dss886.transmis.R
import com.dss886.transmis.base.App
import com.dss886.transmis.plugin.PluginManager
import com.dss886.transmis.utils.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by dss886 on 2017/6/29.
 */
class CallBroadcastReceiver : BroadcastReceiver() {

    companion object {
        private const val ACTION_PHONE_STATE = "android.intent.action.PHONE_STATE"
        private const val TAG = "CallBroadcastReceiver"
        private var sCallNumber: String? = null
        private var sRing = false
        private var sReceived = false
        private var sRingTime = 0L
    }

    override fun onReceive(context: Context, intent: Intent) {
        Logger.d(TAG, "Phone State Received.")
        if (!TransmisManager.isGlobalEnable()) {
            Logger.d(TAG, "Call Transmis has been disable by the global switch!")
            return
        }
        if (!TransmisManager.isMissingCallEnable()) {
            Logger.d(TAG, "Call Transmis has been disable by the missing call switch!")
            return
        }
        if (ACTION_PHONE_STATE == intent.action) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            intent.extras?.let { bundle ->
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
                        doNotify(sCallNumber, sRingTime)
                        sRing = false
                        sReceived = false
                        sCallNumber = null
                        sRingTime = 0L
                    }
                }
            }
        }
    }

    private fun doNotify(callNumber: String?, ringTime: Long) {
        if (tryFilter()) {
            return
        }
        val ringTimeStr = ((System.currentTimeMillis() - ringTime) / 1000).toString()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
        val title = App.inst().sp.getString(Constants.SP_CALL_TITLE_REGEX, null) ?: App.inst().getString(R.string.call_title_default)
        val contentRegex = App.inst().sp.getString(Constants.SP_CALL_CONTENT_REGEX, null) ?: App.inst().getString(R.string.call_content_default)
        val content = String.format(Locale.CHINA, contentRegex, callNumber, sdf.format(Date(ringTime)), ringTimeStr)
        Logger.d(TAG, "Try to notify missing call. title=${title}, content=${content.replace("\n", " ")}")
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

    private fun tryFilter(): Boolean {
        val senderString = App.inst().sp.getString(Constants.SP_FILTER_VALUE_CALL_SENDER, null)
        val senderList = senderString?.stringToList() ?: emptyList()
        for (sender in senderList) {
            if (TextUtils.equals(sender, sCallNumber)) {
                Logger.d(TAG, "Call has been filtered: $sCallNumber")
                return true
            }
        }
        return false
    }

}