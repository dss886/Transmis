package com.dss886.transmis.utils

import android.annotation.SuppressLint
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.dss886.transmis.R
import com.dss886.transmis.base.BaseActivity

/**
 * Created by dss886 on 2017/7/1.
 */
object DialogBuilder {

    @JvmStatic
    @SuppressLint("InflateParams")
    fun showEditTextDialog(activity: BaseActivity,
                           title: String? = null,
                           content: String? = null,
                           hint: String? = null,
                           inputType: Int = InputType.TYPE_CLASS_TEXT,
                           onPositive: (String) -> Unit?) {
        val layout = LayoutInflater.from(activity).inflate(R.layout.view_dialog_edit_text, null)
        val input = layout.findViewById<EditText>(R.id.edit_text).apply {
            this.inputType = inputType
            setText(content)
            setPadding(paddingLeft, paddingTop,
                if (isPassword(inputType)) 36.dpInt else paddingRight, paddingBottom)
            this.hint = hint
            setSelection(text.length)
            requestFocus()
        }
        layout.findViewById<ImageView>(R.id.eye).apply {
            visibility = if (isPassword(input.inputType)) View.VISIBLE else View.GONE
            setOnClickListener {
                input.inputType = togglePasswordVisibility(input.inputType)
                input.setSelection(input.text.length)
            }
        }
        AlertDialog.Builder(activity)
                .setTitle(title)
                .setView(layout)
                .setPositiveButton("确定") { _, _ -> onPositive.invoke(input.text.toString()) }
                .setNegativeButton("取消") { dialog, _ -> dialog.dismiss() }
                .show()
                .apply {
                    window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
                }
    }

    @JvmStatic
    fun showAlertDialog(activity: BaseActivity, title: String?, content: String?, onPositive: (() -> Unit?)?) {
        val dialog = AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(content)
                .setPositiveButton("确定") { _, _ -> onPositive?.invoke() }
        if (onPositive != null) {
            dialog.setNegativeButton("取消") { it, _ -> it.dismiss() }
        }
        dialog.show()
    }

    private fun isPassword(inputType: Int): Boolean {
        return when(inputType and InputType.TYPE_MASK_CLASS) {
            InputType.TYPE_CLASS_TEXT -> {
                val variation = inputType and InputType.TYPE_MASK_VARIATION
                arrayOf(InputType.TYPE_TEXT_VARIATION_PASSWORD,
                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD,
                    InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD).contains(variation)
            }
            InputType.TYPE_CLASS_NUMBER -> {
                inputType and InputType.TYPE_MASK_VARIATION == InputType.TYPE_NUMBER_VARIATION_PASSWORD
            }
            InputType.TYPE_CLASS_PHONE,
            InputType.TYPE_CLASS_DATETIME -> false
            else -> false
        }
    }

    private fun togglePasswordVisibility(inputType: Int): Int {
        return when(inputType and InputType.TYPE_MASK_CLASS) {
            InputType.TYPE_CLASS_TEXT -> {
                when (inputType and InputType.TYPE_MASK_VARIATION) {
                    InputType.TYPE_TEXT_VARIATION_PASSWORD -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    else -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }
            }
            InputType.TYPE_CLASS_NUMBER -> {
                val variation = inputType and InputType.TYPE_MASK_VARIATION
                if (variation == InputType.TYPE_NUMBER_VARIATION_PASSWORD) {
                    InputType.TYPE_CLASS_NUMBER
                } else {
                    InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
                }
            }
            InputType.TYPE_CLASS_PHONE,
            InputType.TYPE_CLASS_DATETIME -> inputType
            else -> inputType
        }
    }

}