package com.orliu.kotlin.common.dialog

import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager

/**
 * Created by orliu on 18/01/2018.
 */
object DialogUtils {
    private val dialogs: ArrayList<DialogFragment> = arrayListOf()

    @JvmStatic
    fun showLoading(manager: FragmentManager, loadingText: String) {
        val dialog = WindowLoadingDialog.instance(loadingText)
        showAllowingStateLoss(dialog, manager, "windowloadingdialog")
        dialogs.add(dialog)
    }

    @JvmStatic
    fun showBottom(manager: FragmentManager, layoutId: Int, widgets: IntArray, dialogClickListenerAdapter: DialogClickListenerAdapter) {
        val dialog = BottomDialog.instance()
                .setLayoutId(layoutId)
                .setWidgets(widgets)
                .setOnDialogClickListener(dialogClickListenerAdapter)
        showAllowingStateLoss(dialog, manager, "bottomdialog")
        dialogs.add(dialog)
    }

    private fun showAllowingStateLoss(fragment: DialogFragment, manager: FragmentManager, tag: String) {
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