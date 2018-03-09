package com.orliu.retrofit.transformer;


import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/*
 * @description
 * @author Liujianping (liujianping@wecash.net)
 * @version 1.0.0(2017/2/17)
 * 17/2/28 上午11:43
 *
 */
public class SchedulersTransformer {

    //    public static <T> FlowableTransformer<T, T> io() {
    //        return upstream ->
    //                upstream.subscribeOn(Schedulers.io())
    //                        .observeOn(AndroidSchedulers.mainThread());
    //    }


    public static <T> ObservableTransformer<T, T> io() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
