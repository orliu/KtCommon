package com.orliu.kotlin

import com.orliu.kotlin.base.BaseActivity
import com.orliu.kotlin.common.base.BaseResult
import com.orliu.kotlin.net.NetObserver
import com.orliu.kotlin.net.NetService
import com.orliu.retrofit.NetClient
import com.orliu.retrofit.extension.request
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class MainActivity : BaseActivity() {

    override fun getTitleStr(): String = "Main Act"

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initBundleArgs() {
    }

    override fun initDataOnStart() {
    }

    override fun initViewOnResume() {

        NetClient.baseUrl("http://baidu.com")
                .create(NetService::class.java)
                .getOriginalString("http://baidu.com")
                .request(object : NetObserver<String>() {

                    override fun onSuccess(t: String) {
                        id_tv.text= t
                    }

                    override fun onError(error: BaseResult<*>) {

                        id_tv.text = error.msg
                    }
                })


        id_btn.onClick {
            NetClient.baseUrl("http://www.123.com")
                    .connectTimeout(100)
                    .readTimeout(10)
                    .create(NetService::class.java)
                    .getOriginalString("http://www.163.com")
                    .request(object:NetObserver<String>(){
                        override fun onSuccess(t: String) {
                           id_tv.text = t
                        }

                        override fun onError(error: BaseResult<*>) {
                            id_tv.text = error.msg
                        }
                    })
        }
    }

    override fun syncDataOnResume() {
    }

}
