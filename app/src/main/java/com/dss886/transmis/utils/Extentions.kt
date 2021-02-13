package com.dss886.transmis.utils

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import com.dss886.transmis.base.App
import com.dss886.transmis.view.BaseItemView
import com.dss886.transmis.viewnew.*
import java.lang.ref.WeakReference
import java.util.concurrent.Executors

/**
 * Created by dss886 on 2021/02/11.
 */

val Float.dp: Float
    get() = (App.me().applicationContext.resources.displayMetrics.density * this) + 0.5f

val Float.dpInt: Int
    get() = this.dp.toInt()

val Float.px: Float
    get() = this / App.me().applicationContext.resources.displayMetrics.density + 0.5f

val Int.px: Float
    get() = this.toFloat().px

val Int.dp: Float
    get() = this.toFloat().dp

val Int.dpInt: Int
    get() = this.dp.toInt()

val Float.sp: Float
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, App.me().applicationContext.resources.displayMetrics)

val Float.spInt: Int
    get() = this.sp.toInt()

val Int.sp: Float
    get() = this.toFloat().sp

val Int.spInt: Int
    get() = this.toFloat().sp.toInt()

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
        App.mainHandler.post { task(ref) }
    }
    return true
}

fun ViewGroup.forEachChildren(action: (View) -> Unit) {
    for (i in 0 until this.childCount) {
        action(this.getChildAt(i))
    }
}

fun IConfig.buildView(context: Context): BaseItemView {
    when (this) {
        is SectionConfig -> return SectionItemView(context).bind(this)
        is InfoConfig -> return InfoItemView(context).bind(this)
        is EditTextConfig -> return EditTextItemView(context).bind(this)
        is TextButtonConfig -> return TextButtonItemView(context).bind(this)
        is SwitchConfig -> return SwitchItemView(context).bind(this)
    }
    throw IllegalStateException("Config ${this.javaClass.simpleName} does not specify a target View!")
}