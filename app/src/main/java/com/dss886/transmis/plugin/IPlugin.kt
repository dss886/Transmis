package com.dss886.transmis.plugin

import com.dss886.transmis.viewnew.IConfig
import java.io.Serializable

/**
 * Created by dss886 on 2021/02/11.
 */
interface IPlugin : Serializable {

    fun getName(): String

    fun isEnable(): Boolean

    /**
     * To avoid the key conflict of sp,
     * each plugin's spKey must have an independent prefix
     */
    fun getSpKeyPrefix(): String

    fun getConfigs(): List<IConfig>

    fun doNotify(title: String, content: String)

}