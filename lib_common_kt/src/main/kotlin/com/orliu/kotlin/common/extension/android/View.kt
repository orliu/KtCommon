package com.orliu.kotlin.common.extension.android

import android.support.v4.app.FragmentActivity
import android.view.View

/**
 * View扩展函数
 * Created by liujianping on 31/01/2018.
 */

// findView
inline fun <reified T : View> FragmentActivity.findView(viewId: Int): T = findViewById(viewId)

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

