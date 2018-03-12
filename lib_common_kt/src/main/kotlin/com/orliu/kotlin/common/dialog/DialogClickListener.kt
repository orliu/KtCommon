package com.orliu.kotlin.common.dialog

/**
 * Created by orliu on 2018/2/24.
 */
interface DialogClickListener {
    fun onNegativeClick()

    fun onPositiveClick()

    fun onBottomViewClick(widgetId: Int)
}