package com.orliu.kotlin.common.tools

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.support.v4.content.ContextCompat
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.view.inputmethod.InputMethodManager
import java.lang.Exception

/**
 * 设备相关常用函数
 * Created by liujianping on 17/10/24.
 */
//屏幕宽px
fun Context.screenWidthPixels(): Int = this.resources.displayMetrics.widthPixels

//屏幕高px
fun Context.screenHeightPixels(): Int = this.resources.displayMetrics.heightPixels

//计算相应的px
fun Int.toPixel(context: Context): Int = (this * context.resources.displayMetrics.density + 0.5f).toInt()

//计算相应的dp
fun Int.toDip(context: Context): Int = (this / context.resources.displayMetrics.density + 0.5f).toInt()

//状态栏高度
fun Context.statusbarHeight(): Int {
    var statusbarHeight = 0
    val resourceId = this.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0)
        statusbarHeight = this.resources.getDimensionPixelSize(resourceId)

    return statusbarHeight
}

//需要申请权限
fun needPermission(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

//检查是否有某个权限
fun Context.hasPermission(permission: String): Boolean =
        PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, permission)

//apk签名信息
fun Context.signature(packageName: String): String {
    var signature: String
    try {
        val packageInfo = this.packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        val signatures = packageInfo.signatures
        val sb = StringBuffer()
        for (sign in signatures) {
            sb.append(sign.toCharsString())
        }
        signature = sb.toString()
    } catch (e: Exception) {
        signature = ""
        e.printStackTrace()
    }

    return signature
}

//拷贝到剪贴板
fun String.copyToClipboard(context: Context) {
    val data = ClipData.newPlainText("clip_text", this)
    val manager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    manager.primaryClip = data
}

//获取剪贴板内容
fun Context.clipboardText(): String {
    val manager = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val data = manager.primaryClip
    return if (data != null) {
        val item = manager.primaryClip.getItemAt(0)
        item?.text?.toString() ?: ""
    } else {
        ""
    }
}

//关闭软件盘
fun Activity.hiddenSoftKeybord() {
    val view = this.window.peekDecorView()
    if (view != null) {
        val inputmanger = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputmanger.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

/**
 * 获得所有运行中进程
 * *
 * @return
 */
fun Context.processList(): List<RunningAppProcessInfo> {
    val am = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    return am.runningAppProcesses
}

/**
 * 判断是否为当前进程 （当manifest中有:process配置时，多进程会导致application多次执行）
 * String packageName = MobileUtils.isMyProcess(mContext, android.os.Process.myPid());
 * if (packageName.equals(REAL_PACKAGENAME)) {
 * }
 * @param myPid
 * *
 * @return 返回myPid对应包名
 */
fun Context.isMyProcess(myPid: Int): String {
    val runningProcesses = processList()
    return runningProcesses
            .firstOrNull { it.pid == myPid }
            ?.processName
            ?: ""
}

/**
 * version code
 */
fun Context.versionCode(): Int {
    var versionCode = 0
    try {
        val pm = this.packageManager
        val info = pm.getPackageInfo(this.packageName, 0)
        versionCode = info.versionCode
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }

    return versionCode
}

/**
 * version code
 */
fun Context.versionCode(packageName: String): Int {
    var versionCode = 0
    try {
        val info = this.packageManager.getPackageInfo(packageName, 0)
        versionCode = info.versionCode
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }

    return versionCode
}

/**
 * version name
 */
fun Context.versionName(): String {
    var versionName = ""
    try {
        val pm = this.packageManager
        val info = pm.getPackageInfo(this.packageName, 0)
        versionName = info.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }

    return versionName
}


/**
 * version name
 */
fun Context.versionName(packageName: String): String {
    var versionName = ""
    try {
        val info = this.packageManager.getPackageInfo(packageName, 0)
        versionName = info.versionName
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return versionName
}

/**
 * Android Id
 */
fun Context.androidId(): String {
    var androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
    if (TextUtils.isEmpty(androidId)) {
        androidId = "" // 16位
    }
    return androidId
}

/**
 * IMEI
 */
@SuppressLint("MissingPermission")
fun Context.imei(): String {
    val tm = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    //351928085980420
    var imei = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
        tm.deviceId
    } else {
        tm.imei
    }

    return imei ?: androidId()
}

/**
 * mac
 */
@SuppressLint("MissingPermission")
fun Context.macAddress(): String {
    val wifiManager = this.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val wifiInfo = wifiManager.connectionInfo
    return wifiInfo.macAddress ?: ""
}
