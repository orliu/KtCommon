package com.orliu.kotlin.common.view.rv

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * Created by orliu on 2016/10/13.
 */

open class RecyclerAdapter<T>(private var mContext: Context) : RecyclerView.Adapter<ViewHolder>() {
    private var mDatas: ArrayList<T> = arrayListOf()
    private var mItemViewDelegateManager: ItemViewDelegateManager<T> = ItemViewDelegateManager()

    fun setData(datas: ArrayList<T>) {
        mDatas = datas
        notifyDataSetChanged()
    }

    fun update(datas: ArrayList<T>) {
        mDatas = datas
        notifyDataSetChanged()
    }

    fun addFirst(data: T) {
        mDatas.add(data)
        notifyDataSetChanged()
    }

    fun addLast(data: T): Int {
        return with(data) {
            mDatas.add(data)
            notifyItemInserted(mDatas.size)
            itemCount - 1
        }
    }

    fun updateItem(position: Int, data: T) {
        notifyItemChanged(position, data)
    }

    override fun getItemViewType(position: Int): Int {
        return if (!useItemViewDelegateManager()) {
            super.getItemViewType(position)
        } else {
            mItemViewDelegateManager.getItemViewType(mDatas[position], position)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemViewDelegate = mItemViewDelegateManager.getItemViewDelegate(viewType)
        val layoutId = itemViewDelegate.getItemViewLayoutId()
        return ViewHolder.createViewHolder(mContext, parent, layoutId)
    }

    private fun convert(holder: ViewHolder, t: T) {
        mItemViewDelegateManager.convert(holder, t, holder.adapterPosition)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        convert(holder, mDatas[position])
    }

    override fun getItemCount(): Int {
        return mDatas.size
    }

    open fun addItemViewDelegate(itemViewDelegate: ItemViewDelegate<T>) {
        mItemViewDelegateManager.addDelegate(itemViewDelegate)
    }

    fun addItemViewDelegate(viewType: Int, itemViewDelegate: ItemViewDelegate<T>) {
        mItemViewDelegateManager.addDelegate(viewType, itemViewDelegate)
    }

    private fun useItemViewDelegateManager(): Boolean {
        return mItemViewDelegateManager.getItemViewDelegateCount() > 0
    }

}