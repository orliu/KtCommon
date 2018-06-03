package com.orliu.kotlin.base

import com.orliu.kotlin.common.base.LibBaseApplication
import com.orliu.kotlin.common.tools.Logger
import com.orliu.kotlin.common.tools.catchUncaughtException
import com.tunaikita.log.LogSDK

/**
 * Created by orliu on 26/01/2018.
 */
class BaseApplication : LibBaseApplication() {

    override fun initPackageName(): String {
        return "com.orliu.kotlin"
    }

    override fun initLogger() {
        Logger.debug = true
        catchUncaughtException()
    }

    override fun initSocialSDK() {
        //LogSDK.init(this, true)
    }

}