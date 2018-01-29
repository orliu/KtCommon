package com.orliu.kotlin

import android.support.v7.widget.LinearLayoutManager
import com.orliu.kotlin.base.BaseActivity
import com.orliu.kotlin.common.view.rv.ItemViewDelegate
import com.orliu.kotlin.common.view.rv.RecyclerAdapter
import com.orliu.kotlin.common.view.rv.ViewHolder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    private lateinit var mAdapter: RecyclerAdapter<String>

    override fun getTitleStr(): String? = "Main Act"

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initBundleArgs() {
    }

    override fun initDataOnStart() {
    }

    override fun initViewOnResume() {
        val manager = LinearLayoutManager(this)
        mAdapter = RecyclerAdapter(this)
        rv.layoutManager = manager
        rv.adapter = mAdapter
        mAdapter.addItemViewDelegate(NormalItemViewDelegate())
    }

    override fun syncDataOnResume() {

        val datas = arrayListOf<String>()
        (0..50).forEach { datas.add(it.toString()) }
        mAdapter.setData(datas)
    }

    private class NormalItemViewDelegate : ItemViewDelegate<String> {
        override fun getItemViewLayoutId() = R.layout.item_normal

        override fun isForViewType(item: String, position: Int) = true

        override fun convert(holder: ViewHolder, t: String, position: Int) {
            holder.withView(R.id.tv).setText(t)
        }

    }

}
