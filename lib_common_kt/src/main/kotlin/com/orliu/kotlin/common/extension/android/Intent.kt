package com.orliu.kotlin.common.extension.android

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.text.TextUtils

// for java caller
inline fun Context.startActivity(clazz: Class<*>) {
    val intent = Intent()
    intent.setClass(this, clazz)
    startActivity(intent)
}

inline fun FragmentActivity.startActivity(clazz: Class<*>) {
    val intent = Intent()
    intent.setClass(this, clazz)
    startActivity(intent)
}

inline fun Context.startActivity(clazz: Class<*>, bundle: Bundle) {
    val intent = Intent()
    intent.setClass(this, clazz)
    intent.putExtras(bundle)
    startActivity(intent)
}

inline fun FragmentActivity.startActivity(clazz: Class<*>, bundle: Bundle) {
    val intent = Intent()
    intent.setClass(this, clazz)
    intent.putExtras(bundle)
    startActivity(intent)
}


// for kotlin caller
/**
 * for component
 */
inline fun Context.startActivity(component: String) {
    val intent = Intent()
    intent.setClassName(this, component)
    startActivity(intent)
}

inline fun Fragment.startActivity(component: String) = activity.startActivity(component)

/**
 * for component with flag
 */
inline fun Context.startActivity(component: String, flag: Int) {
    val intent = Intent()
    intent.setClassName(this, component)
    intent.flags = flag
    startActivity(intent)
}

inline fun Fragment.startActivity(component: String, flag: Int)  = activity.startActivity(component, flag)

/**
 * for A:FragmentActivity with flag
 */
inline fun <reified A : FragmentActivity> Context.startActivity(flag: Int) {
    val intent = Intent()
    intent.setClass(this, A::class.java)
    intent.flags = flag
    startActivity(intent)
}

inline fun <reified A : FragmentActivity> Fragment.startActivity(flag: Int) = activity.startActivity<A>(flag)

/**
 * for A:FragmentActivity with bundle & flag
 */
inline fun <reified A : FragmentActivity> Context.startActivity(bundle: Bundle, flag: Int) {
    val intent = Intent()
    intent.setClass(this, A::class.java)
    intent.putExtras(bundle)
    intent.flags = flag
    startActivity(intent)
}

inline fun <reified A : FragmentActivity> Fragment.startActivity(bundle: Bundle, flag: Int)  = activity.startActivity<A>(bundle, flag)

/**
 * for startActivityForResult with component
 */
inline fun FragmentActivity.startActivityForResult(component: String, requestCode: Int, bundle: Bundle?) {
    val intent = Intent()
    intent.setClassName(this, component)
    bundle?.let { intent.putExtras(it) }
    startActivityForResult(intent, requestCode)
}

/**
 * for url
 *
 * @param url
 */
inline fun Context.targetUrl(url: String) {
    var url = url
    if (TextUtils.isEmpty(url))
        throw IllegalArgumentException("url is null")

    val play = Intent()
    play.action = Intent.ACTION_VIEW
    play.data = Uri.parse(url)
    play.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(play)
}

inline fun Fragment.targetUrl(url: String) = activity.targetUrl(url)

/**
 * 通讯录联系人选择. needs read contact permission
 *
 */
fun Activity.openContactAction(requestCode: Int) {
    try {
        startActivityForResult(Intent(
                Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), requestCode)
    } catch (e: Exception) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = ContactsContract.Contacts.CONTENT_ITEM_TYPE
        startActivityForResult(intent, requestCode)
    }
}

/**
 * for call view
 */
fun Context.makeCall(phone: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + phone))
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
}

fun Fragment.makeCall(phone: String) = activity.makeCall(phone)

/**
 * 跳转到Google play
 *
 * @param activity
 * @param url
 */
fun Context.targetGooglePlay(url: String) {
    var url = url
    if (TextUtils.isEmpty(url))
        throw IllegalArgumentException("url is null")

    if (!url.startsWith("https://"))
        url = "https://" + url

    val play = Intent()
    play.action = Intent.ACTION_VIEW
    play.data = Uri.parse(url)
    play.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(play)
}

// 打开新邮件页面
fun Activity.openEmailPage(email: String, title: String, content: String) {
    try {
        val uri = Uri.parse("mailto:" + email)
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        //intent.putExtra(Intent.EXTRA_CC, email); // 抄送人
        intent.putExtra(Intent.EXTRA_SUBJECT, title) // 主题
        intent.putExtra(Intent.EXTRA_TEXT, content) // 正文
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        this.startActivity(Intent.createChooser(intent, "Please Choose Email"))
    } catch (e: Exception) {
        e.printStackTrace()
    }
}