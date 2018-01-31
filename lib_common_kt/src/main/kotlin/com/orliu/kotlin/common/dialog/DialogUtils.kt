package com.orliu.kotlin.common.dialog

import android.support.v4.app.FragmentManager
import com.orliu.kotlin.common.base.BaseDialog

/**
 * Created by liujianping on 18/01/2018.
 */
object DialogUtils {
    private val dialogs: ArrayList<BaseDialog> = arrayListOf()

    @JvmStatic
    fun showLoading(manager: FragmentManager, loadingText: String) {
        val dialog: BaseDialog = WindowLoadingDialog.instance(loadingText) as BaseDialog
        showAllowingStateLoss(dialog, manager, "windowloadingdialog")
        dialogs.add(dialog)
    }

    private fun showAllowingStateLoss(fragment: BaseDialog, manager: FragmentManager, tag: String) {
        if (!fragment.isAdded) {
            val ft = manager.beginTransaction()
            ft.add(fragment, tag)
            ft.commitAllowingStateLoss()
        }
    }

    @JvmStatic
    fun dismissLoading() {
        dialogs.forEach {
            it?.apply {
                if (isAdded) dismissAllowingStateLoss()
            }
        }
        dialogs.clear()
    }
}