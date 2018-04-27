package com.orliu.retrofit.observer

import com.orliu.kotlin.common.base.BaseResult
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.HttpException
import java.net.SocketTimeoutException

/**
 * Observer
 * Created by liujianping on 19/01/2018.
 */
abstract class NetObserver<T> : Observer<T> {

    override fun onSubscribe(d: Disposable?) {
    }

    override fun onComplete() {
    }

    override fun onError(throwable: Throwable?) {
        throwable?.let {
            when (it) {
                is SocketTimeoutException -> onError(BaseResult.timeout())
                is HttpException -> onError(BaseResult.httpException(it.message))
                is Exception -> onError(BaseResult.exception(it.message))
                else -> onError(BaseResult.unkownError())
            }
        }
    }

    override fun onNext(value: T) {
        value?.let {

            if (it is String) {
                onSuccess(value)
            } else if (it is BaseResult<*>) {
                when (it.errcode) {

                    BaseResult.SERVER_SUCCESS -> onSuccess(value)
                    BaseResult.SERVER_TOKEN_MISSING,
                    BaseResult.SERVER_TOKEN_EXPIRED -> {
                        // logout and target to login activity
                    }
                    else -> onError(BaseResult.unkownServerError(it.errcode, it.msg))
                }
            }
        }
    }

    abstract fun onSuccess(it: T)
    abstract fun onError(error: BaseResult<*>)
}