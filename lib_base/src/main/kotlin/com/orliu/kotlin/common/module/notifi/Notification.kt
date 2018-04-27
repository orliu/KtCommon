package com.orliu.kotlin.common.module.notifi

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.widget.RemoteViews
import com.orliu.kotlin.common.R


/**
 * Created by orliu on 05/04/2018.
 */


fun Context.buildNotifi(ticker: String, title: String, body: String) {
    val remoteViews = RemoteViews(packageName, R.layout.layout_notifi_remoteview)
    remoteViews.setImageViewResource(R.id.id_noti_icon, R.drawable.ic_launcher)
    remoteViews.setTextViewText(R.id.id_noti_title, title)
    remoteViews.setTextViewText(R.id.id_noti_body, body)


    remoteViews.setTextViewText(R.id.id_noti_body, body)


    val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val builder = NotificationCompat.Builder(this, packageName)
    builder.setWhen(System.currentTimeMillis())
            .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
            .setAutoCancel(true)
            .setOngoing(false)
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setSmallIcon(R.drawable.ic_launcher)
            .setTicker(ticker)
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(getDefaultIntent())
            .setGroup(packageName)
            .setGroupSummary(true)

    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
        builder.setCustomBigContentView(remoteViews)
    } else {
        builder.setContent(remoteViews)
    }
    manager.notify((System.currentTimeMillis() % 100000L).toInt(), builder.build())
}

@SuppressLint("WrongConstant")
fun Context.getDefaultIntent(): PendingIntent {
    return PendingIntent.getActivity(this, 0xAB, Intent(), Notification.FLAG_AUTO_CANCEL)
}
