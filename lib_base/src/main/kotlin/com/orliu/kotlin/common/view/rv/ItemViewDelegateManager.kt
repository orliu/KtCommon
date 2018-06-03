package com.orliu.kotlin.common.view.rv

import android.support.v4.util.SparseArrayCompat

/**
 * Created by orliu on 29/01/2018.
 */
class ItemViewDelegateManager<T> {
    private val delegates = SparseArrayCompat<ItemViewDelegate<T>>()

    fun getItemViewDelegateCount() = delegates.size()

    fun addDelegate(delegate: ItemViewDelegate<T>): ItemViewDelegateManager<T> {
        var viewType = delegates.size()
        if (delegate != null) {
            delegates.put(viewType, delegate)
            viewType++
        }
        return this
    }

    fun addDelegate(viewType: Int, delegate: ItemViewDelegate<T>): ItemViewDelegateManager<T> {
        delegates.get(viewType) ?: let {
            throw  IllegalArgumentException(
                    "An ItemViewDelegate is already registered for the viewType = "
                            + viewType
                            + ". Already registered ItemViewDelegate is "
                            + delegates.get(viewType))
        }
        delegates.put(viewType, delegate)
        return this
    }

    fun removeDelegate(delegate: ItemViewDelegate<T>): ItemViewDelegateManager<T> {
        val indexToRemove = delegates.indexOfValue(delegate)
        if (indexToRemove >= 0) delegates.removeAt(indexToRemove)
        return this
    }

    fun removeDelegate(itemType: Int): ItemViewDelegateManager<T> {
        val indexToRemove = delegates.indexOfKey(itemType)
        if (indexToRemove >= 0) delegates.removeAt(indexToRemove)
        return this
    }

    fun getItemViewType(item: T, position: Int): Int {
        val delegatesCount = delegates.size()

        (0 until delegatesCount).forEach {
            val delegate = delegates.valueAt(it)
            if (delegate.isForViewType(item, position))
                return delegates.keyAt(it)
        }
        throw IllegalArgumentException("No ItemViewDelegate added that matches position=$position in data source")
        return -1
    }

    fun convert(holder: ViewHolder, item: T, position: Int) {
        val delegatesCount = delegates.size()
        (0 until delegatesCount).forEach {
            val delegate = delegates.valueAt(it)
            if (delegate.isForViewType(item, position)) {
                delegate.convert(holder, item, position)
                return
            }
        }
        throw  IllegalArgumentException("No ItemViewDelegateManager added that matches position=$position in data source")
    }


    fun getItemViewDelegate(viewType: Int): ItemViewDelegate<T> = delegates.get(viewType)

    fun getItemViewLayoutId(viewType: Int) = getItemViewDelegate(viewType).getItemViewLayoutId()

    fun getItemViewType(itemViewDelegate: ItemViewDelegate<T>) = delegates.indexOfValue(itemViewDelegate)
}