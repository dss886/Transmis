package com.dss886.transmis.utils;

/**
 * Created by dss886 on 2017/6/30.
 */

public class Constants {
    public static final String URL_README = "https://github.com/dss886/Transmis/blob/master/README.md";
    public static final String URL_LICENSE = "https://github.com/dss886/Transmis/blob/master/LICENSE";
    public static final String URL_RELEASE = "https://github.com/dss886/Transmis/releases";
    public static final String URL_DINGDING = "https://oapi.dingtalk.com/robot/send?access_token=";
    public static final String URL_MAILGUN = "https://api.mailgun.net/v3/";
    public static final String URL_IFTTT = "https://maker.ifttt.com/trigger/";
    public static final String URL_BARK = "https://api.day.app/";

    public static final String BOUNDARY_WEBKIT = "----WebKitFormBoundary7MA4YWxkTrZu0gW";

    /**
     * Cert info from: https://github.com/Finb/bark-server/blob/master/deploy/AuthKey_LH4T9V5U4R_5U8LBRXG3A.p8
     */
    public static final String BARK_APN_KEY = "LH4T9V5U4R";
    public static final String BARK_TEAM_ID = "5U8LBRXG3A";
    public static final String BARK_CERT = "-----BEGIN PRIVATE KEY-----\n" +
            "MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQg4vtC3g5L5HgKGJ2+\n" +
            "T1eA0tOivREvEAY2g+juRXJkYL2gCgYIKoZIzj0DAQehRANCAASmOs3JkSyoGEWZ\n" +
            "sUGxFs/4pw1rIlSV2IC19M8u3G5kq36upOwyFWj9Gi3Ejc9d3sC7+SHRqXrEAJow\n" +
            "8/7tRpV+\n" +
            "-----END PRIVATE KEY-----";

    public static final String SP_GLOBAL_ENABLE = "global_enable";
    public static final String SP_SMS_ENABLE = "sms_enable";
    public static final String SP_MISSED_CALL_ENABLE = "missed_call_enable";

    public static final String SP_SMS_TITLE_REGEX = "sms_title_regex";
    public static final String SP_SMS_CONTENT_REGEX = "sms_content_regex";
    public static final String SP_SMS_MERGE_LONG_TEXT = "sms_merge_long_text";

    public static final String SP_CALL_TITLE_REGEX = "call_title_regex";
    public static final String SP_CALL_CONTENT_REGEX = "call_content_regex";

    public static final String SP_FILTER_MODE_SMS_SENDER = "filter_mode_sms_sender";
    public static final String SP_FILTER_MODE_SMS_KEYWORD = "filter_mode_sms_keyword";
    public static final String SP_FILTER_MODE_CALL_SENDER = "filter_mode_call_sender";

    public static final String SP_FILTER_VALUE_SMS_SENDER = "filter_value_sms_sender";
    public static final String SP_FILTER_VALUE_SMS_KEYWORD = "filter_value_sms_keyword";
    public static final String SP_FILTER_VALUE_CALL_SENDER = "filter_value_call_sender";
}
