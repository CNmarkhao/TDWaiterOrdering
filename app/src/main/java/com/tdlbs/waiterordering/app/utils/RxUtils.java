package com.tdlbs.waiterordering.app.utils;

import com.tdlbs.core.ui.mvp.BaseContract;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RxUtils {

    private RxUtils() {
    }

    public static <T> ObservableTransformer<T, T> applySchedulers(final BaseContract.BaseView view) {
        return applySchedulers(view, false);
    }

    public static <T> ObservableTransformer<T, T> applySchedulers(final BaseContract.BaseView view, boolean isAuto) {
        return observable -> observable.subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    if (!isAuto) {
                        view.showLoading();//显示进度条
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (!isAuto) {
                        view.hideLoading();//隐藏进度条
                    }
                }).compose(view.bindToLife());
    }

}
