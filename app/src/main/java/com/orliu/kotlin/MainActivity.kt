package com.orliu.kotlin

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import com.orliu.kotlin.base.BaseActivity
import com.orliu.kotlin.common.dialog.CommonDialog
import com.orliu.kotlin.common.view.rv.ItemViewDelegate
import com.orliu.kotlin.common.view.rv.RecyclerViewLayout
import com.orliu.kotlin.common.view.rv.ViewHolder
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk25.coroutines.onClick


class MainActivity : BaseActivity(), OnRefreshListener, OnLoadMoreListener {

    override fun getTitleStr() = "1231231"

    override fun getLayoutId() = R.layout.activity_main

    override fun initDataOnCreate() {

    }

    override fun initView() {

        id_dialog.onClick {
            CommonDialog.newInstance()
                    .setArguments("刷新数据")
                    .setOnClickListener(object : CommonDialog.OnClickListenerAdapter() {
                        override fun onConfirm() {
                            loadData()
                        }
                    }).show(supportFragmentManager, "")
        }
    }

    override fun initDataOnResume() {
    }

    fun loadData() {

        val layout = findViewById<RecyclerViewLayout<Int>>(R.id.id_rv_layout)
        with(layout) {
            setOnRefreshListener(this@MainActivity)
            setOnLoadMoreListener(this@MainActivity)
            setLayoutManager(GridLayoutManager(this@MainActivity, 3))
            addItemViewDelegate(ItemN(), ItemN2())

            val list = arrayListOf<Int>()
            (0..8).forEach { list.add(it) }
            setData(list)
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
    }

    private class ItemN : ItemViewDelegate<Int> {
        override fun getItemViewLayoutId() = R.layout.item_normal

        override fun isForViewType(item: Int, position: Int) = item % 3 == 0

        override fun convert(holder: ViewHolder, item: Int, position: Int) {
            holder.withView(R.id.tv).setText(item.toString())
        }
    }

    private class ItemN2 : ItemViewDelegate<Int> {
        override fun getItemViewLayoutId() = R.layout.item_normal_2

        override fun isForViewType(item: Int, position: Int) = item % 3 != 0

        override fun convert(holder: ViewHolder, item: Int, position: Int) {
            holder.withView(R.id.tv).setText(item.toString())
        }
    }
}
