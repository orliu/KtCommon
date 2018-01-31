package com.orliu.kotlin.common.extension.android

import android.content.Context
import android.widget.Toast

/**
 * Toast扩展函数
 * Created by liujianping on 17/10/24.
 */
fun Context.toastShort(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
fun Context.toastLong(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()