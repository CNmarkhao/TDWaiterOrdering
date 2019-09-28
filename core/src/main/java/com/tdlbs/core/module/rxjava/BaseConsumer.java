package com.tdlbs.core.module.rxjava;

import io.reactivex.functions.Consumer;


public abstract class BaseConsumer<T> implements Consumer<T> {
    @Override
    public void accept(T t) throws Exception {
        if (t == null) {
            onError();
        } else {
            onSuccess(t);
        }
    }

    public abstract void onSuccess(T t) ;
    public abstract void onError() ;
}
