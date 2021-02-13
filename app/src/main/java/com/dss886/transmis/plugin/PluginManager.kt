package com.dss886.transmis.plugin

import com.dss886.transmis.plugin.plugin.*
import com.dss886.transmis.viewnew.SpConfig

/**
 * Created by dss886 on 2021/02/11.
 */
object PluginManager {

    val plugins = mutableListOf<IPlugin>()

    private val mSpPrefixSet = mutableSetOf<String>()
    private val mSpKeySet = mutableSetOf<String>()

    fun init() {
        registerPlugin(MailPlugin())
        registerPlugin(DingDingPlugin())
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
        if (!mSpPrefixSet.add(plugin.getSpKeyPrefix())) {
            throw IllegalArgumentException("Plugin ${plugin.getName()} " +
                    "has illegal spKeyPrefix ${plugin.getSpKeyPrefix()}")
        }
        plugin.getConfigs().filterIsInstance<SpConfig<*>>().forEach { config ->
            if (!config.spKey.startsWith(plugin.getSpKeyPrefix())) {
                throw IllegalArgumentException("Plugin ${plugin.getName()} " +
                        "has an illegal config '${config.title}' which spKey(${config.spKey}) " +
                        "does not match the prefix ${plugin.getSpKeyPrefix()}.")
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