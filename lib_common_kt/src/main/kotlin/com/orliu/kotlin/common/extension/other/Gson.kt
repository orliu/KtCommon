package com.orliu.kotlin.common.extension.other

import com.google.gson.Gson

/**
 * Gson扩展函数
 * Created by liujianping on 29/01/2018.
 */
// 泛型解释
inline fun <reified T> Gson.parseJson(json: String): T {
    return fromJson(json, T::class.java)
}