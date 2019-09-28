package com.tdlbs.waiterordering.app;
/*
 * Copyright (c) 2019 Nan Jing Tao Dian <TDLBS>. All rights reserved.
 */

import android.app.Activity;

import com.blankj.utilcode.util.Utils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.squareup.leakcanary.LeakCanary;
import com.tdlbs.core.module.network.retrofit.RetrofitManager;
import com.tdlbs.core.tool.InputMethodMangerExUtil;
import com.tdlbs.waiterordering.BuildConfig;
import com.tdlbs.waiterordering.R;
import com.tdlbs.waiterordering.crash.CrashHandler;
import com.tdlbs.waiterordering.crash.LogToFileUtils;
import com.tdlbs.waiterordering.di.component.ApplicationComponent;
import com.tdlbs.waiterordering.di.component.DaggerApplicationComponent;
import com.tdlbs.waiterordering.di.module.ApplicationModule;

/**
 * ================================================
 * TApplication
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-06-10 08:57
 * ================================================
 */
public class TApplication extends androidx.multidex.MultiDexApplication {
    static {//使用static代码段可以防止内存泄漏

        //设置全局默认配置（优先级最低，会被其他设置覆盖）
        SmartRefreshLayout.setDefaultRefreshInitializer((context, layout) -> {
            //开始设置全局的基本参数（可以被下面的DefaultRefreshHeaderCreator覆盖）
            layout.setReboundDuration(1000);
            layout.setFooterHeight(100);
            layout.setDisableContentWhenLoading(false);
            layout.setPrimaryColorsId(R.color.white, R.color.gray_3);
        });

        //全局设置默认的 Header
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            //开始设置全局的基本参数（这里设置的属性只跟下面的MaterialHeader绑定，其他Header不会生效，能覆盖DefaultRefreshInitializer的属性和Xml设置的属性）
            return new ClassicsHeader(getInstance());
        });
    }

    private static TApplication sINSTANCE;

    private TActivityLifecycleCallbacks lifecycleCallback;

    public static synchronized TApplication getInstance() {
        return sINSTANCE;
    }

    public static Activity getCurrentVisibleActivity() {
        return sINSTANCE.lifecycleCallback.getCurrentVisibleActivity();
    }

    public static boolean isInForeground() {
        return null != sINSTANCE && null != sINSTANCE.lifecycleCallback && sINSTANCE.lifecycleCallback.isInForeground();
    }

    //dagger2:get ApplicationComponent
    public ApplicationComponent getApplicationComponent() {
        return DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(sINSTANCE)).build();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        //get application
        if (sINSTANCE == null) {
            sINSTANCE = this;
        }
        //lifecyclecallback
        lifecycleCallback = new TActivityLifecycleCallbacks();
        registerActivityLifecycleCallbacks(lifecycleCallback);
        //init ToastUtil
        Utils.init(this);
        //init SpUtil
        InputMethodMangerExUtil.fixFocusedViewLeak(this);
        //init retrofit url
        RetrofitManager.initBaseUrl(BuildConfig.BASE_URL);
        //init stetho
//        Stetho.initializeWithDefaults(this);
        //init logToFileUtils
        LogToFileUtils.init(this);
        //init crashhandler
        CrashHandler.getInstance().init(this);
    }

}