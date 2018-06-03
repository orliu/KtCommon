package com.orliu.kotlin.common.base

import android.os.Bundle
import android.support.v4.app.FragmentActivity

/**
 * @description desc
 * Created by orliu
 * 17/10/30 下午1:45.
 */
abstract class LibBaseActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(getLayoutId())
        overridePendingTransition()
        initDataOnCreate()
        initView()
    }


    override fun onStart() {
        super.onStart()

        initDataOnStart()
    }

    override fun onResume() {
        super.onResume()

        initDataOnResume()
    }

    /**
     * layout resource
     */
    abstract fun getLayoutId(): Int

    /**
     * init intent's args
     */
    open fun initDataOnCreate(){}

    /**
     * ui's operation
     */
    abstract fun initView()

    /**
     * init local data
     */
    open fun initDataOnStart(){}

    /**
     * init online data
     */
    abstract fun initDataOnResume()

    /**
     * activity anim
     */
    abstract fun overridePendingTransition()
}