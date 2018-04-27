package com.orliu.kotlin.common.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.ProgressBar

/**
 * Created by orliu on 12/04/2018.
 */
class ProgressWithPrecent : ProgressBar {

    private lateinit var mPaint: Paint
    private var progressText: String = ""
    private var progressIndex: Int = 0

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.color = Color.WHITE
    }

    override fun setProgress(progress: Int) {
        progressIndex = progress
        progressText = progressIndex.toString().plus("/").plus(max)
        super.setProgress(progress)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val rect = Rect()
        mPaint.getTextBounds(progressText, 0, progressText.length, rect)

        val x = width.div(max).times(progressIndex).minus(rect.width().plus(10)).toFloat()
        val y = height.div(2).minus(rect.centerY()).toFloat()
        canvas?.let {
            it.drawText(progressText, x, y, mPaint)
        }
    }
}