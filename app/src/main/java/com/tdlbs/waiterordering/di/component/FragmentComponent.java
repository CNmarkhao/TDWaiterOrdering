package com.tdlbs.waiterordering.di.component;


import android.content.Context;

import com.tdlbs.waiterordering.di.module.FragmentModule;
import com.tdlbs.waiterordering.di.scope.FragmentScope;
import com.tdlbs.waiterordering.mvp.page.login.LoginActivity;
import com.tdlbs.waiterordering.mvp.page.order.table_manager.TableManagerFragment;

import dagger.Component;

/**
 * ================================================
 * FragmentComponent
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-06-10 08:57
 * ================================================
 */
@FragmentScope
@Component(dependencies = ApplicationComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {

    Context getContext();


    void inject(TableManagerFragment tableManagerFragment);
}
