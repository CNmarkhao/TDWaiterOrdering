package com.tdlbs.waiterordering.di.module;

import android.app.Activity;
import androidx.fragment.app.DialogFragment;

import com.tdlbs.waiterordering.di.scope.DialogScope;

import dagger.Module;
import dagger.Provides;


/**
 * ================================================
 * DialogModule
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-06-10 08:57
 * ================================================
 */
@Module
public class DialogModule {
    private DialogFragment fragment;

    public DialogModule(DialogFragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @DialogScope
    public Activity provideActivity() {
        return fragment.getActivity();
    }
}
