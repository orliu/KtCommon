package com.orliu.kotlin.common.view.span

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.TextView

/**
 * Created by orliu on 03/04/2018.
 */
class SpanClickTextView : TextView {

    var isContentClicked = true

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun performClick(): Boolean {
        if (isContentClicked) return true
        return super.performClick()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        isContentClicked = false
        return super.onTouchEvent(event)
    }
}