package com.dss886.transmis.utils

import com.dss886.transmis.base.App
import com.dss886.transmis.filter.FilterType

/**
 * Created by dss886 on 2021/02/13.
 * Global switch and configuration manager
 */
object TransmisManager {

    private val mFilterMap = mutableMapOf<FilterType, MutableList<String>>()

    fun isGlobalEnable(): Boolean {
        return App.inst().sp.getBoolean(Constants.SP_GLOBAL_ENABLE, false)
    }

    fun isSmsEnable(): Boolean {
        return App.inst().sp.getBoolean(Constants.SP_SMS_ENABLE, false)
    }

    fun isMissingCallEnable(): Boolean {
        return App.inst().sp.getBoolean(Constants.SP_MISSED_CALL_ENABLE, false)
    }

    fun init() {
        FilterType.values().forEach { type ->
            val value = App.inst().sp.getString(type.valueSpKey, null)
            mFilterMap[type] = value.stringToList().toMutableList()
        }
    }

    fun getFilters(type: FilterType): List<String> {
        return mFilterMap[type] ?: emptyList()
    }

    fun addFilter(type: FilterType, value: String) {
        mFilterMap[type]?.add(value)
        saveFilter(type)
    }

    fun removeFilter(type: FilterType, position: Int) {
        mFilterMap[type]?.removeAt(position)
        saveFilter(type)
    }

    fun getFilterCount(type: FilterType): Int {
        return mFilterMap[type]?.size ?: 0
    }

    private fun saveFilter(type: FilterType) {
        App.inst().sp.edit().putString(type.valueSpKey, mFilterMap[type]?.listToString()).apply()
    }

}