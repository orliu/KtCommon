package com.orliu.kotlin.common.extension.android

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.text.TextUtils

/**
 * for component
 */
inline fun Context.targetActivity(component: String) {
    val intent = Intent()
    intent.setClassName(this, component)
    startActivity(intent)
}

inline fun Fragment.targetActivity(component: String) = activity.targetActivity(component)

/**
 * for component with flag
 */
inline fun Context.targetActivity(component: String, flag: Int) {
    val intent = Intent()
    intent.setClassName(this, component)
    intent.flags = flag
    startActivity(intent)
}

inline fun Fragment.targetActivity(component: String, flag: Int) = activity.targetActivity(component, flag)

/**
 * for T:Activity with flag
 */
inline fun <reified T : Activity> Context.targetActivity(flag: Int) {
    val intent = Intent()
    intent.setClass(this, T::class.java)
    intent.flags = flag
    startActivity(intent)
}

inline fun <reified T : Activity> Fragment.targetActivity(flag: Int) = activity.targetActivity<T>(flag)

/**
 * for T:Activity with bundle & flag
 */
inline fun <reified T : Activity> Context.targetActivity(bundle: Bundle, flag: Int) {
    val intent = Intent()
    intent.setClass(this, T::class.java)
    intent.putExtras(bundle)
    intent.flags = flag
    this.startActivity(intent)
}

inline fun <reified T : Activity> Fragment.tartActivity(bundle: Bundle, flag: Int) = activity.targetActivity<T>(bundle, flag)

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