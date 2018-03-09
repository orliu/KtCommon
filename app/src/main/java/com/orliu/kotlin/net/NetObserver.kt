package com.orliu.kotlin.net

import com.orliu.kotlin.common.base.BaseResult
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.HttpException
import java.net.SocketTimeoutException

/**
 * Created by liujianping on 09/03/2018.
 */
abstract class NetObserver<T> : Observer<T> {
    override fun onComplete() {

    }

    override fun onSubscribe(d: Disposable?) {
    }

    override fun onNext(value: T) {
        if (value is String) {
            onSuccess(value)
        } else if (value is BaseResult<*>) {
            when (value.errcode) {

                BaseResult.SERVER_SUCCESS -> onSuccess(value)
                BaseResult.SERVER_TOKEN_MISSING,
                BaseResult.SERVER_TOKEN_EXPIRED -> {
                    // logout and target to login activity
                }
                else -> onError(BaseResult.unkownServerError(value.errcode))
            }
        }
    }

    abstract fun onSuccess(t : T)

    override fun onError(e: Throwable?) {
        e?.let {
            when (it) {
                is SocketTimeoutException -> onError(BaseResult.timeout())
                is HttpException -> onError(BaseResult.httpException(it.message))
                is Exception -> onError(BaseResult.exception(it.message))
                else -> onError(BaseResult.unkownError())
            }
        }
    }

    abstract fun onError(error: BaseResult<*>)
}