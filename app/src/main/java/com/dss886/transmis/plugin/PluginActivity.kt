package com.dss886.transmis.plugin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.CompoundButton
import com.dss886.transmis.R
import com.dss886.transmis.base.BaseConfigActivity
import com.dss886.transmis.utils.DialogBuilder
import com.dss886.transmis.utils.toEnableSpKey
import com.dss886.transmis.utils.weakRef
import com.dss886.transmis.view.*

/**
 * Created by dss886 on 2021/02/11.
 */
class PluginActivity: BaseConfigActivity() {

    companion object {
        @JvmStatic
        fun start(context: Context, plugin: IPlugin) {
            val starter = Intent(context, PluginActivity::class.java)
                    .putExtra("plugin", plugin)
            context.startActivity(starter)
        }
    }

    private lateinit var mPlugin: IPlugin
    private var mTester: PluginTester? = null

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
            add(0, SwitchConfig("插件开关", mPlugin.getKey().toEnableSpKey()).apply {
                onCheckedChangeListener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked && !checkConfigValid()) {
                        DialogBuilder.showAlertDialog(this@PluginActivity, null, "请填写必填参数！", null)
                        buttonView.postDelayed({
                            buttonView.isChecked = false
                        }, 180L)
                    }
                }
            })
            add(SectionConfig("测试"))
            add(TestConfig("点击发送测试数据").apply {
                onTest = { doTest(this) }
            })
        }
    }

    override fun getToolbarTitle(): String {
        return "插件设置 - " + mPlugin.getName()
    }

    override fun showToolbarBackIcon(): Boolean {
        return true
    }

    private fun doTest(config: TestConfig) {
        if (!checkConfigValid()) {
            config.onReset?.invoke()
            DialogBuilder.showAlertDialog(this, null, "请填写必填参数！", null)
            return
        }
        config.content = "测试中"
        val tester = PluginTester().apply {
            onSuccess = {
                config.content = "测试成功"
                config.onSuccess?.invoke()
            }
            onFailure = { e ->
                config.content = "测试失败"
                config.reason = e
                config.onFailure?.invoke()
            }
            mTester = this
        }
        mPlugin.doNotify("Test Title", "Test Content", tester.weakRef())
    }

    private fun checkConfigValid(): Boolean {
        return mPlugin.getConfigs().none {
            it is EditTextConfig && it.isRequired && it.getSpValue(null).isNullOrEmpty()
        }
    }

}