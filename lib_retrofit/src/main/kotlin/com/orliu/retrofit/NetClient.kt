package com.orliu.retrofit

import android.util.Log
import okhttp3.logging.HttpLoggingInterceptor
import com.google.gson.GsonBuilder
import com.orliu.retrofit.ssl.SSLSocketFactory
import com.orliuœ.retrofit.ssl.NullHostNameVerifer
import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


/**
 * Created by liujianping on 09/03/2018.
 */
object NetClient {
    private val TAG = NetClient::class.java.name

    private var mConnectTimtout = 1000L.times(5)
    private var mWriteTimeout = 1000L.times(5)
    private var mReadTimeout = 1000L.times(5)

    private val mIntercepors = arrayListOf<Interceptor>()
    private var mBaseUrl: String = ""
    private var isSupportSSL: Boolean = false

    private val okHttpBuilder = OkHttpClient.Builder()
    private val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    private lateinit var retrofit: Retrofit

    fun baseUrl(url: String): NetClient {
        mBaseUrl = url
        if (mBaseUrl.isEmpty()){
            Log.e(TAG, "url is empty")
        } else if (!mBaseUrl.endsWith("/")) {
            mBaseUrl += "/"
        }
        return this
    }

    fun connectTimeout(connectTimeout: Long): NetClient {
        mConnectTimtout = connectTimeout
        return this
    }

    fun writeTimeout(writeTimeout: Long): NetClient {
        mWriteTimeout = writeTimeout
        return this
    }

    fun readTimeout(readTimeout: Long): NetClient {
        mReadTimeout = readTimeout
        return this
    }

    fun isSupportSSL(support: Boolean): NetClient {
        isSupportSSL = support
        return this
    }

    fun addInterceptor(interceptor: Interceptor): NetClient {
        mIntercepors.add(interceptor)
        return this
    }

    /**
     * 创建自定义service
     *
     * @param clazz
     * @param <T>
     * @return
    </T> */
    fun <T> create(clazz: Class<T>): T {
        okHttpBuilder.connectTimeout(mConnectTimtout, TimeUnit.MILLISECONDS)
        okHttpBuilder.writeTimeout(mWriteTimeout, TimeUnit.MILLISECONDS)
        okHttpBuilder.readTimeout(mReadTimeout, TimeUnit.MILLISECONDS)
        mIntercepors.forEach { okHttpBuilder.addInterceptor(it) }
        okHttpBuilder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        if (isSupportSSL) {
            okHttpBuilder.sslSocketFactory(SSLSocketFactory.getSSLSocketFactory())
            okHttpBuilder.hostnameVerifier(NullHostNameVerifer())
        }

        retrofit = retrofitBuilder
                .client(okHttpBuilder.build())
                .baseUrl(mBaseUrl)
                .build()
        return retrofit.create(clazz)
    }
}