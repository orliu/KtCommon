package com.orliu.kotlin.common.view.rv

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.orliu.kotlin.common.R
import com.orliu.kotlin.common.R.id.*
import com.orliu.kotlin.common.extension.android.gone
import com.orliu.kotlin.common.extension.android.toastShort
import com.orliu.kotlin.common.extension.android.visible
import com.orliu.kotlin.common.tools.Logger
import kotlinx.android.synthetic.main.layout_recycler_layout.view.*

/**
 * Created by orliu on 2018/6/25.
 */
class RecyclerLayout<T> : RelativeLayout {

    private var mNoDataMessage = "no data"
    private var mNoMoreDataMessage = "no more data"

    private var mLayoutManager: RecyclerView.LayoutManager? = null
    private lateinit var mAdapter: RecyclerAdapter<T>
    private lateinit var mDatas: ArrayList<T>


    @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr)

    init {
        initView()
    }

    private fun initView() {
        if (childCount > 0) removeAllViews()

        val view = LayoutInflater.from(context).inflate(R.layout.layout_recycler_layout, this, false)
        addView(view)

        mAdapter = RecyclerAdapter(context)
        mDatas = arrayListOf()
        mAdapter.setData(mDatas)
        id_recycler_view.adapter = mAdapter

        if (mLayoutManager == null)
            mLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        id_recycler_view.layoutManager = mLayoutManager

        id_recycler_error.text = mNoDataMessage

    }

    fun setLayoutManager(layoutManager: RecyclerView.LayoutManager) {
        mLayoutManager = layoutManager
        id_recycler_view.layoutManager = mLayoutManager
    }

    fun setErrorMessage(noDataMessage: String, noMoreDataMessage: String = "") {
        mNoDataMessage = noDataMessage
        if (noMoreDataMessage.isNotEmpty()) mNoMoreDataMessage = noMoreDataMessage
    }

    fun addItemViewDelegate(vararg itemViews: ItemViewDelegate<T>) {

        if (itemViews.isEmpty()) {
            Logger.e("item view delegate arrays is null")
        } else {
            itemViews.forEach { mAdapter.addItemViewDelegate(it) }
        }
    }

    fun setData(datas: ArrayList<T>) {
        mDatas.clear()
        when (datas.isEmpty()) {
            true -> showErrorView()
            false -> {
                mDatas.addAll(datas)
                showDataView()
            }
        }
    }

    fun addDatas(datas: ArrayList<T>?) {
        datas?.let {
            if (it.isNotEmpty()) {
                mDatas.addAll(datas)
                mAdapter.notifyDataSetChanged()
            } else {
                showErrorToast()
            }
        } ?: run { showErrorToast() }
    }

    fun addData(data: T?, position: Int = -1) {
        data?.let {
            if (position > -1) {
                mDatas.add(position, it)
                mAdapter.notifyItemInserted(position)
            } else {
                mDatas.add(it)
            }
        } ?: run { showErrorToast() }

    }

    fun updateData(data: T?, position: Int = -1) {
        data?.let {
            if (position in 0..mDatas.size) {
                mDatas[position] = data
                mAdapter.notifyItemChanged(position)
            } else {
                Logger.e("updateData: position is wrong")
            }
        } ?: run { showErrorToast() }

    }

    private fun showErrorToast() {
        context.toastShort(mNoMoreDataMessage)
    }

    fun getRecyclerView() = id_recycler_view

    fun getLayoutManager() = mLayoutManager

    fun getLayoutDatas() = mDatas

    private fun showDataView() {
        id_recycler_view.visible()
        id_recycler_progress.gone()
        id_recycler_error.gone()
    }

    private fun showErrorView() {
        id_recycler_view.gone()
        id_recycler_progress.gone()
        id_recycler_error.visible()
    }


    fun showError(error: String) {
        id_recycler_error.text = error
    }

    fun showLoadingView() {
        id_recycler_view.gone()
        id_recycler_progress.visible()
        id_recycler_error.gone()
    }

    fun hiddenLoading() {
        id_recycler_progress.gone()
    }
}