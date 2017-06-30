package com.dss886.transmis.utils;

import android.annotation.SuppressLint;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import com.dss886.transmis.R;
import com.dss886.transmis.base.BaseActivity;

/**
 * Created by dss886 on 2017/7/1.
 */

public class DialogBuilder {

    @SuppressLint("InflateParams")
    public static AlertDialog showEditTextDialog(BaseActivity activity, String title, String content,
                                                 EditTextDialogCallback listener) {
        View layout = LayoutInflater.from(activity).inflate(R.layout.view_dialog_edit_text, null);
        EditText input = (EditText) layout.findViewById(R.id.edit_text);
        input.setText(content);
        input.setSelection(input.getText().length());

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setView(layout);
        builder.setPositiveButton("确定", (dialog, which) -> {
            if (listener != null) {
                listener.onSuccess(input.getText().toString());
            }
        });
        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());

        return builder.show();
    }

    public interface EditTextDialogCallback {
        void onSuccess(String content);
    }
}
