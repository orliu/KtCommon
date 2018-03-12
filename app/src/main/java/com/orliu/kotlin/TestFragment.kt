package com.orliu.kotlin

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orliu.kotlin.common.extension.android.startActivity
import com.tunaikita.log.LogSDK
import kotlinx.android.synthetic.main.fragment_test.*
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * Created by orliu on 31/01/2018.
 */
class TestFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_test, container, false)
                ?: LayoutInflater.from(context).inflate(R.layout.fragment_test, container, false)
    }

    override fun onResume() {
        super.onResume()

        id_button.onClick {
            //startActivity<SecActivity>(Intent.FLAG_ACTIVITY_NEW_TASK)
            LogSDK.close()
        }
    }
}