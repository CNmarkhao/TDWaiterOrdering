package com.tdlbs.waiterordering.mvp.page.main;

import android.content.Context;

import com.tdlbs.core.module.network.retrofit.RetrofitManager;
import com.tdlbs.core.ui.mvp.BasePresenter;

import javax.inject.Inject;

/**
 * ================================================
 * MainPresenter
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-06-11 20:37
 * ================================================
 */
public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Model {

    private RetrofitManager mRetrofitManager;
    private Context mContext;

    @Inject
    public MainPresenter(Context mContext, RetrofitManager mRetrofitManager) {
        this.mContext = mContext;
        this.mRetrofitManager = mRetrofitManager;
    }

}
