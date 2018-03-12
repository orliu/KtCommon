package com.orliu.retrofit.service

import io.reactivex.Observable
import retrofit2.http.Url
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming



/**
 * 下载文件的Service
 * Created by orliu on 09/03/2018.
 */
interface DownloadService {

    @Streaming
    @GET
    fun downloadFile(@Url fileUrl: String): Observable<ResponseBody>
}