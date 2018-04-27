package com.orliu.kotlin.common.extension.android

import android.content.Context
import android.os.Environment

/**
 * 文件操作
 * Created by orliu on 17/11/10.
 */

fun Context.saveDir(): String =
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED)
            Environment.getExternalStorageDirectory().absolutePath
        else {
            this.cacheDir.absolutePath
        }


fun Context.saveDir(appendPath: String, saveName: String): String {
    val sb = StringBuilder()
    sb.append(saveDir())

    if (!appendPath.startsWith(java.io.File.separator)) {
        sb.append(java.io.File.separator)
    }
    sb.append(appendPath)

    val dirPath = sb.toString()
    val dirs = java.io.File(dirPath)
    if (!dirs.exists())
        dirs.mkdirs()

    if (!appendPath.endsWith(java.io.File.separator)) {
        sb.append(java.io.File.separator)
    }
    sb.append(saveName)

    return sb.toString()
}
