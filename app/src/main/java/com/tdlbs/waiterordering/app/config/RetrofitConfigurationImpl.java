package com.tdlbs.waiterordering.app.config;
/*
 * Copyright (c) 2019 Nan Jing Tao Dian <TDLBS>. All rights reserved.
 */

import com.tdlbs.core.module.network.retrofit.RetrofitManager;
import com.tdlbs.waiterordering.BuildConfig;
import com.tdlbs.waiterordering.crash.LogToFileUtils;

import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * ================================================
 * RetrofitConfigurationImpl
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-06-10 18:00
 * ================================================
 */
public class RetrofitConfigurationImpl implements RetrofitManager.RetrofitConfiguration {
    @Override
    public void configRetrofit(OkHttpClient.Builder clientBuilder) {
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(interceptor);
            MHttpLoggingInterceptor fileInterceptor = new MHttpLoggingInterceptor(LogToFileUtils::write);
            fileInterceptor.setLevel(MHttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(fileInterceptor);
        } else {
            MHttpLoggingInterceptor interceptor = new MHttpLoggingInterceptor(LogToFileUtils::write);
            interceptor.setLevel(MHttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(interceptor);
        }

        clientBuilder.connectTimeout(20, TimeUnit.SECONDS);
        clientBuilder.writeTimeout(123, TimeUnit.SECONDS);
        clientBuilder.readTimeout(123, TimeUnit.SECONDS);
        clientBuilder.proxy(Proxy.NO_PROXY);
    }
}
