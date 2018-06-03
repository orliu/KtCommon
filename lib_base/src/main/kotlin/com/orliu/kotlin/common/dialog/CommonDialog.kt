package com.orliu.kotlin.common.dialog

import android.view.View
import com.orliu.kotlin.common.R
import com.tunaikita.log.base.BaseDialog
import kotlinx.android.synthetic.main.dialog_common.*
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * Created by orliu on 2018/5/17.
 */
class CommonDialog : BaseDialog() {

    private var mContent: String = ""
    private var mCancel: String = ""
    private var mConfirm: String = ""
    private var mOnClickListener: OnClickListenerAdapter? = null

    companion object {
        @JvmStatic
        fun newInstance() = CommonDialog()
    }

    fun setArguments(content: String, cancel: String = "", confirm: String = ""): CommonDialog {
        mContent = content
        mCancel = cancel
        mConfirm = confirm
        return this
    }

    fun setOnClickListener(onClickListener: OnClickListenerAdapter): CommonDialog {
        mOnClickListener = onClickListener
        return this
    }

    override fun getLayoutId() = R.layout.dialog_common

    override fun initView() {
        id_common_dialog_content.text = mContent

        if (mCancel.isNullOrEmpty()) {
            id_common_dialog_cancel.visibility = View.GONE
            id_common_button_divider.visibility = View.GONE
        } else {
            id_common_dialog_cancel.text = mCancel
            id_common_dialog_cancel.onClick {
                dismiss()
                mOnClickListener?.let { it.onCancel() }
            }

        }

        if (mConfirm.isNotEmpty()) {
            id_common_dialog_confirm.text = mConfirm
        }
        id_common_dialog_confirm.onClick {
            dismiss()
            mOnClickListener?.let { it.onConfirm() }
        }
    }

    override fun onBackPressed() = false

     abstract class OnClickListenerAdapter {
        open fun onCancel(){}
        open fun onConfirm(){}
    }

}