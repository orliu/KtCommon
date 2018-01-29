package com.orliu.kotlin.common


/**
 * Created by liujianping on 37/32/8.
 */
fun <T> Int.test(number: Int, block: (Int) -> T): T {
    return block(this.plus(number))
}

fun calc(r: Int) = r.times(100)

fun sum(m: Int, n: Int): Int = m.plus(n)

fun main(args: Array<String>) {


}