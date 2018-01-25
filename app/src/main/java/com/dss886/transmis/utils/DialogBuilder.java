package com.dss886.transmis.utils;

import android.annotation.SuppressLint;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import com.dss886.transmis.R;
import com.dss886.transmis.base.BaseActivity;

/**
 * Created by dss886 on 2017/7/1.
 */

public class DialogBuilder {

    @SuppressLint("InflateParams")
    public static void showEditTextDialog(BaseActivity activity, String title, String content,
                                                 boolean isPassword, EditTextDialogCallback callback) {
        View layout = LayoutInflater.from(activity).inflate(R.layout.view_dialog_edit_text, null);
        EditText input = layout.findViewById(R.id.edit_text);
        input.setText(content);
        if (isPassword) {
            input.setInputType(InputType.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
        }
        input.setSelection(input.getText().length());

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setView(layout);
        builder.setPositiveButton("确定", (dialog, which) -> {
            if (callback != null) {
                callback.onSuccess(input.getText().toString());
            }
        });
        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    public static AlertDialog showAlertDialog(BaseActivity activity, String content, AlertDialogCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(content);
        builder.setPositiveButton("确定", (dialog, which) -> {
            if (callback != null) {
                callback.onSuccess();
            }
        });
        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());

        return builder.show();
    }

    public interface EditTextDialogCallback {
        void onSuccess(String content);
    }

    public interface AlertDialogCallback {
        void onSuccess();
    }
}
