package com.orliu.kotlin.common.extension.android

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView

/**
 * TextView扩展函数
 * Created by orliu on 10/02/2018.
 */


/**
 * 为TextView设置多颜色字体
 * e.g:
 * val map = hashMapOf<Int, IntArray>()  // Int: ColorRes; IntArray: 要设置为ColorRes的字符串在originString中的起始位置
 * map[Color.BLACK] = intArrayOf(2, 4)
 * map[Color.RED] = intArrayOf(5, 7)
 * map[Color.YELLOW] = intArrayOf(8, 10)
 * map[Color.BLUE] = intArrayOf(10, 11)
 * map[Color.RED] = intArrayOf(13, origin.length)
 */
fun TextView.setColorSpannableString(originString: String, rangeParams: MutableMap<Int, IntArray>) {
    if (originString.isNotEmpty()) {
        val spannableString = SpannableString(originString)
        rangeParams.map {
            val span = ForegroundColorSpan(it.key)
            spannableString.setSpan(span, it.value[0], it.value[1], Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        }

        // text
        text = spannableString
    }
}