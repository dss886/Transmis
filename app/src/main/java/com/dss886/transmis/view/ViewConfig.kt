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

class TextButtonConfig(val title: String,
                       var content: String? = null,
                       var showRightArrow: Boolean = false,
                       val clickAction: () -> Unit) : IConfig

class EditTextConfig(title: String, spKey: String) : StringSpConfig(title, spKey) {
    var isRequired: Boolean = true  // TODO: 2021/02/11 @duansishu to be done
    var isPassword: Boolean = false
}

class SwitchConfig(title: String, spKey: String) : BooleanSpConfig(title, spKey) {
    var defaultValue: Boolean = false
    var onCheckedChangeListener: CompoundButton.OnCheckedChangeListener? = null
    fun getSpValue(): Boolean {
        return super.getSpValue(defaultValue)
    }
}