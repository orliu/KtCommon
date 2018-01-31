package com.orliu.kotlin.common.view.rv

/**
 * Created by liujianping on 29/01/2018.
 */
interface ItemViewDelegate<T> {

    fun getItemViewLayoutId(): Int
    fun isForViewType(item: T, position: Int): Boolean
    fun convert(holder: ViewHolder, item: T, position: Int)

}