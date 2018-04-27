package com.orliu.kotlin.common.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.*
import com.orliu.kotlin.common.R
import com.orliu.kotlin.common.tools.Logger
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.find

/**
 * 底部自定义布局弹窗
 * Created by orliu on 2018/2/24.
 */
class BottomDialog : DialogFragment() {

    private var mLayoutId = -1
    private var mWidgets: IntArray? = null
    private var mDialogClickListenerAdapter: DialogClickListenerAdapter? = null


    companion object {
        @JvmStatic
        fun instance() = Holder.instance
    }

    private object Holder {
        val instance = BottomDialog()
    }

    fun setLayoutId(layoutId: Int): BottomDialog {
        this.mLayoutId = layoutId
        return this
    }

    fun setWidgets(widgets: IntArray): BottomDialog {
        this.mWidgets = widgets
        return this
    }

    fun setOnDialogClickListener(listenerAdapter: DialogClickListenerAdapter): BottomDialog {
        this.mDialogClickListenerAdapter = listenerAdapter
        return this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.StyleDialogBottom)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        if (mLayoutId == -1) {
            Logger.e("LayoutId is null. Please call the setLayoutId() method.")
            return super.onCreateDialog(savedInstanceState)
        } else {
            val dialog = Dialog(activity, R.style.StyleDialogBottom)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)    // 设置Content前设定
            dialog.setContentView(mLayoutId)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setCancelable(true)
            dialog.setOnKeyListener { _, keyCode, _ ->
                when (keyCode) {
                    KeyEvent.KEYCODE_BACK -> {
                        dismiss()
                        true
                    }
                    else -> false
                }
            }

            val window = dialog.window
            val params = window.attributes
            params.gravity = Gravity.BOTTOM
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            window.attributes = params
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            return dialog
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(mLayoutId, container, false)
        mWidgets?.let {
            it.forEachIndexed { _, widgetId ->
                //find<View>(widgetId)?
                view?.findViewById<View>(widgetId)?.onClick {
                    dismissAllowingStateLoss()
                    mDialogClickListenerAdapter?.let {
                        it.onBottomViewClick(widgetId)
                    }
                }
            }
        }
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissAllowingStateLoss()
    }
}