package com.orliu.kotlin.common.tools

import java.util.regex.Pattern

/**
 * 正则相关
 * Created by orliu on 17/11/7.
 */

/**
 * 纯数字校验 for indonesia cellphone
 *
 * @param mobiles
 * @param minLength
 * @param maxLength
 * @return
 */
fun String.verifyIndonesiaPhoneNumber(minLength: Int, maxLength: Int): Boolean {
    return if (this.isEmpty()) {
        false
    } else {
        var regex = "^[0-9]{$minLength,$maxLength}$"
        val p = Pattern.compile(regex)
        val m = p.matcher(this)
        m.matches()
    }
}
