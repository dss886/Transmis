package com.dss886.transmis.view

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.dss886.transmis.R
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by dss886 on 2021/02/11.
 */
class TestItemView(context: Context) : BaseItemView(context) {

    init {
        inflate(getContext(), R.layout.view_test_item, this)
    }

    private val mTestLayoutView: View = findViewById(R.id.test_layout)
    private val mTitleView: TextView = findViewById(R.id.title)
    private val mContentView: TextView = findViewById(R.id.content)
    private val mReasonView: TextView = findViewById(R.id.reason)
    private val mReasonDividerView: View = findViewById(R.id.reason_divider)
    private val mProgressView: View = findViewById(R.id.progress)
    private val mIconView: ImageView = findViewById(R.id.icon)
    private lateinit var mConfig: TestConfig
    private var mIsLoading: Boolean = false

    fun bind(config: TestConfig): TestItemView {
        mConfig = config
        mTitleView.text = config.title
        mContentView.text = config.content
        mTestLayoutView.setOnClickListener {
            if (mIsLoading) {
                return@setOnClickListener
            }
            mIsLoading = true
            mContentView.text = config.content
            mIconView.visibility = View.GONE
            mReasonView.visibility = View.GONE
            mReasonDividerView.visibility = View.GONE
            mProgressView.visibility = View.VISIBLE
            mConfig.onTest?.invoke()
        }
        mConfig.onReset = {
            mIsLoading = false
            mContentView.text = config.content
            mIconView.visibility = View.GONE
            mReasonView.visibility = View.GONE
            mReasonDividerView.visibility = View.GONE
            mProgressView.visibility = View.GONE
        }
        mConfig.onSuccess = { result ->
            mIsLoading = false
            mContentView.text = config.content
            mIconView.setImageResource(R.drawable.ic_success)
            mIconView.visibility = View.VISIBLE
            if (result != null) {
                mReasonView.text = try {
                    JSONObject(result).toString(4)
                } catch (e: JSONException) {
                    result
                }
                mReasonView.visibility = View.VISIBLE
                mReasonDividerView.visibility = View.VISIBLE
            } else {
                mReasonView.visibility = View.GONE
                mReasonDividerView.visibility = View.GONE
            }
            mProgressView.visibility = View.GONE
        }
        mConfig.onFailure = { tr ->
            mIsLoading = false
            mContentView.text = config.content
            mReasonView.text = tr?.stackTraceToString() ?: ""
            mIconView.setImageResource(R.drawable.ic_error)
            mIconView.visibility = View.VISIBLE
            mReasonView.visibility = View.VISIBLE
            mReasonDividerView.visibility = View.VISIBLE
            mProgressView.visibility = View.GONE
        }
        return this
    }

    override fun onResume() {
        // do noting
    }

}