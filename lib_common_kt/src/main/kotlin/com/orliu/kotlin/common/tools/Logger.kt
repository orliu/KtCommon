package com.orliu.kotlin.common.tools


/**
 * Logger
 * Created by liujianping on 17/11/10.
 */
class Logger {

    companion object {
        @JvmField
        var debug: Boolean = false

        private fun defaultTag(): String {
            val stackTraceElement = Thread.currentThread().stackTrace[4]
            val className = stackTraceElement.className
            val line = stackTraceElement.lineNumber
            return "$className [$line]"
        }

        @JvmStatic
        fun d(o: Any?) {
            if (debug)
                android.util.Log.d(defaultTag(), o?.toString() ?: "object is null")
        }

        @JvmStatic
        fun e(o: Any?) {
            if (debug)
                android.util.Log.e(defaultTag(), o?.toString() ?: "object is null")
        }

        @JvmStatic
        fun v(o: Any?) {
            if (debug)
                android.util.Log.v(defaultTag(), o?.toString() ?: "object is null")
        }

        @JvmStatic
        fun i(o: Any?) {
            if (debug)
                android.util.Log.i(defaultTag(), o?.toString() ?: "object is null")
        }

        @JvmStatic
        fun w(o: Any?) {
            if (debug)
                android.util.Log.w(defaultTag(), o?.toString() ?: "object is null")
        }
    }
}