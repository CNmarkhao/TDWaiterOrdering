package com.tdlbs.waiterordering.app.base;
/*
 * Copyright (c) 2019 Nan Jing Tao Dian <TDLBS>. All rights reserved.
 */

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.tdlbs.core.base.EasyFragment;
import com.tdlbs.core.eventbus.EventCenter;
import com.tdlbs.core.ui.dialog.dialogactivity.LoadingDialog;
import com.tdlbs.core.ui.mvp.BasePresenter;
import com.tdlbs.core.ui.toastbar.Toast;
import com.tdlbs.core.ui.toastbar.TopToast;
import com.tdlbs.waiterordering.di.module.FragmentModule;

import javax.inject.Inject;

/**
 * ================================================
 * BaseFragment
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-06-10 14:29
 * ================================================
 */
public abstract class BaseFragment<T extends BasePresenter> extends EasyFragment {
    @Inject
    @Nullable
    public T mPresenter;

    protected LoadingDialog mLoadingDialog;

    @Override
    protected void initCreate() {
        //dagger2
        initInjector();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    protected void initInjector() {
    }

    @Override
    protected void initDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void onEventComing(EventCenter eventCenter) {

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
    protected Intent getGoIntent(Class<?> clazz) {
        if (BaseFragment.class.isAssignableFrom(clazz)) {
            Intent intent = new Intent(getActivity(), FrameActivity.class);
            intent.putExtra("fragmentName", clazz.getName());
            return intent;
        } else {
            return super.getGoIntent(clazz);
        }
    }

    @Override
    public void showMessage(String message) {
        TopToast.make((ViewGroup) getActivity().getWindow().getDecorView(), message, Toast.DEFAULT_TIME).show();
    }

    protected FragmentModule getFragmentModule() {
        return new FragmentModule(this);
    }
}