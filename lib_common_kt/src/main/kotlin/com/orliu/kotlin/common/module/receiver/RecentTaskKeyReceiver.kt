package com.orliu.kotlin.common.module.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * 监听home键、recent task键
 * IntentFilter Action: Intent.ACTION_CLOSE_SYSTEM_DIALOGS
 * Created by liujianping on 07/02/2018.
 */
class RecentTaskKeyReceiver : BroadcastReceiver() {

    private val SYSTEM_DIALOG_REASON_KEY = "reason"
    private val SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions"
    private val SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps"
    private val SYSTEM_DIALOG_REASON_HOME_KEY = "homekey"

    override fun onReceive(context: Context?, intent: Intent?) {
        intent ?: return

        val action = intent.action
        if (action == Intent.ACTION_CLOSE_SYSTEM_DIALOGS) {
            val reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY)
            if (reason != null) {
                if (reason == SYSTEM_DIALOG_REASON_HOME_KEY) {
                    // home

                } else if (reason == SYSTEM_DIALOG_REASON_RECENT_APPS) {
                    // recent task

                }
            }
        }
    }

}