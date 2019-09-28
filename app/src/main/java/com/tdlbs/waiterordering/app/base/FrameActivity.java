package com.tdlbs.waiterordering.app.base;
/*
 * Copyright (c) 2019 Nan Jing Tao Dian <TDLBS>. All rights reserved.
 */

import android.os.Bundle;

import com.blankj.utilcode.util.ReflectUtils;
import com.tdlbs.core.base.EasyFragment;
import com.tdlbs.core.tool.LogUtil;
import com.tdlbs.waiterordering.R;

/**
 * ================================================
 * FrameActivity
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-06-10 14:33
 * ================================================
 */
public class FrameActivity extends BaseActivity {

    protected static String TAG = "FrameActivity";
    private Bundle bundle;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_frame;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        bundle = extras;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        String className = bundle.getString("fragmentName");
        LogUtil.i(TAG, "the fragment class name is->" + className);
        if (className != null) {
            Object object = ReflectUtils.reflect(className).get();
            if (object instanceof EasyFragment) {
                EasyFragment fragment = (EasyFragment) object;
                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commitAllowingStateLoss();
                }
            } else {
                LogUtil.e(TAG, " the fragment class is not exist!!!");
            }
        }
    }

    @Override
    protected void initInjector() {

    }
}
