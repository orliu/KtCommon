package com.orliu.kotlin

import android.graphics.Color
import com.orliu.kotlin.base.BaseActivity
import com.orliu.kotlin.common.dialog.DialogClickListenerAdapter
import com.orliu.kotlin.common.dialog.DialogUtils
import com.orliu.kotlin.common.extension.android.setColorSpannableString
import com.orliu.kotlin.common.extension.android.toastShort
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class MainActivity : BaseActivity() {

    override fun getTitleStr(): String = "Main Act"

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initBundleArgs() {
    }

    override fun initDataOnStart() {
    }

    override fun initViewOnResume() {
        val origin = "细心的读者可能已经注意到一个事实：习近平主席此前4场会晤的外国客人也都来自欧洲——法国总统马克龙、北欧和波罗的海国家议会领导人、英国前首相卡梅伦、英国首相特雷莎·梅。"
        val map = hashMapOf<Int, IntArray>()
        map[Color.BLACK] = intArrayOf(2, 4)
        map[Color.RED] = intArrayOf(5, 7)
        map[Color.YELLOW] = intArrayOf(8, 10)
        map[Color.BLUE] = intArrayOf(10, 11)
        map[Color.RED] = intArrayOf(13, origin.length)
        id_tv.setColorSpannableString(origin, map)

        id_btn.onClick {
            DialogUtils.showBottom(supportFragmentManager,
                    R.layout.bottom_test,
                    intArrayOf(R.id.id_test1, R.id.id_test3),
                    object : DialogClickListenerAdapter() {
                        override fun onBottomViewClick(widgetId: Int) {
                            when (widgetId) {
                                R.id.id_test1 -> toastShort("test 1")
                                R.id.id_test3 -> toastShort("test 3")
                                else -> Unit
                            }
                        }
                    })

        }
    }

    override fun syncDataOnResume() {
    }


}
