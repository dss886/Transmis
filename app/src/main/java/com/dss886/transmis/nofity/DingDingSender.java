package com.dss886.transmis.nofity;

import android.text.TextUtils;
import android.util.Log;
import com.dss886.transmis.base.App;
import com.dss886.transmis.utils.Tags;
import okhttp3.*;
import org.json.JSONObject;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by dss886 on 2017/9/22.
 */

public class DingDingSender {

    private static final String URL = "https://oapi.dingtalk.com/robot/send?access_token=";

    private MediaType mMediaType = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient mClient = new OkHttpClient();
    private Executor mExecutor = Executors.newSingleThreadExecutor();

    public void send(String title, String content) {
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            return;
        }
        mExecutor.execute(() -> {
            try {
                String url = URL + App.sp.getString(Tags.SP_DING_TOKEN, null);
                StringBuilder sb = new StringBuilder();
                sb.append(title).append("\n\n");
                for (String line : content.split("\n")) {
                    sb.append("> ").append(line).append("\n\n");
                }
                JSONObject message = new JSONObject();
                message.put("msgtype", "markdown");
                JSONObject markdown = new JSONObject();
                markdown.put("title", title);
                markdown.put("text", sb.toString().trim());
                message.put("markdown", markdown);

                RequestBody body = RequestBody.create(mMediaType, message.toString());
                Request request = new Request.Builder().url(url).post(body).build();
                Response response = mClient.newCall(request).execute();
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    Log.d("DingDingSender", responseBody.string());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
