package com.orliu.retrofit.interceptor

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by orliu on 09/03/2018.
 */
class BaseInterceptor : Interceptor {

    private var mHeaders: HashMap<String, String>? = null


    fun BaseInterceptor(addHeaders: HashMap<String, String>) {
        mHeaders = addHeaders
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        //        Log.e(TAG, request.url().toString());

        val builder = chain.request().newBuilder()
        mHeaders?.let {
           for ((key, value) in it){
                builder.addHeader(key, value).build()
           }
        }

        return chain.proceed(builder?.build())
    }
}