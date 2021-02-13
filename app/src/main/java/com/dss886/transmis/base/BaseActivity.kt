package com.dss886.transmis.base

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import com.dss886.transmis.R
import com.dss886.transmis.utils.dp

/**
 * Created by dss886 on 2017/6/30.
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.extras?.let {
            resolveIntentExtra(it)
        }
        setContentView(getLayout())
        findViewById<Toolbar?>(R.id.toolbar)?.let { toolbar ->
            setSupportActionBar(toolbar)
            ViewCompat.setElevation(toolbar, 4.dp)
        }
        supportActionBar?.apply {
            title = getToolbarTitle()
            setDisplayHomeAsUpEnabled(showToolbarBackIcon())
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    protected abstract fun getLayout(): Int
    protected open fun resolveIntentExtra(bundle: Bundle) {}
    protected abstract fun getToolbarTitle(): String
    protected abstract fun showToolbarBackIcon(): Boolean

}