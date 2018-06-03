package com.orliu.kotlin.common.base

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.*
import com.orliu.kotlin.common.R

/**
 * Created by orliu on 16/04/2018.
 */
abstract class BaseBottomDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(activity, R.style.StyleDialogBottom)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(getLayoutId())
        dialog.setCanceledOnTouchOutside(getCancelable())
        dialog.setCancelable(getCancelable())
        dialog.setOnKeyListener(DialogInterface.OnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dismiss()
                return@OnKeyListener true
            }
            false
        })

        val window = dialog.window
        val params = window.attributes
        params.gravity = Gravity.BOTTOM
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = params
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater?.inflate(getLayoutId(), container ?: null, false)
        initView(contentView)
        return contentView
    }

    abstract fun getLayoutId(): Int

    abstract fun getCancelable(): Boolean

    abstract fun initView(view: View?)

    override fun show(fm: FragmentManager, tag: String) {
        if (isAdded) return
        if (fm.isDestroyed) return
        fm.beginTransaction().add(this, tag).commitAllowingStateLoss()
    }

    override fun dismiss() {
        if (!isAdded) return
        dismissAllowingStateLoss()
    }
}