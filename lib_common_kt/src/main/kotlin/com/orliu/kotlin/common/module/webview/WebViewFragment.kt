package com.orliu.kotlin.common.module.webview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.ProgressBar
import com.orliu.kotlin.common.R
import com.orliu.kotlin.common.tools.Logger
import org.jetbrains.anko.support.v4.find


/**
 * @description desc
 * Created by liujianping
 * 17/10/27 下午1:51.
 */
class WebViewFragment : DialogFragment() {

    private lateinit var webview: WebView
    private lateinit var url: String
    private var engine: CustomerClientEngine? = null
    private var listener: OnLoadingListenerAdapter? = null

    companion object {

        fun instance(): WebViewFragment {
            return Holder.INSTANCE
        }
    }

    private object Holder {
        val INSTANCE = WebViewFragment()
    }

    fun url(url: String): WebViewFragment {
        this.url = url
        return this
    }

    fun engine(engine: CustomerClientEngine): WebViewFragment {
        this.engine = engine
        return this
    }

    fun listener(listener: OnLoadingListenerAdapter): WebViewFragment {
        this.listener = listener
        return this
    }

    /**
     * 参数校验
     */
    private fun validateParams(): Boolean {
        if (url.isEmpty()) {
            throw IllegalArgumentException("webview url is empty")
            return false
        } else {
            Logger.d("webview url is $url")
        }

        if (engine == null) Logger.i("webview engine is null")
        if (listener == null) Logger.i("webview listener is null")
        return true
    }

    /**
     * listener
     */
    private interface OnLoadingListener {
        fun shouldOverrideUrlLoading(webView: WebView?, url: String?): Boolean

        fun onPageStarted(webView: WebView?, url: String?)

        fun onPageFinished(webView: WebView?, url: String?)
    }

    /**
     * listener adapter
     */
    abstract class OnLoadingListenerAdapter : OnLoadingListener {
        override fun shouldOverrideUrlLoading(webView: WebView?, url: String?): Boolean {
            return false
        }

        override fun onPageStarted(webView: WebView?, url: String?) {
        }

        override fun onPageFinished(webView: WebView?, url: String?) {
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_webview, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {

        initView()
    }

    @SuppressLint("JavascriptInterface")
    private fun initView() {
        if (!validateParams()) return

        removeCookie()

        webview = find(R.id.id_webview)
        webview.requestFocus()

        // > 4.4, chrome debug mode
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) webview.webViewContentDebugEnable

        var setting = webview.settings
        setting.domStorageEnabled = true
        setting.javaScriptEnabled = true
        setting.javaScriptCanOpenWindowsAutomatically = true

        // zoom
        setting.setSupportZoom(false)
        setting.builtInZoomControls = false

        // cache
        setting.setAppCacheEnabled(true)
        val appCachePath = activity.getDir("cache", Context.MODE_PRIVATE).path
        setting.setAppCachePath(appCachePath)
        setting.setAppCacheMaxSize(5 * 1024 * 1024)

        // image do not display
        setting.blockNetworkImage = false
        setting.allowFileAccess = true

        // automatic adaptation
        setting.useWideViewPort = true
        setting.loadWithOverviewMode = true

        setting.allowContentAccess = true
        //webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        //webSettings.setPluginState(WebSettings.PluginState.ON);


        // client
        webview.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                listener?.let { it.shouldOverrideUrlLoading(view, url) }
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)

                listener?.onPageStarted(view, url)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                listener?.onPageFinished(view, url)
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                handler?.proceed()
            }
        }

        // chrome client
        webview.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {

                // update progressbar
                val progressbar = find<ProgressBar>(R.id.id_webiew_progressbar)
                when (newProgress) {
                    100 -> progressbar.visibility = View.GONE
                    else -> {
                        if (progressbar.visibility == View.GONE) progressbar.visibility = View.VISIBLE
                        progressbar.progress = newProgress
                    }
                }
                super.onProgressChanged(view, newProgress)
            }
        }

        engine?.let { webview.addJavascriptInterface(engine, "android") }
        url?.let { webview.loadUrl(url) }
    }

    /**
     * 移除cookie
     */
    private fun removeCookie() {
        CookieSyncManager.createInstance(activity)
        CookieManager.getInstance().removeAllCookie()
        CookieManager.getInstance().removeSessionCookie()
        CookieSyncManager.getInstance().sync()
        CookieSyncManager.getInstance().startSync()
    }

    /**
     * 物理返回键触发此方法
     *
     * @param keyCode
     * @param event
     * @return
     */
    fun onKeyDown(keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return if (isCanGoback()) {
                goBack()
                true
            } else {
                removeCookie()
                activity.finish()
                true
            }
        } else if (keyCode != KeyEvent.KEYCODE_VOLUME_UP
                && keyCode != KeyEvent.KEYCODE_VOLUME_DOWN
                && keyCode != KeyEvent.KEYCODE_POWER
                && keyCode != KeyEvent.KEYCODE_MENU
                && keyCode != KeyEvent.KEYCODE_DEL) {
            activity.finish()
            return true
        }
        return false
    }


    /**
     * 向前或向后访问已访问过的站点
     */
    private fun goBack() {
        if (isCanGoback()) {
            webview.goBack()
        } else {
            removeCookie()
            activity.finish()
        }
    }

    /**
     * 是否可以回退
     *
     * @return
     */
    private fun isCanGoback(): Boolean {
        return webview.canGoBack()
    }
}