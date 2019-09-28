package com.tdlbs.waiterordering.di.component;

import android.content.Context;

import com.tdlbs.core.module.network.retrofit.RetrofitManager;
import com.tdlbs.waiterordering.app.TApplication;
import com.tdlbs.waiterordering.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;


/**
 * ================================================
 * ApplicationComponent
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-06-10 08:57
 * ================================================
 */
@Singleton
@Component(
        modules = {
                ApplicationModule.class
        }
)
public interface ApplicationComponent {
    Context getContext();  // 提供App的Context

    RetrofitManager getRetrofitManager();  //提供http的帮助类

    void inject(TApplication application);
}
