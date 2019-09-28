package com.tdlbs.waiterordering.mvp.widget.menu;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.AdaptScreenUtils;

/**
 * ================================================
 * CoordinatorMainView
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-12 16:31
 * ================================================
 */
public class CoordinatorMainView extends FrameLayout {
    private CoordinatorMenuView mCoordinatorMenu;

    public CoordinatorMainView(Context context) {
        this(context, null, 0);
    }

    public CoordinatorMainView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CoordinatorMainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setParent(CoordinatorMenuView coordinatorMenu) {
        mCoordinatorMenu = coordinatorMenu;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (mCoordinatorMenu.isOpened()) {
            return true;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mCoordinatorMenu.isOpened()) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                mCoordinatorMenu.closeMenu();
            }
            return true;
        }
        return super.onTouchEvent(event);
    }
}