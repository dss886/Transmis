package com.dss886.transmis.nofity;

import android.text.TextUtils;
import android.util.Log;

import com.dss886.transmis.base.App;
import com.dss886.transmis.utils.Tags;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by dray on 2019/10/13.
 */
public class MailGunSender extends javax.mail.Authenticator {

    private static final String URL = "https://api.mailgun.net/v3/";
    private static final String BOUNDARY = "----WebKitFormBoundary7MA4YWxkTrZu0gW";

    private OkHttpClient mClient = new OkHttpClient();
    private MediaType mediaType = MediaType.parse("multipart/form-data; boundary=" + BOUNDARY);

    private Executor mExecutor = Executors.newSingleThreadExecutor();

    public void send(String title, String content) {
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            return;
        }
        mExecutor.execute(() -> {
            try {
                String domain = App.sp.getString(Tags.SP_MAILGUN_DOMAIN, null);
                String key = App.sp.getString(Tags.SP_MAILGUN_KEY, null);
                String from_name = App.sp.getString(Tags.SP_MAILGUN_SEND_NAME, null);
                String from_mail = App.sp.getString(Tags.SP_MAILGUN_SEND_MAIL, null);
                String to_mail = App.sp.getString(Tags.SP_MAILGUN_RECEIVE_MAIL, null);

                String url = URL + domain + "/messages";
                String text = "--" + BOUNDARY + "\n";
                text += (
                        "Content-Disposition: form-data; name=\"from\"\r\n\r\n" +
                                from_name + "<" + from_mail + ">" + "\r\n" +
                                "--" + BOUNDARY + "\r\n" +
                                "Content-Disposition: form-data; name=\"to\"\r\n\r\n" +
                                to_mail + "\r\n" +
                                "--" + BOUNDARY + "\r\n" +
                                "Content-Disposition: form-data; name=\"subject\"\r\n\r\n" +
                                title + "\r\n" +
                                "--" + BOUNDARY + "\r\n" +
                                "Content-Disposition: form-data; name=\"text\"\r\n\r\n" +
                                content + "\r\n" +
                                "--" + BOUNDARY + "--\r\n"
                );

                RequestBody body = RequestBody.create(mediaType, text);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .addHeader("content-type", "multipart/form-data; boundary=" + BOUNDARY)
                        .addHeader("authorization", "Basic " + key)
                        .addHeader("cache-control", "no-cache")
                        .build();

                Response response = mClient.newCall(request).execute();
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    Log.d("MailGunSender", url);
                    Log.d("MailGunSender", responseBody.string());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
