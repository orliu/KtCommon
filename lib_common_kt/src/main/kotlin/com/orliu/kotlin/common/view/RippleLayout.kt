package com.orliu.kotlin.common.view

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.RelativeLayout
import org.jetbrains.anko.backgroundDrawable

/**
 * RippleLayout: it's working when the sdk code above LOLLIPOP
 *
 * e.g: <RippleLayout....android:id="@+id/rippleLayout">.......</RippleLayout>
 * Created by orliu on 17/12/6.
 */
class RippleLayout : RelativeLayout {

    constructor(context: Context?) : super(context) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val attrs = intArrayOf(android.R.attr.selectableItemBackground)
            if (attrs.size == 1) {
                backgroundDrawable = context.theme.obtainStyledAttributes(attrs).getDrawable(0)
            }
        }
    }
}