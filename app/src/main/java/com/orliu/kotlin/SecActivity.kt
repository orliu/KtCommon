package com.orliu.kotlin

import com.orliu.kotlin.base.BaseActivity
import kotlinx.android.synthetic.main.activity_sec.*

/**
 * @description desc
 * Created by orliu
 * 17/10/30 下午2:52.
 */
class SecActivity : BaseActivity() {
    override fun getTitleStr(): String = "SecActivity Page"

    private var key: String? = ""
    override fun getLayoutId(): Int = R.layout.activity_sec

    override fun initBundleArgs() {
        intent.extras?.let {
            it["key"]?.let { key = it.toString() }
        }
    }

    override fun initDataOnStart() {
    }

    override fun initViewOnResume() {
        id_text_sec.text = id_text_sec.text.toString().plus(key)
    }

    override fun syncDataOnResume() {
    }

}