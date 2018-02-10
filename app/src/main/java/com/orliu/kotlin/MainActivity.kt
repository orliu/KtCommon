package com.orliu.kotlin

import android.graphics.Color
import com.orliu.kotlin.base.BaseActivity
import com.orliu.kotlin.common.extension.android.setColorSpannableString
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun getTitleStr(): String? = "Main Act"

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
        id_tv.setColorSpannableString(origin,map)
    }

    override fun syncDataOnResume() {
    }


}
