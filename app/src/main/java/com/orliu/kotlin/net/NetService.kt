package com.orliu.kotlin.net

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * Created by liujianping on 09/03/2018.
 */
interface NetService {

    @GET
    fun getOriginalString(@Url url: String) : Observable<String>
}