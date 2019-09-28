package com.tdlbs.core.module.network.retrofit;

import android.content.Context;

import com.tdlbs.core.BuildConfig;
import com.tdlbs.core.tool.LogUtil;

import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitManager {

    private static final String TAG = "RetrofitManager";
    private static Retrofit singleton;
    private static OkHttpClient okHttpClient = null;
    private static String BASE_URL = "http://127.0.0.1";

    private void init(RetrofitConfiguration configuration) {
        initOkHttp(configuration);
    }

    public RetrofitManager(RetrofitConfiguration configuration) {
        init(configuration);
    }

    public interface RetrofitConfiguration {
        void configRetrofit(OkHttpClient.Builder builder);
    }

    private void initOkHttp(RetrofitConfiguration configuration) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            //配置SSL证书检测
            builder.sslSocketFactory(SSLSocketClient.getNoSSLSocketFactory());
            builder.hostnameVerifier(SSLSocketClient.getHostnameVerifier());
        }
        configuration.configRetrofit(builder);
        builder.connectTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(123, TimeUnit.SECONDS);
        builder.readTimeout(123, TimeUnit.SECONDS);
        builder.proxy(Proxy.NO_PROXY);
        //错误重连
        builder.retryOnConnectionFailure(true);
        okHttpClient = builder.build();
        LogUtil.i(TAG, "initOkHttp:getNoSSLSocketFactory");
    }

    public static void initBaseUrl(String url) {
        BASE_URL = url;
        LogUtil.i(TAG, " base_url ->" + BASE_URL);
    }

    /**
     * @param clazz   interface
     * @param <T>     interface实例化
     * @return
     */
    public  <T> T createApi(Class<T> clazz) {
        if (singleton == null) {
            synchronized (RetrofitManager.class) {
                if (singleton == null) {
                    Retrofit.Builder builder = new Retrofit.Builder();
                    builder.baseUrl(BASE_URL)
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create())//定义转化器,用Gson将服务器返回的Json格式解析成实体
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create());//关联Rxjava
                    singleton = builder.build();
                }
            }
        }
        return singleton.create(clazz);
    }
}
