package com.orliu.kotlin.common.base

import java.io.Serializable

/**
 * Created by liujianping on 26/01/2018.
 */
data class BaseResult<T>(var errcode: Int,
                         var msg: String,
                         var data: T?) : Serializable {

    companion object {
        const val UNKOWN_REASON = -4
        const val EXCEPTION = -3
        const val HTTP_EXCEPTION = -2
        const val TIME_OUT = -1
        const val SERVER_SUCCESS = 0
        const val SERVER_TOKEN_MISSING = 11
        const val SERVER_TOKEN_EXPIRED = 12


        fun unkownError(): BaseResult<*> = BaseResult(UNKOWN_REASON, "unkown error", null)
        fun exception(error: String?): BaseResult<*> {
            var msg = "unkown exception"
            error?.let {
                if (it.isNotEmpty()) msg = it
            }
            return BaseResult(EXCEPTION, msg, null)
        }

        fun httpException(error: String?): BaseResult<*> {
            var msg = "unkown http exception"
            error?.let {
                if (it.isNotEmpty()) msg = it
            }
            return BaseResult(HTTP_EXCEPTION, msg, null)
        }

        fun timeout(): BaseResult<*> = BaseResult(TIME_OUT, "Time out", null)

        fun tokenMissing(): BaseResult<*> = BaseResult(SERVER_TOKEN_MISSING, "AccessToken params was missing", null)
        fun tokenExpired(): BaseResult<*> = BaseResult(SERVER_TOKEN_EXPIRED, "AccessToken expired", null)
        fun unkownServerError(errcode: Int): BaseResult<*> = BaseResult(errcode, "unkown server error ($errcode)", "")
        fun originString(origin: String): BaseResult<String> = BaseResult(SERVER_SUCCESS, "", origin)
    }
}