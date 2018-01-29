package com.orliu.kotlin.common.view.rv

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import java.util.*

/**
 * Created by Orliu on 2016/10/13.
 */

open class RecyclerAdapter<T>(private var mContext: Context) : RecyclerView.Adapter<ViewHolder>() {
    private var mDatas: ArrayList<T> = arrayListOf()
    private var mItemViewDelegateManager: ItemViewDelegateManager<T> = ItemViewDelegateManager()

    fun setData(datas: ArrayList<T>?) {
        if (datas != null) {
            mDatas = datas
            notifyDataSetChanged()
        }
        datas?.let {
            mDatas = datas
            notifyDataSetChanged()
        }
    }

    fun update(datas: ArrayList<T>?) {
        datas?.let {
            mDatas = it
            notifyDataSetChanged()
        }
    }

    fun addFirst(data: T?) {
        data?.let {
            mDatas.add(data)
            notifyDataSetChanged()
        }
    }

    fun addLast(data: T?): Int {
        return data?.let {
            mDatas.add(data)
            notifyItemInserted(mDatas.size)
            itemCount - 1
        } ?: let { 0 }
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

    fun convert(holder: ViewHolder, t: T) {
        mItemViewDelegateManager.convert(holder, t, holder.adapterPosition)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        convert(holder, mDatas[position])
    }

    override fun getItemCount(): Int {
        return mDatas.size
    }

    fun addItemViewDelegate(itemViewDelegate: ItemViewDelegate<T>): RecyclerAdapter<*> {
        mItemViewDelegateManager.addDelegate(itemViewDelegate)
        return this
    }

    fun addItemViewDelegate(viewType: Int, itemViewDelegate: ItemViewDelegate<T>): RecyclerAdapter<*> {
        mItemViewDelegateManager.addDelegate(viewType, itemViewDelegate)
        return this
    }

    private fun useItemViewDelegateManager(): Boolean {
        return mItemViewDelegateManager.getItemViewDelegateCount() > 0
    }

}