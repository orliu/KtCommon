package com.orliu.kotlin.common.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.FragmentActivity

/**
 * @description desc
 * Created by liujianping
 * 17/10/30 下午1:45.
 */
abstract class LibBaseActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(getLayoutId())
        overridePendingTransition()
        initBundleArgs()
    }


    override fun onStart() {
        super.onStart()

        initDataOnStart()
    }

    override fun onResume() {
        super.onResume()

        initViewOnResume()
        syncDataOnResume()
    }

    /**
     * layout resource
     */
    abstract fun getLayoutId(): Int

    /**
     * init intent's args
     */
    abstract fun initBundleArgs()

    /**
     * init intent's args of url
     */
    fun initBundleArgs(url: String?) {}

    /**
     * init local data
     */
    abstract fun initDataOnStart()

    /**
     * ui's operation
     */
    abstract fun initViewOnResume()

    /**
     * init online data
     */
    abstract fun syncDataOnResume()


    /**
     * activity anim
     */
    abstract fun overridePendingTransition()
}