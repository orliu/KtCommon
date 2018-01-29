package com.orliu.kotlin.base

import android.os.Bundle
import com.orliu.kotlin.R
import com.orliu.kotlin.common.base.LibBaseActivity

/**
 * @description desc
 * Created by liujianping
 * 17/10/30 下午3:04.
 */
abstract class BaseActivity : LibBaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initNavigationBar()
    }

    /**
     * navigation bar
     */
    private fun initNavigationBar() {
//        id_navigation_layout?.let {
//            id_navigation_title.text = getTitleStr()
//
//            id_navigation_back.onClick { finish() }
//        }
    }

    /**
     * title text
     */
    abstract fun getTitleStr(): String?

    /**
     * activity anim
     */
    override fun overridePendingTransition() {
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out)
    }

}