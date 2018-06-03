package com.orliu.kotlin.common.dialog

import com.orliu.kotlin.common.R
import com.tunaikita.log.base.BaseDialog
import kotlinx.android.synthetic.main.dialog_window_loading.*

/**
 * window loading dialog
 * Created by orliu on 18/01/2018.
 */
class WindowLoadingDialog : BaseDialog() {
    private var loadingText: String = "Loading..."

    companion object {
        @JvmStatic
        fun instance(): WindowLoadingDialog {
            return WindowLoadingDialog()
        }
    }

    fun setLoadingText(loadingText: String){
        this.loadingText = loadingText
    }

    override fun getLayoutId() = R.layout.dialog_window_loading

    override fun initView() {
        if (loadingText.isNotEmpty()) id_dialog_loading.text = loadingText
    }

    override fun onBackPressed() = false


}