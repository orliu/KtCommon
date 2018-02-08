package com.orliu.kotlin.common.module.webview

import android.content.Context
import android.webkit.JavascriptInterface
import com.orliu.kotlin.common.extension.android.toastShort

/**
 * JS交互方法
 * Created by liujianping
 * 17/10/27 下午2:06.
 */
class CustomerClientEngine(val context: Context) : BaseWebViewClientEngine() {

    @JavascriptInterface
    override fun toast(message: String) {
        context.toastShort(message)
    }
}