package com.orliu.kotlin

import android.support.v7.widget.GridLayoutManager
import com.orliu.kotlin.base.BaseActivity
import com.orliu.kotlin.common.dialog.CommonDialog
import com.orliu.kotlin.common.view.rv.ItemViewDelegate
import com.orliu.kotlin.common.view.rv.RecyclerLayout
import com.orliu.kotlin.common.view.rv.ViewHolder
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk25.coroutines.onClick


class MainActivity : BaseActivity(), OnRefreshListener, OnLoadMoreListener {

    private lateinit var layout: RecyclerLayout<Int>

    override fun getTitleStr() = "1231231"

    override fun getLayoutId() = R.layout.activity_main

    override fun initDataOnCreate() {
        layout = findViewById(R.id.id_rv_layout)
    }

    override fun initView() {

        id_show_data.onClick {
            CommonDialog.newInstance()
                    .setArguments("刷新数据")
                    .setOnClickListener(object : CommonDialog.OnClickListenerAdapter() {
                        override fun onConfirm() {
                            loadData()
                        }
                    }).show(supportFragmentManager, "")
        }

        id_add_data.onClick {
            layout.addDatas(arrayListOf(11,22,333,44))
        }

        id_add_data_2.onClick {
            layout.addData(888, 2)
        }

        id_change_data.onClick {
            layout.updateData(2222, 0)
        }

        id_show_nodata.onClick {
            layout.setErrorMessage("nonono data")
            layout.setData(arrayListOf())
        }

        id_show_nomoredata.onClick {
            layout.setErrorMessage("nonono data", "no more more more more data")
            layout.addDatas(arrayListOf())
        }
    }

    override fun initDataOnResume() {
    }

    fun loadData() {

        with(layout) {
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
