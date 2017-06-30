package com.dss886.transmis.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import com.dss886.transmis.mail.MailSender;
import com.dss886.transmis.utils.Settings;
import com.dss886.transmis.utils.Logger;
import com.dss886.transmis.utils.Tags;

import java.util.Locale;

/**
 * Created by dss886 on 2017/6/29.
 */

public class SmsListener extends BroadcastReceiver {

    private static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TEMPLATE_TITLE = "你的备用机收到了一条新短信";
    private static final String TEMPLATE_CONTENT = "电话：%s: \n内容：%s \n\n";

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.d("SMS Received.");
        if (!Settings.is(Tags.SP_GLOBAL_ENABLE, false)) {
            Logger.d("Transmis has been disable!");
            return;
        }
        if (intent.getAction().equals(ACTION_SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    SmsMessage[] messages = new SmsMessage[pdus.length];
                    for (int i = 0; i < messages.length; i++) {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    }
                    Logger.d("Try To Send Mail.");
                    sendMail(messages);
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendMail(SmsMessage[] messages) {
        if (messages == null || messages.length == 0) {
            return;
        }
        MailSender sender = new MailSender("notify@dss886.com", "", "dss886@qq.com");
        StringBuilder sb = new StringBuilder();
        for (SmsMessage message : messages) {
            sb.append(String.format(Locale.CHINA, TEMPLATE_CONTENT,
                    message.getOriginatingAddress(), message.getMessageBody()));
        }
        sender.send(TEMPLATE_TITLE, sb.toString());
    }
}
