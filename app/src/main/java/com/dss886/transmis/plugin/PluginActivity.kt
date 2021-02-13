package com.dss886.transmis.plugin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.dss886.transmis.R
import com.dss886.transmis.base.BaseSwitchActivity
import com.dss886.transmis.view.IConfig
import com.dss886.transmis.view.SectionConfig
import com.dss886.transmis.view.TextButtonConfig

/**
 * Created by dss886 on 2021/02/11.
 */
class PluginActivity: BaseSwitchActivity() {

    companion object {
        @JvmStatic
        fun start(context: Context, plugin: IPlugin) {
            val starter = Intent(context, PluginActivity::class.java)
                    .putExtra("plugin", plugin)
            context.startActivity(starter)
        }
    }

    private lateinit var mPlugin: IPlugin

    override fun onCreate(savedInstanceState: Bundle?) {
        mPlugin = intent?.getSerializableExtra("plugin") as? IPlugin
                ?: throw IllegalStateException("Plugin is null!")
        super.onCreate(savedInstanceState)
    }

    override fun getLayout(): Int {
        return R.layout.activity_base_switch
    }

    override fun getConfigs(): List<IConfig> {
        return mPlugin.getConfigs().toMutableList().apply {
            add(SectionConfig("测试"))
            add(TextButtonConfig("点击发送测试数据") {
                mPlugin.doNotify("Test Title", "Test Content")
            })
        }
    }

    override fun getToolbarTitle(): String {
        return "插件设置 - " + mPlugin.getName()
    }

    override fun showToolbarBackIcon(): Boolean {
        return true
    }

}