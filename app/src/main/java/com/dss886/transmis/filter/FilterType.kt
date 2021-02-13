package com.dss886.transmis.filter

import com.dss886.transmis.R
import com.dss886.transmis.utils.Constants

/**
 * Created by dss886 on 2021/02/13.
 */
enum class FilterType(val titleResId: Int, val modeSpKey: String, val valueSpKey: String) {

    SMS_SENDER(R.string.sender_title, Constants.SP_FILTER_MODE_SMS_SENDER, Constants.SP_FILTER_VALUE_SMS_SENDER),
    SMS_KEYWORD(R.string.keyword_title, Constants.SP_FILTER_MODE_SMS_KEYWORD, Constants.SP_FILTER_VALUE_SMS_KEYWORD),
    CALL_SENDER(R.string.sender_call_title, Constants.SP_FILTER_MODE_CALL_SENDER, Constants.SP_FILTER_VALUE_CALL_SENDER);

}