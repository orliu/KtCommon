package com.orliu.amap.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Orliu on 2016/10/13.
 */

public class ViewHolder extends RecyclerView.ViewHolder {

    private View mConvertView;
    private View mTargetView;
    private SparseArray<View> mViews;

    public ViewHolder(View itemView) {
        super(itemView);

        mConvertView = itemView;
        mViews = new SparseArray<>();
    }

    public static ViewHolder createViewHolder(Context context, ViewGroup parent, int layoutId) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new ViewHolder(itemView);
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public ViewHolder withView(int viewId) {
        mTargetView = getView(viewId);
        return this;
    }


    public ViewHolder setText(String text) {
        ((TextView) mTargetView).setText(text);
        return this;
    }

    public ViewHolder setVisible(int visible) {
        mTargetView.setVisibility(visible);
        return this;
    }

    public ViewHolder setImageResource(int resId) {
        ((ImageView) mTargetView).setImageResource(resId);
        return this;
    }

    public ViewHolder setBackgroundResource(int resId){
        ((ImageView) mTargetView).setBackgroundResource(resId);
        return this;
    }

    public ViewHolder setChecked(boolean checked) {
        ((CheckBox) mTargetView).setChecked(checked);
        return this;
    }

    public ViewHolder setOnCheckChangedListener(CompoundButton.OnCheckedChangeListener listener){
        ((CheckBox) mTargetView).setOnCheckedChangeListener(listener);
        return this;
    }

    public ViewHolder setOnClickListener(View.OnClickListener listener) {
        mTargetView.setOnClickListener(listener);
        return this;
    }

    public ViewHolder onItemClickListener(View.OnClickListener listener) {
        mConvertView.setOnClickListener(listener);
        return this;
    }
}
