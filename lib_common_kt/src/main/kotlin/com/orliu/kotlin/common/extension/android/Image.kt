package com.orliu.kotlin.common.extension.android

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

/**
 * 图片相关
 * Created by liujianping on 17/11/10.
 */

/**
 * decodeStream读取资源，减少BitmapFactory.createBitmap内存占用
 *
 * @param buf
 * @return
 */
fun decodeStream(buf: ByteArray): Bitmap {
    val opt = BitmapFactory.Options()
    opt.inPreferredConfig = Bitmap.Config.RGB_565
    opt.inPurgeable = true
    opt.inInputShareable = true

    val inputStream = ByteArrayInputStream(buf)
    val bm = BitmapFactory.decodeStream(inputStream)

    try {
        inputStream?.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return bm
}

/**
 * decodeStream读取资源，减少BitmapFactory.createBitmap内存占用
 *
 * @param buf
 * @return
 */
fun decodeStream(buf: ByteArray, width: Int, height: Int): Bitmap {
    val opt = BitmapFactory.Options()
    opt.inPreferredConfig = Bitmap.Config.RGB_565
    opt.inJustDecodeBounds = true

    BitmapFactory.decodeByteArray(buf, 0, buf.size, opt)
    opt.inSampleSize = calculateInSampleSize(opt, width, height)
    opt.inJustDecodeBounds = false
    opt.inPurgeable = true
    opt.inInputShareable = true

    val inputStream = ByteArrayInputStream(buf)
    val bm = BitmapFactory.decodeStream(inputStream)

    try {
        inputStream?.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return bm
}

/**
 * decodeStream读取资源，减少BitmapFactory.createBitmap内存占用
 *
 * @param context
 * @param resId
 * @return
 */
fun Context.decodeStream(resId: Int): Bitmap {
    val opt = BitmapFactory.Options()
    opt.inPreferredConfig = Bitmap.Config.RGB_565
    opt.inPurgeable = true
    opt.inInputShareable = true

    val inputStream = resources.openRawResource(resId)
    val bm = BitmapFactory.decodeStream(inputStream)

    try {
        inputStream?.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return bm
}

/**
 * decodeStream读取资源，减少BitmapFactory.createBitmap内存占用
 *
 * @param context
 * @param resId
 * @param width
 * @param height
 * @return
 */
fun Context.decodeStream(resId: Int, width: Int, height: Int): Bitmap {
    val opt = BitmapFactory.Options()
    opt.inPreferredConfig = Bitmap.Config.RGB_565
    opt.inJustDecodeBounds = true

    BitmapFactory.decodeResource(resources, resId, opt)
    opt.inSampleSize = calculateInSampleSize(opt, width, height)
    opt.inJustDecodeBounds = false

    opt.inPurgeable = true
    opt.inInputShareable = true

    val inputStream = resources.openRawResource(resId)
    val bm = BitmapFactory.decodeStream(inputStream, null, opt)

    try {
        inputStream?.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return bm
}

/**
 * decodeStream读取资源，减少BitmapFactory.createBitmap内存占用
 *
 * @return
 */
fun File.decodeStream(): Bitmap? {
    return try {
        val opt = BitmapFactory.Options()
        opt.inPreferredConfig = Bitmap.Config.RGB_565
        opt.inPurgeable = true
        opt.inInputShareable = true
        BitmapFactory.decodeStream(this.inputStream(), null, opt)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
        null
    }
}

/**
 * decodeStream读取资源，减少BitmapFactory.createBitmap内存占用
 *
 * @param file
 * @param width
 * @param height
 * @return
 */
fun File.decodeStream(width: Int, height: Int): Bitmap? {
    return try {
        val opt = BitmapFactory.Options()
        opt.inPreferredConfig = Bitmap.Config.RGB_565
        opt.inJustDecodeBounds = true

        BitmapFactory.decodeFile(this.absolutePath, opt)
        opt.inSampleSize = calculateInSampleSize(opt, width, height)
        opt.inJustDecodeBounds = false
        opt.inPurgeable = true
        opt.inInputShareable = true

        BitmapFactory.decodeStream(this.inputStream(), null, opt)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
        null
    }
}

/**
 * Bitmap比例压缩
 *
 * @param options
 * @param reqWidth
 * @param reqHeight
 * @return
 */
private fun calculateInSampleSize(options: BitmapFactory.Options,
                                  reqWidth: Int, reqHeight: Int): Int {
    val width = options.outWidth
    val height = options.outHeight
    var inSampleSize = 1

    if (width > reqWidth || height > reqHeight) {
        val suitedValue = if (reqHeight > reqWidth) reqHeight else reqWidth
        val widthRatio = width.toFloat() / suitedValue.toFloat()
        val heightRatio = height.toFloat() / suitedValue.toFloat()

        inSampleSize = Math.max(widthRatio, heightRatio).toInt()
    }

    inSampleSize = Math.max(1, inSampleSize)

    return inSampleSize
}

