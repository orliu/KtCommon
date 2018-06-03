package com.orliu.kotlin

import com.google.gson.Gson
import com.orliu.kotlin.common.extension.other.parseJson
import org.junit.Test

/**
 * Created by orliu on 2018/5/29.
 */
class Test {

    @Test
    fun testB() {
        val bean = TestBean("aaaa")
        System.out.println(bean)

        val json = "{\"name\":\"bbbbbbb\"}"
        val result = Gson().parseJson<TestBean>(json)

        System.out.println(result)

    }
}