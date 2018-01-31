package com.orliu.kotlin.common.view.rv

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by Orliu on 2016/10/13.
 */

class ViewHolder(private var mConvertView: View) : RecyclerView.ViewHolder(mConvertView) {
    private lateinit var mTargetView: View
    private val mViews: SparseArray<View> = SparseArray()


    companion object {
        @JvmStatic
        fun createViewHolder(context: Context, parent: ViewGroup, layoutId: Int): ViewHolder {
            val itemView = LayoutInflater.from(context).inflate(layoutId, parent, false)
            return ViewHolder(itemView)
        }
    }

    private fun <T : View> getView(viewId: Int): T {
        var view: View? = mViews[viewId]
        if (view == null) {
            view = mConvertView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view as T
    }

    fun withView(viewId: Int): ViewHolder {
        mTargetView = getView(viewId)
        return this
    }


    fun setText(text: String): ViewHolder {
        (mTargetView as TextView).text = text
        return this
    }

    fun setTextColor(colorRes: Int): ViewHolder {
        (mTargetView as TextView).setTextColor(colorRes)
        return this
    }

    fun setVisible(visible: Int): ViewHolder {
        mTargetView!!.visibility = visible
        return this
    }

    fun setImageResource(resId: Int): ViewHolder {
        (mTargetView as ImageView).setImageResource(resId)
        return this
    }

    fun setOnClickListener(listener: View.OnClickListener): ViewHolder {
        mTargetView!!.setOnClickListener(listener)
        return this
    }

    fun onItemClickListener(listener: View.OnClickListener): ViewHolder {
        mConvertView.setOnClickListener(listener)
        return this
    }

}