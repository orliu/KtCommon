package com.tunaikita.log.module.httplog

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.gson.Gson
import com.orliu.kotlin.common.base.BaseResult
import com.orliu.kotlin.common.extension.other.parseJson
import com.orliu.kotlin.common.tools.Logger
import com.tunaikita.log.R
import com.tunaikita.log.bean.HttpLog
import kotlinx.android.synthetic.main.activity_httplog_details.*
import org.jetbrains.anko.textColor
import java.text.SimpleDateFormat
import java.util.*

/**
 * HttpLog日志详情页
 * Created by orliu on 17/11/30.
 */
class HttpLogDetailsActivity : AppCompatActivity() {

    // params
    private var httpLog: HttpLog? = null

    private val sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_httplog_details)

        intent.extras?.let {
            it["httpLog"]?.let { httpLog = it as HttpLog }
        }

        if (httpLog == null) Logger.e("httpLog: $httpLog")
    }

    override fun onResume() {
        super.onResume()

        httpLog?.apply {

            val sb = StringBuffer()
            sb.append("_id: ").append(_id).append("\n")
                    .append("requestTime: ").append(sdf.format(Date(requestTime))).append("\n")
                    .append("duration: ").append(useTimes).append("ms\n")
                    .append("method: ").append(requestMethod).append("\n")
                    .append("requestUrl: ").append(requestUrl).append("\n")
                    .append("requestParams: ").append(requestParamsJson).append("\n")
                    .append("requestHeaders: ").append(requestHeaderJson).append("\n")
                    .append("response: ").append(responseJson)
            id_httplog_details.text = sb.toString()

            val result: BaseResult<*> = Gson().parseJson(responseJson)
            when (result.errcode) {
                BaseResult.SERVER_SUCCESS -> {
                    if (useTimes >= 500) {
                        id_httplog_details.textColor = resources.getColor(android.R.color.holo_orange_dark)
                    } else {
                        id_httplog_details.textColor = resources.getColor(android.R.color.holo_blue_dark)
                    }
                }
                else -> id_httplog_details.textColor = resources.getColor(android.R.color.holo_red_dark)
            }

        }
    }
}