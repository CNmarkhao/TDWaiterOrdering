package com.tdlbs.waiterordering.app;
/*
 * Copyright (c) 2019 Nan Jing Tao Dian <TDLBS>. All rights reserved.
 */

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.tdlbs.core.tool.LogUtil;

import java.util.Stack;

/**
 * ================================================
 * TActivityLifecycleCallbacks
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-06-10 09:01
 * ================================================
 */
public class TActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    public static Stack<Activity> store = new Stack<>();
    private static String TAG = "AQActivityLifecycleCallbacks";
    private Activity visibleActivity = null;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        LogUtil.i(TAG, activity.getLocalClassName() + " was Created, " + "activity==null    "
                + (activity == null) + ", activity.isFinishing()    " + (activity.isFinishing()) + ", activity.isDestroyed()    " + activity.isDestroyed());
    }

    @Override
    public void onActivityStarted(Activity activity) {
        LogUtil.i(TAG, activity.getLocalClassName() + " was Started, " + "activity==null    "
                + (activity == null) + ", activity.isFinishing()    " + (activity.isFinishing()) + ", activity.isDestroyed()    " + activity.isDestroyed());
        store.add(activity);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        LogUtil.i(TAG, activity.getLocalClassName() + " was oResumed, " + "activity==null   "
                + (activity == null) + ", activity.isFinishing()    " + (activity.isFinishing()) + ", activity.isDestroyed()    " + activity.isDestroyed());
        visibleActivity = activity;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        LogUtil.i(TAG, activity.getLocalClassName() + " was Pauseed, " + "activity==null  "
                + (activity == null) + ", activity.isFinishing()    " + (activity.isFinishing()) + ", activity.isDestroyed()    " + activity.isDestroyed());
        visibleActivity = null;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        LogUtil.i(TAG, activity.getLocalClassName() + " was Stoped, " + "activity==null   "
                + (activity == null) + ", activity.isFinishing()    " + (activity.isFinishing()) + ", activity.isDestroyed()    " + activity.isDestroyed());
        store.remove(activity);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        LogUtil.i(TAG, activity.getLocalClassName() + " was SaveInstanceState, " + "activity==null    "
                + (activity == null) + ", activity.isFinishing()    " + (activity.isFinishing()) + ", activity.isDestroyed()    " + activity.isDestroyed());
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        LogUtil.i(TAG, activity.getLocalClassName() + " was Destroyed, " + "activity==null  "
                + (activity == null) + ", activity.isFinishing()    " + (activity.isFinishing()) + ", activity.isDestroyed()    " + activity.isDestroyed());
    }

    public Activity getCurrentVisibleActivity() {
        return visibleActivity;
    }

    public boolean isInForeground() {
        return store.size() > 0;
    }
}