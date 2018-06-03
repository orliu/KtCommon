package com.orliu.kotlin.common.extension.android

import android.support.v4.app.FragmentActivity
import android.view.View

/**
 * View扩展函数
 * Created by orliu on 31/01/2018.
 */

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

/**
 * 计算属性为GONE的View的size
 * @param view
 * @return int[]: width;height
 */
fun calcViewSizeForGone(view: View): IntArray {
    val size = IntArray(2)
    val width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    val height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    view.measure(width, height)
    size[0] = view.measuredWidth
    size[1] = view.measuredHeight
    return size
}