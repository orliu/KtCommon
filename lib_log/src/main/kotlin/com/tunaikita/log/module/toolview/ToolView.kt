package com.tunaikita.log.module.toolview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.tunaikita.log.R

/**
 * Tool view
 * Created by liujianping on 17/11/29.
 */
class ToolView : RelativeLayout {

    constructor(context: Context?) : super(context) {
        initView(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    private fun initView(context: Context?) {
        context?.let {
            val view = LayoutInflater.from(context).inflate(R.layout.layout_tool_view, null, false)
            addView(view)

            //id_tool_img.onClick { context.toastShort("tool view") }
        }
    }
}