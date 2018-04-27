package com.orliu.amap;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.amap.api.services.help.Tip;
import com.orliu.amap.adapter.ItemViewDelegate;
import com.orliu.amap.adapter.RecyclerAdapter;
import com.orliu.amap.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by orliu on 21/04/2018.
 */

public class LocationInputDropDownPupup extends CardView {

    private PopupWindow mPopupWindow;
    private RecyclerView mRv;
    private RecyclerAdapter<Tip> mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<Tip> mTips;
    private IAdapterListener<Tip> mItemClickListener;

    public void setOnItemClickListener(IAdapterListener<Tip> itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public LocationInputDropDownPupup(Context context) {
        this(context, null);
    }

    public LocationInputDropDownPupup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LocationInputDropDownPupup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initViews();
    }

    private void initViews() {
        if (getChildCount() > 0) removeAllViews();

        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_popup_location_input, this, false);
        addView(view);

        mRv = view.findViewById(R.id.id_drop_down_rv);
        if (mLayoutManager == null)
            mLayoutManager = new LinearLayoutManager(getContext());
        mRv.setLayoutManager(mLayoutManager);
        if (mAdapter == null)
            mAdapter = new RecyclerAdapter<>(getContext());
        if (mTips == null)
            mTips = new ArrayList<>();
        mAdapter.setData(mTips);
        mAdapter.addItemViewDelegate(new DropDownItemDelegate());
        mRv.setAdapter(mAdapter);
    }

    public void setTips(List<Tip> tips) {
        this.mTips = tips;
        mAdapter.setData(mTips);
    }

    private class DropDownItemDelegate implements ItemViewDelegate<Tip> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_drop_down_tip;
        }

        @Override
        public boolean isForViewType(Tip item, int position) {
            return true;
        }

        @Override
        public void convert(ViewHolder holder, Tip tip, int position) {
            holder.withView(R.id.id_tip_name).setText(tip.getName());
            holder.itemView.setOnClickListener(v -> {
                if (mItemClickListener != null)
                    mItemClickListener.onClick(tip, position);
            });
        }
    }

    public void show(View anchor) {
        if (mPopupWindow == null) {
            int popupWidth = anchor.getWidth() > 0 ? anchor.getWidth() + dip2px(getContext(), 6) : LayoutParams.WRAP_CONTENT;
            mPopupWindow = new PopupWindow(this, popupWidth, LayoutParams.WRAP_CONTENT);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable());
            mPopupWindow.setFocusable(false);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        int offset = -dip2px(getContext(), 3);
        mPopupWindow.showAsDropDown(anchor, offset, 0);
    }

    public void dismissPopup() {
        if (mPopupWindow != null) {
            if (mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }
        }
    }

    /**
     * dip to px
     *
     * @param context
     * @param dpValue
     * @return
     */
    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
