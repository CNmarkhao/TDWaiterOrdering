package com.tdlbs.waiterordering.di.component;

import android.app.Activity;
import android.content.Context;

import com.tdlbs.waiterordering.di.module.ActivityModule;
import com.tdlbs.waiterordering.di.scope.ActivityScope;
import com.tdlbs.waiterordering.mvp.page.loading_data.LoadingDataActivity;
import com.tdlbs.waiterordering.mvp.page.login.LoginActivity;
import com.tdlbs.waiterordering.mvp.page.main.MainActivity;
import com.tdlbs.waiterordering.mvp.page.order.choose_product.ChooseProductActivity;
import com.tdlbs.waiterordering.mvp.page.order.shopping_cart.ShoppingCartActivity;
import com.tdlbs.waiterordering.mvp.page.order.table_console.TableConsoleActivity;

import dagger.Component;


/**
 * ================================================
 * ActivityComponent
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-06-10 08:57
 * ================================================
 */
@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface ActivityComponent {
    Activity getActivity();

    Context getContext();


    void inject(LoginActivity loginActivity);

    void inject(LoadingDataActivity loadingDataActivity);

    void inject(MainActivity mainActivity);

    void inject(ChooseProductActivity chooseProductActivity);

    void inject(ShoppingCartActivity shoppingCartActivity);

    void inject(TableConsoleActivity tableConsoleActivity);
}
