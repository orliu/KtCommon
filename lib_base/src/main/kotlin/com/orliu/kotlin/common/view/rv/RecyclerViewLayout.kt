package com.orliu.kotlin.common.view.rv

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.orliu.kotlin.common.R
import com.orliu.kotlin.common.extension.android.gone
import com.orliu.kotlin.common.extension.android.visible
import com.orliu.kotlin.common.tools.Logger
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.layout_recycler_view_container.view.*

/**
 * Created by orliu on 2018/6/3.
 */
class RecyclerViewLayout<T> : RelativeLayout {

    private lateinit var mAdapter: RecyclerAdapter<T>
    private lateinit var mDatas: ArrayList<T>
    private var orientation: Int = LinearLayoutManager.VERTICAL

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private fun initView() {
        if (childCount > 0) removeAllViews()

        // layout
        LayoutInflater.from(context).inflate(R.layout.layout_recycler_view_container, this, true)

        // adapter
        mAdapter = RecyclerAdapter(context)
        id_recycler_view.adapter = mAdapter
    }

    fun setOrientation(orientation: Int) {
        this.orientation = orientation
    }

    fun setLayoutManager(layoutManager: RecyclerView.LayoutManager){
        id_recycler_view.layoutManager = layoutManager
    }

    fun addItemViewDelegate(vararg itemViews: ItemViewDelegate<T>) {
        if (itemViews.isEmpty()) {
            Logger.e("item views delegate arrays is null")
        } else {
            itemViews.forEach { mAdapter.addItemViewDelegate(it) }
        }
    }

    fun setData(datas: ArrayList<T>) {
        mDatas = datas
        mAdapter.setData(mDatas)
        if (mDatas.isNotEmpty()) {
            showDataView()
        } else {
            showNoDataView()
        }
    }

    fun setOnRefreshListener(listener: OnRefreshListener) {
        id_refresh_layout.setOnRefreshListener(listener)
    }

    fun setOnLoadMoreListener(listener: OnLoadMoreListener) {
        id_refresh_layout.setOnLoadMoreListener(listener)
    }

    fun finishRefresh() {
        id_refresh_layout.finishRefresh()
    }

    fun finishLoadMore() {
        id_refresh_layout.finishLoadMore()
    }

    private fun showDataView() {
        id_recycler_view.visible()
        id_recycler_view_progress.gone()
        id_no_data.gone()
    }

    private fun showNoDataView() {
        id_recycler_view.gone()
        id_recycler_view_progress.gone()
        id_no_data.visible()
    }

    fun showLoading() {
        id_recycler_view.gone()
        id_recycler_view_progress.visible()
        id_no_data.gone()
    }

    fun hideLoading() {
        id_recycler_view_progress.gone()
    }
}