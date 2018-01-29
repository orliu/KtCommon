package com.orliu.kotlin.common.base

import android.app.Application
import com.orliu.kotlin.common.tools.isMyProcess

/**
 * Created by apple on 17/10/24.
 */
open abstract class LibBaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (checkPakcageName()){
            initLogger()
            initSocialSDK()
        }
    }

    private fun checkPakcageName(): Boolean {
        return initPackageName() == isMyProcess(android.os.Process.myPid())
    }

    abstract fun initPackageName(): String

    abstract fun initLogger()

    abstract fun initSocialSDK()

}