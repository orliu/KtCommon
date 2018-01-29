package com.orliu.kotlin.common.tools

import android.os.StrictMode
import java.io.PrintWriter
import java.io.StringWriter


/**
 * app性能相关函数
 * Created by liujianping on 17/10/24.
 */
//检测内容泄漏
fun catchMemoryLeak() {
    val threadBuilder = StrictMode.ThreadPolicy.Builder()
    threadBuilder.detectAll()
    threadBuilder.penaltyLog()
    threadBuilder.penaltyDialog()
    threadBuilder.penaltyFlashScreen()
    StrictMode.setThreadPolicy(threadBuilder.build())

    val vmBuilder = StrictMode.VmPolicy.Builder()
    vmBuilder.detectAll()
    vmBuilder.penaltyLog()
    vmBuilder.penaltyDeath()
    StrictMode.setVmPolicy(vmBuilder.build())
}

//捕获异常，未处理
fun catchUncaughtException() {
    Thread.setDefaultUncaughtExceptionHandler { _, ex ->
        val writer = StringWriter()
        val pw = PrintWriter(writer)
        ex.printStackTrace(pw)
        pw.close()
        val error = writer.toString()
        Logger.e(error)
        throw ex
    }
}
