package com.tunaikita.log.bean

import java.io.Serializable

/**
 * Created by liujianping on 17/11/30.
 */

data class Result<out T>(val errcode: Int,
                     val msg:String?,
                     val data: T?) : Serializable{
    companion object {
        val SUCCESS = 0
        val TOKEN_NONE = 11
        val TOKEN_EXPIRES = 12
    }
}