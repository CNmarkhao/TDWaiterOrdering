package com.tdlbs.waiterordering.mvp.widget.menu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

/**
 * ================================================
 * 对类的描述
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-12 16:31
 * ================================================
 */
public class CoordinatorMenuView extends FrameLayout {

    private static final String TAG = "CoordinatorMenu";
    private final int mScreenWidth;
    private final int mScreenHeight;

    private View mMenuView;
    private CoordinatorMainView mCoordinatorMainView;

    private ViewDragHelper mViewDragHelper;

    private static final int MENU_CLOSED = 1;
    private static final int MENU_OPENED = 2;
    private int mMenuState = MENU_CLOSED;

    private int mDragOrientation;
    private static final int LEFT_TO_RIGHT = 3;
    private static final int RIGHT_TO_LEFT = 4;

    private static final float SPRING_BACK_VELOCITY = 1500;
    private static final int SPRING_BACK_DISTANCE = 80;
    private int mSpringBackDistance;

    private static final int MENU_MARGIN_RIGHT = 64;
    private int mMenuWidth;

    private static final int MENU_OFFSET = 128;
    private int mMenuOffset;

    private static final float TOUCH_SLOP_SENSITIVITY = 1.f;

    private static final String DEFAULT_SHADOW_OPACITY = "00";
    private String mShadowOpacity = DEFAULT_SHADOW_OPACITY;
    private int mMenuLeft;
    private int mMainLeft;

    public CoordinatorMenuView(Context context) {
        this(context, null);
    }

    public CoordinatorMenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CoordinatorMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final float density = getResources().getDisplayMetrics().density;//屏幕密度

        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = getResources().getDisplayMetrics().heightPixels;

        mSpringBackDistance = (int) (SPRING_BACK_DISTANCE * density + 0.5f);

        mMenuOffset = (int) (MENU_OFFSET * density + 0.5f);

        mMenuLeft = -mMenuOffset;

        mMainLeft = 0;

        mMenuWidth = mScreenWidth - (int) (MENU_MARGIN_RIGHT * density + 0.5f);

        mViewDragHelper = ViewDragHelper.create(this, TOUCH_SLOP_SENSITIVITY, new CoordinatorCallback());
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    private class CoordinatorCallback extends ViewDragHelper.Callback {

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            mViewDragHelper.captureChildView(mCoordinatorMainView, pointerId);
        }

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return mCoordinatorMainView == child || mMenuView == child;
        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            if (capturedChild == mMenuView) {
                mViewDragHelper.captureChildView(mCoordinatorMainView, activePointerId);
            }
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return 1;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (left < 0) {
                left = 0;
            } else if (left > mMenuWidth) {
                left = mMenuWidth;
            }
            return left;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
//            Log.e(TAG, "onViewReleased: xvel: " + xvel);
            if (mDragOrientation == LEFT_TO_RIGHT) {
                if (xvel > SPRING_BACK_VELOCITY || mCoordinatorMainView.getLeft() > mSpringBackDistance) {
                    openMenu();
                } else {
                    closeMenu();
                }
            } else if (mDragOrientation == RIGHT_TO_LEFT) {
                if (xvel < -SPRING_BACK_VELOCITY || mCoordinatorMainView.getLeft() < mMenuWidth - mSpringBackDistance) {
                    closeMenu();
                } else {
                    openMenu();
                }
            }

        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
//            Log.d(TAG, "onViewPositionChanged: left:" + left);
            mMainLeft = left;
            if (dx > 0) {
                mDragOrientation = LEFT_TO_RIGHT;
            } else if (dx < 0) {
                mDragOrientation = RIGHT_TO_LEFT;
            }
            float scale = (float) (mMenuWidth - mMenuOffset) / (float) mMenuWidth;
            mMenuLeft = left - ((int) (scale * left) + mMenuOffset);
            mMenuView.layout(mMenuLeft, mMenuView.getTop(),
                    mMenuLeft + mMenuWidth, mMenuView.getBottom());
            float showing = (float) (mScreenWidth - left) / (float) mScreenWidth;
            int hex = 255 - Math.round(showing * 255);
            if (hex < 16) {
                mShadowOpacity = "0" + Integer.toHexString(hex);
            } else {
                mShadowOpacity = Integer.toHexString(hex);
            }
        }
    }

    //加载完布局文件后调用
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mMenuView = getChildAt(0);
        mCoordinatorMainView = (CoordinatorMainView) getChildAt(1);
        mCoordinatorMainView.setParent(this);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mViewDragHelper.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //将触摸事件传递给ViewDragHelper，此操作必不可少
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        MarginLayoutParams menuParams = (MarginLayoutParams) mMenuView.getLayoutParams();
        menuParams.width = mMenuWidth;
        mMenuView.setLayoutParams(menuParams);

        mMenuView.layout(mMenuLeft, top, mMenuLeft + mMenuWidth, bottom);
        mCoordinatorMainView.layout(mMainLeft, 0, mMainLeft + mScreenWidth, bottom);
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        final int restoreCount = canvas.save();//保存画布当前的剪裁信息

        final int height = getHeight();
        final int clipLeft = 0;
        int clipRight = mCoordinatorMainView.getLeft();
        if (child == mMenuView) {
            canvas.clipRect(clipLeft, 0, clipRight, height);//剪裁显示的区域
        }

        boolean result = super.drawChild(canvas, child, drawingTime);//绘制当前view

        //恢复画布之前保存的剪裁信息
        //以正常绘制之后的view
        canvas.restoreToCount(restoreCount);


        int shadowLeft = mCoordinatorMainView.getLeft();
        final Paint shadowPaint = new Paint();
        shadowPaint.setColor(Color.parseColor("#" + mShadowOpacity + "777777"));
        shadowPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(shadowLeft, 0, mScreenWidth, mScreenHeight, shadowPaint);

        return result;
    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
        if (mCoordinatorMainView.getLeft() == 0) {
            mMenuState = MENU_CLOSED;
        } else if (mCoordinatorMainView.getLeft() == mMenuWidth) {
            mMenuState = MENU_OPENED;
        }
    }

    public void openMenu() {
        mViewDragHelper.smoothSlideViewTo(mCoordinatorMainView, mMenuWidth, 0);
        ViewCompat.postInvalidateOnAnimation(CoordinatorMenuView.this);
    }

    public void closeMenu() {
        mViewDragHelper.smoothSlideViewTo(mCoordinatorMainView, 0, 0);
        ViewCompat.postInvalidateOnAnimation(CoordinatorMenuView.this);
    }

    public void toogleMenu() {
        if (isOpened()) {
            closeMenu();
        } else {
            openMenu();
        }
    }

    public boolean isOpened() {
        return mMenuState == MENU_OPENED;
    }


}