package com.orliu.retrofit.interceptor

import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Response
import okio.GzipSink
import okio.Okio
import okio.BufferedSink
import okhttp3.RequestBody
import java.io.IOException


/**
 * Created by orliu on 09/03/2018.
 */
class GzipInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        if (originalRequest.body() == null || originalRequest.header("Content-Encoding") != null) {
            return chain.proceed(originalRequest)
        }
        val compressedRequest = originalRequest.newBuilder()
                .header("Content-Encoding", "gzip")
                .method(originalRequest.method(), gzip(originalRequest.body()!!))
                .build()
        return chain.proceed(compressedRequest)
    }

    private fun gzip(body: RequestBody): RequestBody {
        return object : RequestBody() {
            override fun contentType(): MediaType? {
                return body.contentType()
            }

            @Throws(IOException::class)
            override fun contentLength(): Long {
                return -1
            }

            @Throws(IOException::class)
            override fun writeTo(sink: BufferedSink) {
                val gzipSink = Okio.buffer(GzipSink(sink))
                body.writeTo(gzipSink)
                gzipSink.close()
            }
        }
    }
}