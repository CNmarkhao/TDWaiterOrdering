package com.tdlbs.waiterordering.mvp.widget.searchview.util;

/**
 * ================================================
 * 对类的描述
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-17 12:00
 * ================================================
 */

import android.animation.Animator;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;

import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;

/**
 * 动画使用工具，控制RevealSearchView的显隐，LOLLIPOP以上为Reveal模式，否则为Fade模式
 *
 * @author 2017/6/15 15:27 / changliugang
 */
public class AnimationTools {

    // 动画持续时间
    public static final int ANIMATION_DURATION = 300;

    public static void fadeInView(View targetView) {
        fadeInView(targetView, ANIMATION_DURATION);
    }

    /**
     * 渐隐式显示控件
     *
     * @param targetView 目标控件
     * @param duration   动画持续时间
     */
    public static void fadeInView(final View targetView, int duration) {
        ViewCompat.animate(targetView).alpha(1f).setDuration(duration).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {
                if (targetView.getVisibility() != View.VISIBLE)
                    targetView.setVisibility(View.VISIBLE);
                targetView.setAlpha(0f);
            }

            @Override
            public void onAnimationEnd(View view) {

            }

            @Override
            public void onAnimationCancel(View view) {

            }
        });
    }

    public static void fadeOutView(View targetView) {
        fadeOutView(targetView, ANIMATION_DURATION);
    }

    /**
     * 渐隐式隐藏控件
     *
     * @param targetView 目标控件
     * @param duration   动画持续时间
     */
    public static void fadeOutView(final View targetView, int duration) {
        ViewCompat.animate(targetView).alpha(0f).setDuration(duration).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {

            }

            @Override
            public void onAnimationEnd(View view) {
                if (targetView.getVisibility() != View.GONE)
                    targetView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(View view) {

            }
        });
    }

    /**
     * 揭露式显示/隐藏控件
     *
     * @param targetView 目标控件
     * @param isShow     true 为显示View false为隐藏View
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void revealViewToggle(final View targetView, final boolean isShow) {
// 圆点坐标
        int cx = targetView.getWidth();
        int cy = targetView.getHeight() / 2;
        // 半径
        int endRadius = Math.max(targetView.getWidth(), targetView.getHeight());
        Animator circularReveal;
        if (isShow) {
            circularReveal = ViewAnimationUtils.createCircularReveal(targetView, cx, cy, 0, endRadius);
        } else {
            circularReveal = ViewAnimationUtils.createCircularReveal(targetView, cx, cy, endRadius, 0);
        }
        circularReveal.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (isShow)
                    targetView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isShow)
                    targetView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        circularReveal.start();
    }

}