package com.orliu.kotlin

import android.Manifest
import android.app.Activity
import android.content.Intent
import com.orliu.amap.search.AMapSearchActivity
import com.orliu.kotlin.base.BaseActivity
import com.orliu.kotlin.common.extension.android.toastShort
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk25.coroutines.onClick


class MainActivity : BaseActivity() {

    override fun getTitleStr(): String = "Main Act"

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initBundleArgs() {
    }

    override fun initDataOnStart() {
    }

    private var index = 0

    override fun initViewOnResume() {

        id_btn.onClick {

            val permission = RxPermissions(this@MainActivity)
            permission.request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe { granted ->
                        when (granted) {
                            true -> AMapSearchActivity.startActivityForResult(this@MainActivity)
                            false -> toastShort("need permission")
                        }
                    }

        }


    }

    override fun syncDataOnResume() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) return
        when (requestCode) {
            AMapSearchActivity.REQUEST_CODE -> {
                val addressStr = data?.getStringExtra("addressStr")
                id_tv.text = addressStr
            }
            else -> id_tv.text = "default"
        }
    }

}
