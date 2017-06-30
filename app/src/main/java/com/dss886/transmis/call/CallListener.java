package com.dss886.transmis.call;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import com.dss886.transmis.utils.Settings;
import com.dss886.transmis.mail.MailSender;
import com.dss886.transmis.utils.Logger;
import com.dss886.transmis.utils.Tags;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by dss886 on 2017/6/29.
 */

public class CallListener extends BroadcastReceiver {

    private static final String ACTION_PHONE_STATE = "android.intent.action.PHONE_STATE";
    private static final String TEMPLATE_TITLE = "你的备用机有一个未接电话";
    private static final String TEMPLATE_CONTENT = "电话：%s \n时间：%s \n响铃：%d 秒";

    private static String sCallNumber = null;
    private static boolean sRing = false;
    private static boolean sReceived = false;
    private static long sRingTime = 0L;

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.d("Phone State Received.");
        if (!Settings.is(Tags.SP_MISSED_CALL_ENABLE, false)) {
            Logger.d("Transmis has been disable!");
            return;
        }
        if (intent.getAction().equals(ACTION_PHONE_STATE)) {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if (state != null) {
                Bundle bundle = intent.getExtras();
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
                        Logger.d("Phone missed, try to send mail.");
                        sendMail();
                        sRing = false;
                        sReceived = false;
                        sCallNumber = null;
                        sRingTime = 0L;
                    }
                }
            }
        }
    }

    private void sendMail() {
        long ringTime = (System.currentTimeMillis() - sRingTime) / 1000;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String content = String.format(Locale.CHINA, TEMPLATE_CONTENT, sCallNumber, sdf.format(new Date(sRingTime)), ringTime);
        Logger.d("mail content: " + content);
        new MailSender().send(TEMPLATE_TITLE, content);
    }

}
