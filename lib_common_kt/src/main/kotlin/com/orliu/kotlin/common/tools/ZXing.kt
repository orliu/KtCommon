package com.orliu.kotlin.common.tools

import android.graphics.Bitmap
import android.text.TextUtils
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel


/**
 * ZXing图形码
 * compile 'com.google.zxing:core:3.3.1'
 * Created by liujianping on 30/01/2018.
 */

//  生成条形码
fun buildBarCodeImage(content: String, imageWidth: Int, imageHeight: Int): Bitmap? {
    return buildImageCode(BarcodeFormat.CODE_128, content, imageWidth, imageHeight)
}

//  生成二维码
fun buildQRCodeImage(content: String, size: Int): Bitmap? {
    return buildImageCode(BarcodeFormat.QR_CODE, content, size, size)
}

//  生成图形码
private fun buildImageCode(format: BarcodeFormat, content: String, imageWidth: Int, imageHeight: Int): Bitmap? {
    try {
        if (TextUtils.isEmpty(content)) {
            Logger.e("content is null")
            return null
        }
        if (imageWidth <= 0 || imageHeight <= 0) {
            Logger.e("please confirm barWidth & barHeight first")
            return null
        }

        //配置参数
        val hints = hashMapOf<EncodeHintType, Any>()
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8")
        //容错级别
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H)
        //设置空白边距的宽度
        hints.put(EncodeHintType.MARGIN, 0)

        // 编码指定大小
        val matrix = MultiFormatWriter().encode(content, format, imageWidth, imageHeight, hints)
        val width = matrix.getWidth()
        val height = matrix.getHeight()
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            for (x in 0 until width) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = -0x1000000
                }
            }
        }

        // 通过像素数组生成bitmap
        val bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }

}