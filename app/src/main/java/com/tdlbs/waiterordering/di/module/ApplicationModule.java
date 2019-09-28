package com.tdlbs.waiterordering.di.module;

import android.content.Context;

import com.tdlbs.core.module.network.retrofit.RetrofitManager;
import com.tdlbs.waiterordering.app.TApplication;
import com.tdlbs.waiterordering.app.config.RetrofitConfigurationImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * ================================================
 * ApplicationModule
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-06-10 08:57
 * ================================================
 */
@Module
public class ApplicationModule {
    private final Context context;

    public ApplicationModule(TApplication context) {
        this.context = context;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return context;
    }

    @Provides
    @Singleton
    RetrofitManager provideRetrofitManager() {
        return new RetrofitManager(new RetrofitConfigurationImpl());
    }
}
