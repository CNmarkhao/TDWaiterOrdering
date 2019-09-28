package com.tdlbs.core.module.rxjava;

import com.tdlbs.core.module.network.retrofit.exeception.ApiException;
import com.tdlbs.core.module.network.retrofit.exeception.ExeceptionEngine;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


/**
 * RxJava订阅者封装,包括Exception
 */
public abstract class BaseObserver<T> implements Observer<T> {

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }
    @Override
    public void onNext(@NonNull T t) {
        onSuccess(t);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        onError(ExeceptionEngine.handleException(e));
    }

    @Override
    public void onComplete() {
    }
    public abstract void onError(ApiException exception) ;
    public abstract void onSuccess( T t) ;
}
