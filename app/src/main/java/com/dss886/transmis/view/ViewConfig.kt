package com.dss886.transmis.view

import android.widget.CompoundButton
import com.dss886.transmis.base.App
import java.io.Serializable

/**
 * Created by dss886 on 2021/02/11.
 */

interface IConfig: Serializable

abstract class SpConfig<T>(var title: String, val spKey: String): IConfig {
    abstract fun setSpValue(value: T?)

    abstract fun getSpValue(defaultValue: T?): T?
}

abstract class StringSpConfig(title: String, spKey: String): SpConfig<String>(title, spKey) {
    override fun setSpValue(value: String?) {
        App.inst().sp.edit().putString(spKey, value).apply()
    }

    override fun getSpValue(defaultValue: String?): String? {
        return App.inst().sp.getString(spKey, defaultValue) ?: defaultValue
    }
}

abstract class BooleanSpConfig(title: String, spKey: String): SpConfig<Boolean>(title, spKey) {
    override fun setSpValue(value: Boolean?) {
        if (value == null) {
            App.inst().sp.edit().remove(spKey).apply()
        } else {
            App.inst().sp.edit().putBoolean(spKey, value).apply()
        }
    }

    override fun getSpValue(defaultValue: Boolean?): Boolean {
        return App.inst().sp.getBoolean(spKey, defaultValue ?: false)
    }
}

class SectionConfig(val title: String) : IConfig

class InfoConfig(val content: String) : IConfig

class TextConfig(val title: String,
                 var content: String? = null,
                 var showRightArrow: Boolean = false,
                 var clickAction: (() -> Unit)? = null) : IConfig {
    var resumeAction: (() -> Unit)? = null
}

class TestConfig(val title: String,
                 var content: String? = null,
                 var reason: Throwable? = null) : IConfig {
    var onTest: (() -> Unit)? = null
    var onReset: (() -> Unit)? = null
    var onSuccess: (() -> Unit)? = null
    var onFailure: (() -> Unit)? = null
}

class EditTextConfig(title: String, spKey: String) : StringSpConfig(title, spKey) {
    var isRequired: Boolean = true // isRequired=false also means it has default value
    var isPassword: Boolean = false
    var isMultiLine: Boolean = false
}

class SwitchConfig(title: String, spKey: String) : BooleanSpConfig(title, spKey) {
    var defaultValue: Boolean = false
    var onCheckedChangeListener: CompoundButton.OnCheckedChangeListener? = null
    fun getSpValue(): Boolean {
        return super.getSpValue(defaultValue)
    }
}