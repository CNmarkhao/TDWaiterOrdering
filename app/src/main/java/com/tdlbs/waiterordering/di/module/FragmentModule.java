package com.tdlbs.waiterordering.di.module;

import android.app.Activity;
import androidx.fragment.app.Fragment;

import com.tdlbs.waiterordering.di.scope.FragmentScope;

import dagger.Module;
import dagger.Provides;


/**
 * ================================================
 * FragmentModule
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-06-10 08:57
 * ================================================
 */
@Module
public class FragmentModule {
    private Fragment fragment;

    public FragmentModule(Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @FragmentScope
    public Activity provideActivity() {
        return fragment.getActivity();
    }
}
