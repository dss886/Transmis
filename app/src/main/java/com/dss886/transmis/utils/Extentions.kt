package com.dss886.transmis.utils

import android.content.Context
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.dss886.transmis.base.App
import com.dss886.transmis.view.*
import java.lang.ref.WeakReference
import java.util.concurrent.Executors

/**
 * Created by dss886 on 2021/02/11.
 */

val Float.dp: Float
    get() = (App.inst().applicationContext.resources.displayMetrics.density * this) + 0.5f

val Float.dpInt: Int
    get() = this.dp.toInt()

val Float.px: Float
    get() = this / App.inst().applicationContext.resources.displayMetrics.density + 0.5f

val Int.px: Float
    get() = this.toFloat().px

val Int.dp: Float
    get() = this.toFloat().dp

val Int.dpInt: Int
    get() = this.dp.toInt()

val Float.sp: Float
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, App.inst().applicationContext.resources.displayMetrics)

val Float.spInt: Int
    get() = this.sp.toInt()

val Int.sp: Float
    get() = this.toFloat().sp

val Int.spInt: Int
    get() = this.toFloat().sp.toInt()

val Int.toColor: Int
    get() = ContextCompat.getColor(App.inst(), this)

fun <T> T.weakRef(): WeakReference<T> = WeakReference(this)

class AsyncContext<T>(val weakRef: WeakReference<T>)

private val sAsyncExecutor = Executors.newCachedThreadPool()

fun <T> T.doAsync(task: AsyncContext<T>.() -> Unit) {
    val context = AsyncContext(WeakReference(this))
    sAsyncExecutor.execute {
        context.task()
    }
}

fun <T> AsyncContext<T>.uiThread(task: (T) -> Unit): Boolean {
    val ref = weakRef.get() ?: return false
    if (android.os.Looper.getMainLooper() === android.os.Looper.myLooper()) {
        task(ref)
    } else {
        App.inst().mainHandler.post { task(ref) }
    }
    return true
}

fun ViewGroup.forEachChildren(action: (View) -> Unit) {
    for (i in 0 until this.childCount) {
        action(this.getChildAt(i))
    }
}

fun List<String>.listToString(): String {
    if (this.isEmpty()) {
        return ""
    }
    val sb = StringBuilder()
    for (string in this) {
        sb.append(string).append(",")
    }
    if (sb.isNotEmpty()) {
        sb.deleteCharAt(sb.length - 1)
    }
    return sb.toString()
}

fun String.countOccurrences(ch: CharSequence): Int {
    return (this.length - this.replace(ch.toString(), "").length) / ch.length
}

fun String?.stringToList(): List<String> {
    if (this == null || TextUtils.isEmpty(this)) {
        return emptyList()
    }
    return this.split(",").toTypedArray().toList()
}

fun String.toEnableSpKey(): String {
    return this + "_enable"
}

fun IConfig.buildView(context: Context): BaseItemView {
    when (this) {
        is SectionConfig -> return SectionItemView(context).bind(this)
        is InfoConfig -> return InfoItemView(context).bind(this)
        is EditTextConfig -> return EditTextItemView(context).bind(this)
        is TextConfig -> return TextItemView(context).bind(this)
        is SwitchConfig -> return SwitchItemView(context).bind(this)
        is TestConfig -> return TestItemView(context).bind(this)
    }
    throw IllegalStateException("Config ${this.javaClass.simpleName} does not specify a target View!")
}