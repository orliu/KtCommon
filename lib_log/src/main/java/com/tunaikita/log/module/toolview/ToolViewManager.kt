package com.tunaikita.log.module.toolview

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import com.orliu.kotlin.common.extension.android.targetActivity
import com.orliu.kotlin.common.tools.screenHeightPixels
import com.orliu.kotlin.common.tools.screenWidthPixels
import com.tunaikita.log.module.httplog.HttpLogActivity

/**
 * ToolView悬浮按钮管理
 * Created by liujianping on 17/11/29.
 */
object ToolViewManager {
    private lateinit var wmParams: WindowManager.LayoutParams
    private var wm: WindowManager? = null
    private lateinit var toolView: ToolView

    private var x = 0.0F
    private var y = 0.0F
    private var xTemp = 0.0F
    private var yTemp = 0.0F
    private var mTouchStartX = 0.0F
    private var mTouchStartY = 0.0F

    fun initView(context: Context) {
        wmParams = WindowManager.LayoutParams()
        wmParams.type = WindowManager.LayoutParams.TYPE_TOAST
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        wmParams.gravity = Gravity.LEFT or Gravity.TOP
        wmParams.x = context.screenWidthPixels().times(0.8).toInt()
        wmParams.y = context.screenHeightPixels().times(0.1).toInt()
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        wmParams.format = PixelFormat.RGBA_8888

        toolView = ToolView(context)
        toolView.setOnTouchListener(ToolViewOnTouchListener(context))
        if (wm == null)
            wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm?.addView(toolView, wmParams)
    }

    fun close() {
        wm?.removeView(toolView)
        wm = null
    }

    /**
     * 按钮拖动及点击事件
     */
    private class ToolViewOnTouchListener : View.OnTouchListener {
        private var context: Context? = null

        constructor(context: Context) {
            this.context = context
        }

        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            if (v != null && event != null) {

                x = event.rawX
                y = event.rawY - 25


                when (event.action) {

                    MotionEvent.ACTION_DOWN -> {
                        mTouchStartX = event.x
                        mTouchStartY = event.y + v.height / 2


                        xTemp = x
                        yTemp = y
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val dx = (event.rawX - x).toInt()
                        val dy = (event.rawY - y).toInt()
                        var l = v.left + dx
                        var b = v.bottom + dy
                        var r = v.right + dx
                        var t = v.top + dy

                        if (l < 0 || t < 0 || r > context!!.screenWidthPixels() || b > context!!.screenHeightPixels()) {
                            updateViewPosition(v, 100.0F, 100.0F)
                        } else {
                            updateViewPosition(v)
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        x = event.rawX
                        y = event.rawY - 25
                        if (Math.abs(x - xTemp) < 6 && Math.abs(y - yTemp) < 6) {

                            // 较小的移动范围，视为click事件
                            context?.targetActivity<HttpLogActivity>(Intent.FLAG_ACTIVITY_NEW_TASK)
                        } else {

                            // 拖动
                            updateViewPosition(v)
                            mTouchStartX = 0.0F
                            mTouchStartY = 0.0F
                        }
                    }
                }
            }

            return true
        }

    }

    private fun updateViewPosition(view: View) {
        wmParams.x = (x - mTouchStartX).toInt()
        wmParams.y = (y - mTouchStartY).toInt()
        wm?.updateViewLayout(view, wmParams)
    }

    private fun updateViewPosition(view: View, x: Float, y: Float) {
        wmParams.x = x.toInt()
        wmParams.y = y.toInt()
        wm?.updateViewLayout(view, wmParams)
    }
}