package com.dss886.transmis.utils

import com.dss886.transmis.base.App

/**
 * Created by dss886 on 2021/02/13.
 * Global switch and configuration manager
 */
object TransmisManager {

    fun isGlobalEnable(): Boolean {
        return App.inst().sp.getBoolean(Constants.SP_GLOBAL_ENABLE, false)
    }

    fun isSmsEnable(): Boolean {
        return App.inst().sp.getBoolean(Constants.SP_SMS_ENABLE, false)
    }

    fun isMissingCallEnable(): Boolean {
        return App.inst().sp.getBoolean(Constants.SP_MISSED_CALL_ENABLE, false)
    }

}