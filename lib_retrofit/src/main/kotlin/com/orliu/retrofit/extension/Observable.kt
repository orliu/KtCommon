package com.orliu.retrofit.extension

import com.orliu.retrofit.transformer.SchedulersTransformer
import io.reactivex.Observable
import io.reactivex.Observer

/**
 * Created by liujianping on 09/03/2018.
 */

/**
 * 请求service
 *
 * @param observer
 * @param <T>
 * @return
</T> */
fun <T> Observable<T>.request(observer: Observer<T>) {
    compose(SchedulersTransformer.io()).subscribe(observer)
}