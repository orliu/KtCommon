package com.orliu.kotlin.common.view.rv

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet

/**
 * Created by orliu on 02/04/2018.
 */
class VerticalLinearLayoutManager : LinearLayoutManager {
    private var isScrollEnable = true

    constructor(context: Context?) : super(context)
    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun canScrollVertically(): Boolean {
        return super.canScrollVertically() && isScrollEnable
    }

    fun setScrollEnable(enable: Boolean) {
        isScrollEnable = enable
    }
}