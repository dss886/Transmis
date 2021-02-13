package com.dss886.transmis.plugin

import com.dss886.transmis.view.IConfig
import java.io.Serializable

/**
 * Created by dss886 on 2021/02/11.
 */
interface IPlugin : Serializable {

    fun getName(): String

    fun isEnable(): Boolean

    /**
     * The unique identifier of a plugin, which must be in [a-z][A-Z][0-9].
     * To avoid the key conflict of sp,
     * each config's spKey in this plugin must start with (key + "_")
     */
    fun getKey(): String

    fun getConfigs(): List<IConfig>

    fun doNotify(title: String, content: String)

}