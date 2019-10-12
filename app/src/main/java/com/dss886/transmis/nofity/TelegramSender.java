package com.dss886.transmis.nofity;

import android.text.TextUtils;
import android.util.Log;

import com.dss886.transmis.base.App;
import com.dss886.transmis.utils.Tags;

import org.json.JSONObject;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by dray on 2019/10/13.
 */
public class TelegramSender extends javax.mail.Authenticator {

    private OkHttpClient mClient = new OkHttpClient();
    private MediaType mediaType = MediaType.parse("application/json");

    private Executor mExecutor = Executors.newSingleThreadExecutor();

    public void send(String title, String content) {
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            return;
        }
        mExecutor.execute(() -> {
            try {
                String url = App.sp.getString(Tags.SP_TELEGRAM_URL, null);
                String chat_id = App.sp.getString(Tags.SP_TELEGRAM_CHAT_ID, null);

                StringBuilder sb = new StringBuilder();
                sb.append(title).append("\n\n");
                for (String line : content.split("\n")) {
                    sb.append("> ").append(line).append("\n\n");
                }

                JSONObject message = new JSONObject();
                message.put("chat_id", chat_id);
                message.put("text", sb.toString().trim());
                message.put("parse_mode", "HTML");

                RequestBody body = RequestBody.create(mediaType, message.toString());
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .addHeader("content-type", "application/json")
                        .addHeader("cache-control", "no-cache")
                        .build();

                Response response = mClient.newCall(request).execute();
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    Log.d("TelegramSender", url);
                    Log.d("TelegramSender", responseBody.string());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
