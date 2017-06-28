package com.dss886.transmis;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.Switch;
import com.dss886.transmis.base.Settings;
import com.dss886.transmis.view.TextItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Switch switchAll = (Switch) findViewById(R.id.switch_all);
        switchAll.setChecked(Settings.inst().isEnable());
        switchAll.setOnCheckedChangeListener((buttonView, isChecked) -> Settings.inst().setEnable(isChecked));

        LinearLayout container = (LinearLayout) findViewById(R.id.container);
        container.addView(TextItem.get(this, "监听内容", "短信、未接来电"));
        container.addView(TextItem.get(this, "提醒方式", "邮件"));
        container.addView(TextItem.get(this, "收件人", "dss886@qq.com"));
        container.addView(TextItem.get(this, "短信提醒标题", "你的备用机收到了一️条新短信"));
        container.addView(TextItem.get(this, "电话提醒标题", "你的备用机有一个未接电话"));
    }

}
