package com.tdlbs.waiterordering.app.base;
/*
 * Copyright (c) 2019 Nan Jing Tao Dian <TDLBS>. All rights reserved.
 */


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.AdaptScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tdlbs.core.base.EasyActivity;
import com.tdlbs.core.eventbus.EventCenter;
import com.tdlbs.core.tool.LogUtil;
import com.tdlbs.core.ui.dialog.dialogactivity.LoadingDialog;
import com.tdlbs.core.ui.mvp.BasePresenter;
import com.tdlbs.core.ui.toastbar.BottomToast;
import com.tdlbs.core.ui.toastbar.Toast;
import com.tdlbs.core.ui.toastbar.TopToast;
import com.tdlbs.waiterordering.BuildConfig;
import com.tdlbs.waiterordering.di.module.ActivityModule;

import javax.inject.Inject;

/**
 * ================================================
 * BaseActivity
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-06-10 08:57
 * ================================================
 */
public abstract class BaseActivity<T extends BasePresenter> extends EasyActivity {
    private final static int sOneSecond = 1000;
    protected NoActionTimer noActionTimer;
    protected static String TAG = "BaseActivity";
    @Inject
    @Nullable
    public T mPresenter;

    protected LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtil.i(TAG, "onCreate:" + getClass().getSimpleName());
        //dagger2
        initInjector();
        if (mPresenter != null) {
            mPresenter.attachView(this);

        }
        noActionTimer = new NoActionTimer(BuildConfig.STANDBY_TIME * sOneSecond, sOneSecond);
        super.onCreate(savedInstanceState);
    }

    @Override
    public Resources getResources() {
        return AdaptScreenUtils.adaptWidth(super.getResources(), 1080);
    }

    protected void initInjector() {

    }

    @Override
    public void showMessage(String message) {
        TopToast.make((ViewGroup) getWindow().getDecorView(), message, Toast.DEFAULT_TIME).show();
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(mContext);
            mLoadingDialog.setTip("处理中");
        }
        mLoadingDialog.show();

    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog == null) {
            return;
        }
        mLoadingDialog.dismiss();
    }

    @Override
    protected void onPause() {
        super.onPause();
        noActionTimer.cancel();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (noActionTimer == null) {
            return;
        }
        runOnUiThread(() -> startNoActionTimer());
    }

    protected void startNoActionTimer() {
        noActionTimer.start();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                noActionTimer.start();
                break;
            default:
                noActionTimer.cancel();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
        if (noActionTimer != null) {
            noActionTimer.cancel();
        }
        super.onDestroy();
        LogUtil.i(TAG, "onDestroy:" + getClass().getSimpleName());
    }

    @Override
    protected void initCreate() {

    }

    @Override
    protected void initDestroy() {

    }

    @Override
    protected void getBundleExtras(Bundle extras) {
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void onEventComing(EventCenter eventCenter) {
        LogUtil.i(TAG, eventCenter.getEventCode() + "");
    }

    @Override
    protected boolean toggleOverridePendingTransition() {
        return true;
    }

    @Override
    protected EasyActivity.TransitionMode getOverridePendingTransitionMode() {
        return EasyActivity.TransitionMode.LEFT;
    }

    @Override
    protected boolean isApplySystemBarTint() {
        return true;
    }

    @Override
    protected boolean isLoadDefaultTitleBar() {
        return false;
    }

    @Override
    protected Intent getGoIntent(Class<?> clazz) {
        if (BaseFragment.class.isAssignableFrom(clazz)) {
            Intent intent = new Intent(this, FrameActivity.class);
            intent.putExtra("fragmentName", clazz.getName());
            return intent;
        } else {
            return super.getGoIntent(clazz);
        }
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

    class NoActionTimer extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public NoActionTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            showNoActionBackHomeDialog();
        }
    }

    protected void showNoActionBackHomeDialog() {

    }
}
