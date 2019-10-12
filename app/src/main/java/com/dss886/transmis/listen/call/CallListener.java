package com.dss886.transmis.listen.call;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.dss886.transmis.R;
import com.dss886.transmis.base.App;
import com.dss886.transmis.nofity.DingDingSender;
import com.dss886.transmis.nofity.IftttWebhooksSender;
import com.dss886.transmis.nofity.MailGunSender;
import com.dss886.transmis.nofity.TelegramSender;
import com.dss886.transmis.utils.Settings;
import com.dss886.transmis.nofity.MailSender;
import com.dss886.transmis.utils.Logger;
import com.dss886.transmis.utils.StringUtils;
import com.dss886.transmis.utils.Tags;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by dss886 on 2017/6/29.
 */

public class CallListener extends BroadcastReceiver {

    private static final String ACTION_PHONE_STATE = "android.intent.action.PHONE_STATE";

    private static String sCallNumber = null;
    private static boolean sRing = false;
    private static boolean sReceived = false;
    private static long sRingTime = 0L;

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.d("Phone State Received.");
        if (!Settings.is(Tags.SP_GLOBAL_ENABLE, false)) {
            Logger.d("Call Transmis has been disable!");
            return;
        }
        if (ACTION_PHONE_STATE.equals(intent.getAction())) {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            Bundle bundle = intent.getExtras();
            if (state != null && bundle != null) {
                String callNumber = bundle.getString("incoming_number");
                if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    sRing = true;
                    sReceived = false;
                    sCallNumber = callNumber;
                    sRingTime = System.currentTimeMillis();
                    Logger.d("Call " + sCallNumber + " is ringing.");
                } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                    Logger.d("Call " + sCallNumber + " is off-hook.");
                    sReceived = true;
                } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    if (sRing & !sReceived) {
                        Logger.d("Phone missed, try to notify.");
                        doNotify();
                        sRing = false;
                        sReceived = false;
                        sCallNumber = null;
                        sRingTime = 0L;
                    }
                }
            }
        }
    }

    private void doNotify() {
        if (tryFilter()) {
            return;
        }
        String ringTime = String.valueOf((System.currentTimeMillis() - sRingTime) / 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String titleRegex = App.sp.getString(Tags.SP_CALL_TITLE_REGEX, App.me().getString(R.string.call_title_default));
        String contentRegex = App.sp.getString(Tags.SP_CALL_CONTENT_REGEX, App.me().getString(R.string.call_content_default));
        String content = String.format(Locale.CHINA, contentRegex, sCallNumber, sdf.format(new Date(sRingTime)), ringTime);
        Logger.d("mail content: " + content);
        if (Settings.is(Tags.SP_MISSED_CALL_MAIL_ENABLE, false)) {
            new MailSender().send(titleRegex, content);
        }
        if (Settings.is(Tags.SP_MISSED_CALL_DING_ENABLE, false)) {
            new DingDingSender().send(titleRegex, content);
        }
        if (Settings.is(Tags.SP_MISSED_CALL_MAILGUN_ENABLE, false)) {
            new MailGunSender().send(titleRegex, content);
        }
        if (Settings.is(Tags.SP_MISSED_CALL_TELEGRAM_ENABLE, false)) {
            new TelegramSender().send(titleRegex, content);
        }
        if (Settings.is(Tags.SP_MISSED_CALL_IFTTT_WEBHOOKS_ENABLE, false)) {
            new IftttWebhooksSender().send(titleRegex, content);
        }
    }

    private boolean tryFilter() {
        String senderString = App.sp.getString(Tags.SP_FILTER_VALUE_CALL_SENDER, null);
        List<String> senderList = StringUtils.parseToList(senderString);
        for (String sender : senderList) {
            if (TextUtils.equals(sender, sCallNumber)) {
                Logger.d("Call has been filtered: " + sCallNumber);
                return true;
            }
        }
        return false;
    }

}
