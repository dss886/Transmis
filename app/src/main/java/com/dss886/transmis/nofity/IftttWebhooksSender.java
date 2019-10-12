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
public class IftttWebhooksSender extends javax.mail.Authenticator {

    private static final String URL = "https://maker.ifttt.com/trigger/";

    private OkHttpClient mClient = new OkHttpClient();
    private MediaType mediaType = MediaType.parse("application/json");

    private Executor mExecutor = Executors.newSingleThreadExecutor();

    public void send(String title, String content) {
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            return;
        }
        mExecutor.execute(() -> {
            try {
                String event = App.sp.getString(Tags.SP_IFTTT_WEBHOOKS_EVENT, null);
                String key = App.sp.getString(Tags.SP_IFTTT_WEBHOOKS_KEY, null);

                JSONObject message = new JSONObject();
                message.put("value1", "transmis");
                message.put("value2", title);
                message.put("value3", content);

                String url = URL + event + "/with/key/" + key;

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
                    Log.d("IftttWebhooksSender", url);
                    Log.d("IftttWebhooksSender", responseBody.string());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
