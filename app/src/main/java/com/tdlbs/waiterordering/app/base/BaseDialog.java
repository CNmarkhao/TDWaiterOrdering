package com.tdlbs.waiterordering.app.base;
/*
 * Copyright (c) 2019 Nan Jing Tao Dian <TDLBS>. All rights reserved.
 */

import android.content.Context;
import android.content.Intent;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.tdlbs.core.base.EasyDialogFragment;
import com.tdlbs.core.eventbus.EventCenter;
import com.tdlbs.core.ui.mvp.BasePresenter;
import com.tdlbs.core.ui.toastbar.Toast;
import com.tdlbs.core.ui.toastbar.TopToast;
import com.tdlbs.waiterordering.di.module.DialogModule;

import javax.inject.Inject;

/**
 * ================================================
 * BaseDialog
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-06-10 14:29
 * ================================================
 */
public abstract class BaseDialog<T extends BasePresenter> extends EasyDialogFragment {
    @Inject
    @Nullable
    public T mPresenter;

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
    public void showMessage(String message) {
        TopToast.make((ViewGroup) getActivity().getWindow().getDecorView(), message, Toast.DEFAULT_TIME).show();
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
    public void hideLoading() {

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void onEventComing(EventCenter eventCenter) {

    }

    @Override
    protected Intent getGoIntent(Class<?> clazz) {
        if (BaseDialog.class.isAssignableFrom(clazz)) {
            Intent intent = new Intent(getActivity(), FrameActivity.class);
            intent.putExtra("fragmentName", clazz.getName());
            return intent;
        } else {
            return super.getGoIntent(clazz);
        }
    }


    protected DialogModule getFragmentModule() {
        return new DialogModule(this);
    }
}