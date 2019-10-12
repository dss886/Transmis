package com.dss886.transmis.listen.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import com.dss886.transmis.R;
import com.dss886.transmis.base.App;
import com.dss886.transmis.nofity.DingDingSender;
import com.dss886.transmis.nofity.IftttWebhooksSender;
import com.dss886.transmis.nofity.MailGunSender;
import com.dss886.transmis.nofity.MailSender;
import com.dss886.transmis.nofity.TelegramSender;
import com.dss886.transmis.utils.Logger;
import com.dss886.transmis.utils.Settings;
import com.dss886.transmis.utils.StringUtils;
import com.dss886.transmis.utils.Tags;

import java.util.List;
import java.util.Locale;

/**
 * Created by dss886 on 2017/6/29.
 */

public class SmsListener extends BroadcastReceiver {

    private static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.d("SMS Received.");
        if (!Settings.is(Tags.SP_GLOBAL_ENABLE, false)) {
            Logger.d("SMS Transmis has been disable!");
            return;
        }
        if (ACTION_SMS_RECEIVED.equals(intent.getAction())) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    assert pdus != null;
                    SmsMessage[] messages = new SmsMessage[pdus.length];
                    for (int i = 0; i < messages.length; i++) {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    }
                    Logger.d("Try To Notify.");
                    doNotify(messages);
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    private void doNotify(SmsMessage[] messages) {
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
            String callNumber = messages[0].getOriginatingAddress();
            String smsContent = sb.toString();
            if (tryFilter(callNumber, smsContent)) {
                return;
            }
            content = String.format(Locale.CHINA, contentRegex, callNumber, smsContent);
        } else {
            for (SmsMessage message : messages) {
                String callNumber = message.getOriginatingAddress();
                String smsContent = message.getMessageBody();
                if (tryFilter(callNumber, smsContent)) {
                    continue;
                }
                sb.append(String.format(Locale.CHINA, contentRegex, callNumber, smsContent));
            }
            content = sb.toString();
        }
        if (TextUtils.isEmpty(content)) {
            return;
        }
        if (Settings.is(Tags.SP_SMS_MAIL_ENABLE, false)) {
            new MailSender().send(titleRegex, content);
        }
        if (Settings.is(Tags.SP_SMS_DING_ENABLE, false)) {
            new DingDingSender().send(titleRegex, content);
        }
        if (Settings.is(Tags.SP_SMS_MAILGUN_ENABLE, false)) {
            new MailGunSender().send(titleRegex, content);
        }
        if (Settings.is(Tags.SP_SMS_TELEGRAM_ENABLE, false)) {
            new TelegramSender().send(titleRegex, content);
        }
        if (Settings.is(Tags.SP_SMS_IFTTT_WEBHOOKS_ENABLE, false)) {
            new IftttWebhooksSender().send(titleRegex, content);
        }
    }

    private boolean tryFilter(String callNumber, String content) {
        if (TextUtils.isEmpty(callNumber) || TextUtils.isEmpty(content)) {
            return true;
        }
        String senderString = App.sp.getString(Tags.SP_FILTER_VALUE_SMS_SENDER, null);
        String wordString = App.sp.getString(Tags.SP_FILTER_VALUE_SMS_KEYWORD, null);
        List<String> senderList = StringUtils.parseToList(senderString);
        List<String> wordList = StringUtils.parseToList(wordString);
        for (String number : senderList) {
            if (callNumber.contains(number)) {
                Logger.d("SMS has been filtered by sender: " + callNumber + ", " + content);
                return true;
            }
        }
        for (String word : wordList) {
            if (content.contains(word)) {
                Logger.d("SMS has been filtered by content: " + callNumber + ", " + content);
                return true;
            }
        }
        return false;
    }
}
