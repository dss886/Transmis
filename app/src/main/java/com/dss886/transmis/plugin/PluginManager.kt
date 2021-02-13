package com.dss886.transmis.plugin

import com.dss886.transmis.BuildConfig
import com.dss886.transmis.plugin.plugin.*
import com.dss886.transmis.utils.toEnableSpKey
import com.dss886.transmis.view.SpConfig

/**
 * Created by dss886 on 2021/02/11.
 */
object PluginManager {

    val plugins = mutableListOf<IPlugin>()

    private val mPluginKeySet = mutableSetOf<String>()
    private val mSpKeySet = mutableSetOf<String>()
    private val mDisableKey = listOf("sms", "call", "missed_call", "filter")

    fun init() {
        registerPlugin(MailPlugin())
        registerPlugin(DingDingPlugin())
        registerPlugin(BarkPlugin())
        registerPlugin(MailGunPlugin())
        registerPlugin(TelegramPlugin())
        registerPlugin(IFTTTPlugin())
    }

    private fun registerPlugin(plugin: IPlugin) {
        if (checkPlugin(plugin)) {
            plugins.add(plugin)
        }
    }

    private fun checkPlugin(plugin: IPlugin): Boolean {
        if (!BuildConfig.DEBUG) {
            // Check only when we are developing
            return true
        }
        if (mDisableKey.contains(plugin.getKey())) {
            throw IllegalArgumentException("Plugin ${plugin.getName()} " +
                    "has illegal key ${plugin.getKey()}")
        }
        for (char in plugin.getKey()) {
            if (char !in 'A'..'Z' && char !in 'a'..'z' && char !in '0'..'9' && char != '_') {
                throw IllegalArgumentException("Plugin ${plugin.getName()} " +
                        "has illegal key ${plugin.getKey()}")
            }
        }
        if (!mPluginKeySet.add(plugin.getKey())) {
            throw IllegalArgumentException("Plugin ${plugin.getName()} " +
                    "has illegal spKeyPrefix ${plugin.getKey()}")
        }
        plugin.getConfigs().filterIsInstance<SpConfig<*>>().forEach { config ->
            if (!config.spKey.startsWith(plugin.getKey() + "_")) {
                throw IllegalArgumentException("Plugin ${plugin.getName()} " +
                        "has an illegal config '${config.title}' which spKey(${config.spKey}) " +
                        "does not match the prefix ${plugin.getKey()}.")
            }
            if (config.spKey == config.spKey.toEnableSpKey()) {
                throw IllegalArgumentException("Plugin ${plugin.getName()} " +
                        "has an illegal config '${config.title}' which spKey(${config.spKey}) " +
                        "is not allowed!")
            }
            if (!mSpKeySet.add(config.spKey)) {
                throw IllegalArgumentException("Plugin ${plugin.getName()} " +
                        "has an illegal config '${config.title}' which spKey(${config.spKey}) " +
                        "is duplicated.")
            }
        }
        return true
    }

}