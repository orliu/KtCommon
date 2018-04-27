package com.orliu.kotlin.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ScrollView

/**
 * Created by orliu on 02/04/2018.
 */
class CustomScrollView : ScrollView {

    private var prevX: Int = 0
    private var prevY: Int = 0

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                prevX = ev.x.toInt()
                prevY = ev.y.toInt()
            }
            MotionEvent.ACTION_MOVE -> {
                val x = ev.x
                val y = ev.y

                val moveX = Math.abs(x - prevX)
                val moveY = Math.abs(y - prevY)

                if (moveX > moveY) return false
            }
        }
        return super.onInterceptTouchEvent(ev)
    }
}