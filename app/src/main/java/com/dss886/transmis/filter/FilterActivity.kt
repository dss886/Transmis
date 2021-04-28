package com.dss886.transmis.filter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dss886.transmis.R
import com.dss886.transmis.base.BaseActivity
import com.dss886.transmis.utils.DialogBuilder.showEditTextDialog

/**
 * Created by duansishu on 2018/1/25.
 */
class FilterActivity : BaseActivity() {

    companion object {
        private const val BUNDLE_TYPE = "type"

        fun start(context: Context, type: FilterType) {
            val starter = Intent(context, FilterActivity::class.java).apply {
                putExtra(BUNDLE_TYPE, type)
            }
            context.startActivity(starter)
        }
    }

    private var mType: FilterType? = null
    private var mFilterAdapter: FilterAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<RecyclerView?>(R.id.recycler_view)?.apply {
            layoutManager = LinearLayoutManager(this@FilterActivity)
            adapter = FilterAdapter(this@FilterActivity, mType).also { mFilterAdapter = it }
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_base_recycler_view
    }

    override fun resolveIntentExtra(bundle: Bundle) {
        mType = bundle.getSerializable(BUNDLE_TYPE) as? FilterType
    }

    override fun getToolbarTitle(): String {
        return getString(mType!!.titleResId)
    }

    override fun showToolbarBackIcon(): Boolean {
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_filter, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_add) {
            tryToAddFilterValue()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun tryToAddFilterValue() {
        val inputType = if (mType === FilterType.SMS_KEYWORD) InputType.TYPE_CLASS_TEXT else InputType.TYPE_CLASS_NUMBER
        showEditTextDialog(this, "请添加过滤项", inputType = inputType) { content: String? ->
            mFilterAdapter?.add(content)
        }
    }

}