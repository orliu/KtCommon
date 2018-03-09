//package com.orliu.kotlin.retrofit.model
//
//import android.content.ContentValues
//import okhttp3.ResponseBody
//import android.net.Uri
//import android.util.Log
//import com.orliu.retrofit.NetClient
//import com.orliu.retrofit.callback.DownloadCallback
//import com.orliu.retrofit.extension.request
//import com.orliu.retrofit.service.DownloadService
//import io.reactivex.Observer
//import io.reactivex.disposables.Disposable
//import java.io.*
//
//
///**
// * Created by liujianping on 09/03/2018.
// */
//class DownloadManager private constructor() {
//
//    fun start(url: String, savePath: String, saveName: String, callback: DownloadCallback) {
//        val uri = Uri.parse(url)
//        val baseUrl = "http://" + uri.getHost() + "/"
//        var getUrl = uri.getPath()
//        getUrl = getUrl.substring(1, getUrl.length)
//
//        Log.e(TAG, baseUrl + getUrl)
//        Log.e(TAG, savePath + File.separator + saveName)
//
//        NetClient.baseUrl(baseUrl)
//                .create(DownloadService::class.java)
//                .downloadFile(getUrl)
//                .request(object : Observer<ResponseBody> {
//                    override fun onComplete() {
//
//                    }
//
//                    override fun onSubscribe(d: Disposable) {
//                    }
//
//                    override fun onError(e: Throwable?) {
//                    }
//
//                    override fun onNext(value: ResponseBody) {
//                        Thread { writeResponseBodyToDisk(savePath, saveName, value, callback) }.start()
//                    }
//                })
//
//
//    }
//
//    private val TAG = DownloadManager::class.java.name
//
//    companion object {
//
//        @JvmStatic
//        fun instance() = Holder.instance
//    }
//
//    private object Holder {
//        val instance = DownloadManager()
//    }
//
//    private val suffies = arrayOf(".apk", ".png", ".jpg")
//
//    /**
//     * 写文件，回调进度
//     *
//     * @param body
//     */
//    private fun writeResponseBodyToDisk(savePath: String, saveName: String, body: ResponseBody, callback: DownloadCallback?) {
//        var savePathCopy = savePath
//
//        val fileSuffix = saveName.substring(saveName.length - 4, saveName.length)
//
//        if (!suffies.contains(fileSuffix)) {
//            Log.e(ContentValues.TAG, "not support the file's suffix yet")
//            return
//        }
//
//        if (savePathCopy.endsWith("/"))
//            savePathCopy = savePathCopy.substring(0, savePathCopy.length - 1)
//
//
//        try {
//            val folder = File(savePathCopy)
//            if (!folder.exists()) {
//                folder.mkdirs()
//            }
//
//            val existsFile = File(savePathCopy, saveName)
//            if (existsFile.exists()) {
//                callback?.onDownLoadFinish(existsFile)
//                return
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//        val file = File(savePathCopy, saveName + ".temp")
//        val downloadBytes = file.length()
//        var fos: RandomAccessFile? = null
//        try {
//            fos = RandomAccessFile(file, "rw")
//            fos.seek(downloadBytes)
//        } catch (e1: FileNotFoundException) {
//            e1.printStackTrace()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//
//        var inputStream: InputStream? = null
//        try {
//
//            inputStream = body.byteStream()
//            val buf = ByteArray(2048)
//            var len = -1
//            var totalSize = body.contentLength()
//            var receivedSize: Long = 0
//            var progress = 0
//            if (downloadBytes != 0L) {
//                receivedSize = downloadBytes
//                totalSize += downloadBytes
//            }
//
//            while ((len = inputStream.read(buf)) != -1) {
//                fos!!.write(buf, 0, len)
//
//                // 计算进度
//                receivedSize += len.toLong()
//                val percent = (receivedSize / totalSize.toFloat() * 100).toInt()
//
//                // 减少通知频率
//                if (progress < percent) {
//                    progress = percent
//
//                    Log.e(ContentValues.TAG, progress.toString() + " %")
//                    // 回调下载进度
//                    callback?.onDownLoading(progress)
//                }
//
//                // 下载完成
//                if (progress == 100) {
//                    val finalFile = File(savePathCopy, saveName)
//                    file.renameTo(finalFile)
//
//                    // 回调下载文件
//                    callback?.onDownLoadFinish(finalFile)
//                }
//
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            callback?.onDownLoadError(e.message ?: "")
//        } finally {
//            inputStream?.let { it.close() }
//        }
//    }
//}