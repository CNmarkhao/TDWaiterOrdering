package com.tdlbs.core.ui.mvp;

import com.trello.rxlifecycle2.LifecycleTransformer;


public interface BaseContract {

    interface BasePresenter<T extends BaseContract.BaseView> {

        void attachView(T view);

        void detachView();
    }


    interface BaseView {

        void showLoading();

        void hideLoading();

        void showSuccess();

        void showFail();

        void showRetry();

        void showMessage(String message);

        <T> LifecycleTransformer<T> bindToLife();
    }
}
