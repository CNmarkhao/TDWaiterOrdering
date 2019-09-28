package com.tdlbs.waiterordering.di.module;

import android.app.Activity;

import com.tdlbs.waiterordering.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * ================================================
 * ActivityModule
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-06-10 08:57
 * ================================================
 */
@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    @Provides
    @ActivityScope
    public Activity provideActivity() {
        return mActivity;
    }
}
