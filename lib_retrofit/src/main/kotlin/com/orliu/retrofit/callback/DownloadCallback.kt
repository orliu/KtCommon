package com.orliu .retrofit.callback

import java.io.File


/**
 * Created by liujianping on 09/03/2018.
 */
interface DownloadCallback {

    fun onDownLoading(progress: Int)

    fun onDownLoadFinish(downloadFile: File)

    fun onDownLoadError(error: String)
}