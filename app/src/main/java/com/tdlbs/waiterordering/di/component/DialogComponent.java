package com.tdlbs.waiterordering.di.component;


import com.tdlbs.waiterordering.di.module.FragmentModule;
import com.tdlbs.waiterordering.di.scope.DialogScope;
import com.tdlbs.waiterordering.mvp.dialog.create_order.CreateOrderDg;

import dagger.Component;

/**
 * ================================================
 * DialogComponent
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-06-10 08:57
 * ================================================
 */
@DialogScope
@Component(dependencies = ApplicationComponent.class, modules = FragmentModule.class)
public interface DialogComponent {

    void inject(CreateOrderDg createOrderDg);
}
