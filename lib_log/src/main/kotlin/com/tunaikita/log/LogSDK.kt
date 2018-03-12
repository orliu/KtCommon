package com.tunaikita.log

import android.content.Context
import com.orliu.kotlin.common.tools.Logger
import com.tunaikita.log.bean.HttpLog
import com.tunaikita.log.database.Database
import com.tunaikita.log.database.DatabaseManager
import com.tunaikita.log.module.toolview.ToolViewManager


/**
 * Created by orliu on 17/11/29.
 */
object LogSDK {

    private var open: Boolean = false

    @JvmStatic
    fun init(context: Context, open: Boolean) {
        this.open = open
        when (open) {

            true -> {
                // init view
                ToolViewManager.initView(context)

                // init databse
                DatabaseManager.initDatabase(context)
            }
            false -> Logger.e("LogSDK is not open")
        }
    }

    @JvmStatic
    fun close() {
        ToolViewManager.close()
    }

    /**
     * 记录一条http日志
     */
    @JvmStatic
    fun recordHttpLog(requestUrl: String,
                      requestMethod: String,
                      requestParams: Map<String, Object>?,
                      requestHeaders: Map<String, Object>?,
                      responseJson: String?,
                      requestTime: Long,
                      useTimes: Long) {
        when (open) {
            true -> {
                // insert db
                Thread(Runnable {
                    val httpLog = HttpLog()
                    httpLog.requestUrl = requestUrl
                    httpLog.requestMethod = requestMethod
                    httpLog.requestParamsJson = requestParams.toString()
                    httpLog.requestHeaderJson = requestHeaders.toString()
                    httpLog.responseJson = responseJson
                    httpLog.requestTime = requestTime
                    httpLog.useTimes = useTimes
                    val newId = Database.getInstance().insert(httpLog)
                    Logger.v("LogSDK insert success, _id: $newId")
                }).start()
            }
            false -> Logger.v("LogSDK is not open")
        }

    }

}