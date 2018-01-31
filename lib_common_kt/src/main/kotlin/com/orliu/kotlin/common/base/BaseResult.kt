package com.orliu.kotlin.common.base

import java.io.Serializable

/**
 * Created by liujianping on 26/01/2018.
 */
data class BaseResult<out T>(val errcode: Int,
                         val msg: String?,
                         val data: T?) : Serializable {
    companion object {
        val SUCCESS = 0
        val TOKEN_NONE = 11
        val TOKEN_EXPIRES = 12
    }
}