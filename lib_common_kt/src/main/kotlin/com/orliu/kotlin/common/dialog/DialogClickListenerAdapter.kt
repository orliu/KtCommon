package com.orliu.kotlin.common.dialog

/**
 * Created by orliu on 2018/2/24.
 */
abstract class DialogClickListenerAdapter: DialogClickListener {

    override fun onNegativeClick() {}

    override fun onPositiveClick() {}

    override fun onBottomViewClick(widgetId: Int) {}
}