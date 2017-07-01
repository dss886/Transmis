package com.dss886.transmis.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import com.dss886.transmis.R;
import com.dss886.transmis.base.App;
import com.dss886.transmis.mail.MailSender;
import com.dss886.transmis.utils.Logger;
import com.dss886.transmis.utils.Settings;
import com.dss886.transmis.utils.Tags;

import java.util.Locale;

/**
 * Created by dss886 on 2017/6/29.
 */

public class SmsListener extends BroadcastReceiver {

    private static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.d("SMS Received.");
        if (!Settings.is(Tags.SP_GLOBAL_ENABLE, false) ||
                !Settings.is(Tags.SP_SMS_ENABLE, true)) {
            Logger.d("SMS Transmis has been disable!");
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
        String titleRegex = App.sp.getString(Tags.SP_SMS_TITLE_REGEX, App.me().getString(R.string.sms_title_default));
        String contentRegex = App.sp.getString(Tags.SP_SMS_CONTENT_REGEX, App.me().getString(R.string.sms_content_default));
        String content;
        StringBuilder sb = new StringBuilder();
        if (App.sp.getBoolean(Tags.SP_SMS_MERGE_LONG_TEXT, true)) {
            for (SmsMessage message : messages) {
                sb.append(message.getMessageBody());
            }
            content = String.format(Locale.CHINA, contentRegex, messages[0].getOriginatingAddress(), sb.toString());
        } else {
            for (SmsMessage message : messages) {
                sb.append(String.format(Locale.CHINA, contentRegex, message.getOriginatingAddress(), message.getMessageBody()));
            }
            content = sb.toString();
        }
        new MailSender().send(titleRegex, content);
    }
}
