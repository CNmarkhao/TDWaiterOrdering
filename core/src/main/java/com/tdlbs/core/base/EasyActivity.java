package com.tdlbs.core.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.tdlbs.core.R;
import com.tdlbs.core.eventbus.EventCenter;
import com.tdlbs.core.module.permission.EasyPermissions;
import com.tdlbs.core.tool.immersion.StatusBarUtil;
import com.tdlbs.core.ui.dialog.dialogactivity.CommonDialog;
import com.tdlbs.core.ui.dialog.dialogactivity.LoadingDialog;
import com.tdlbs.core.ui.mvp.BaseContract;
import com.tdlbs.core.ui.viewstatus.VaryViewHelperController;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class EasyActivity extends RxAppCompatActivity implements EasyPermissions.PermissionWithDialogCallbacks, BaseContract.BaseView {

    /**
     * log tag
     */
    protected static String TAG = "EasyActivity";

    /**
     * context
     */
    protected Context mContext = null;

    /**
     * screen information
     */
    protected int mScreenWidth = 0;
    protected int mScreenHeight = 0;
    protected float mScreenDensity = 0.0f;

    /**
     * loading view controller
     */
    private VaryViewHelperController mVaryViewHelperController = null;

    /**
     * overridePendingTransition mode
     */
    public enum TransitionMode {
        LEFT, RIGHT, TOP, BOTTOM, SCALE, FADE
    }

    /**
     * butterknife 8+ support
     */
    private Unbinder mUnbinder;


    /**
     * default toolbar
     */
    protected TextView tvTitle;
    protected TextView tvRight;
    protected Toolbar toolbar;
    protected FrameLayout toolbarLayout;
    private LinearLayout contentView;
    /**
     * dialog
     */
    protected LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        initCreate();
        mContext = this;

        // animation
        if (toggleOverridePendingTransition()) {
            switch (getOverridePendingTransitionMode()) {
                case LEFT:
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    break;
                case RIGHT:
                    overridePendingTransition(R.anim.right_in, R.anim.right_out);
                    break;
                case TOP:
                    overridePendingTransition(R.anim.top_in, R.anim.top_out);
                    break;
                case BOTTOM:
                    overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
                    break;
                case SCALE:
                    overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
                    break;
                case FADE:
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    break;
                default:
            }
        }
        // extras
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            getBundleExtras(extras);
        }
        // eventbus
        EventBus.getDefault().register(this);
        // screen info
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mScreenDensity = displayMetrics.density;
        mScreenHeight = displayMetrics.heightPixels;
        mScreenWidth = displayMetrics.widthPixels;
        int layoutId = getContentViewLayoutID();
        if (layoutId != 0) {
            setContentView(layoutId);
        } else {
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");
        }

        // system status bar immersion
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, false)) {
            StatusBarUtil.setStatusBarColor(this, 0x55000000);
        }

        initViewsAndEvents(savedInstanceState);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//        }
    }

    @Override
    public void setContentView(int layoutResID) {
        // 添加toolbar布局
        if (isLoadDefaultTitleBar()) {
            initToolBarView();
            initContentView();
            contentView.addView(toolbarLayout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            View view = getContentView(layoutResID, contentView);
            if (view == null) {

                LayoutInflater.from(this).inflate(layoutResID, contentView, true);

                super.setContentView(contentView, new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
            } else {
                super.setContentView(view, new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
            }
        } else {
            super.setContentView(layoutResID);
        }
        mUnbinder = ButterKnife.bind(this);
        if (null != getLoadingTargetView()) {
            mVaryViewHelperController = new VaryViewHelperController(getLoadingTargetView());
        }
    }

    protected abstract void initCreate();

    protected abstract void initDestroy();

    protected abstract boolean isLoadDefaultTitleBar();

    protected View getContentView(int layoutResID, LinearLayout contentView) {
        return null;
    }

    protected void initContentView() {
        contentView = new LinearLayout(this);
        contentView.setOrientation(LinearLayout.VERTICAL);
    }

    private void initToolBarView() {
        toolbarLayout = new FrameLayout(this);
        LayoutInflater.from(this).inflate(R.layout.layout_toolbar, toolbarLayout,
                true);
        tvTitle = (TextView) toolbarLayout.findViewById(R.id.tv_title);
        tvRight = (TextView) toolbarLayout.findViewById(R.id.tv_right);
        toolbar = (Toolbar) toolbarLayout.findViewById(R.id.toolbar);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        initToolBarSet(actionBar);
    }

    protected void initToolBarSet(ActionBar actionBar) {
        actionBar.setDisplayShowTitleEnabled(false);

        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeAsUpIndicator(R.drawable.icon_back);
            toolbar.setNavigationOnClickListener(v -> finish());
        }
    }

    protected Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public void finish() {
        super.finish();
        if (toggleOverridePendingTransition()) {
            switch (getOverridePendingTransitionMode()) {
                case LEFT:
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    break;
                case RIGHT:
                    overridePendingTransition(R.anim.right_in, R.anim.right_out);
                    break;
                case TOP:
                    overridePendingTransition(R.anim.top_in, R.anim.top_out);
                    break;
                case BOTTOM:
                    overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
                    break;
                case SCALE:
                    overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
                    break;
                case FADE:
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    break;
                default:
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        initDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        EventBus.getDefault().unregister(this);
        dismissLoadingDialog();
    }

    /**
     * get bundle data
     *
     * @param extras
     */
    protected abstract void getBundleExtras(Bundle extras);

    /**
     * bind layout resource file
     *
     * @return id of layout resource
     */
    protected abstract int getContentViewLayoutID();

    /**
     * when event coming
     *
     * @param eventCenter
     */
    protected abstract void onEventComing(EventCenter eventCenter);

    /**
     * get loading target view
     */
    protected abstract View getLoadingTargetView();

    /**
     * init all views and add events
     */
    protected abstract void initViewsAndEvents(Bundle savedInstanceState);

    /**
     * toggle overridePendingTransition
     *
     * @return
     */
    protected abstract boolean toggleOverridePendingTransition();

    /**
     * get the overridePendingTransition mode
     */
    protected abstract TransitionMode getOverridePendingTransitionMode();

    /**
     * apply systembar color tint
     */
    protected abstract boolean isApplySystemBarTint();

    /**
     * 会被子类覆盖去封装 跳转到不同的activity 或者fragment的页面
     *
     * @param clazz
     * @return
     */
    protected Intent getGoIntent(Class<?> clazz) {
        return new Intent(this, clazz);
    }


    /**
     * startActivity
     *
     * @param clazz
     */
    protected void readyGo(Class<?> clazz) {
        Intent intent = getGoIntent(clazz);
        startActivity(intent);
    }

    /**
     * startActivity with  flag
     *
     * @param clazz
     * @param flag
     */
    protected void readyGo(Class<?> clazz, int flag) {
        Intent intent = getGoIntent(clazz);
        intent.setFlags(flag);
        startActivity(intent);
    }

    /**
     * startActivity with bundle
     *
     * @param clazz
     * @param bundle
     */
    protected void readyGo(Class<?> clazz, Bundle bundle) {
        Intent intent = getGoIntent(clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * startActivity with bundle and flag
     *
     * @param clazz
     * @param bundle
     * @param flag
     */
    protected void readyGo(Class<?> clazz, Bundle bundle, int flag) {
        Intent intent = getGoIntent(clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        intent.setFlags(flag);
        startActivity(intent);
    }


    /**
     * startActivity then finish
     *
     * @param clazz
     */
    protected void readyGoThenKill(Class<?> clazz) {
        Intent intent = getGoIntent(clazz);
        startActivity(intent);
        finish();
    }

    /**
     * startActivity with bundle then finish
     *
     * @param clazz
     * @param bundle
     */
    protected void readyGoThenKill(Class<?> clazz, Bundle bundle) {
        Intent intent = getGoIntent(clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        finish();
    }

    /**
     * startActivityForResult
     *
     * @param clazz
     * @param requestCode
     */
    protected void readyGoForResult(Class<?> clazz, int requestCode) {
        Intent intent = getGoIntent(clazz);
        startActivityForResult(intent, requestCode);
    }

    /**
     * startActivityForResult with bundle
     *
     * @param clazz
     * @param requestCode
     * @param bundle
     */
    protected void readyGoForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * show toast
     *
     * @param msg
     */
    protected void showToast(String msg) {
        //防止遮盖虚拟按键
        if (null != msg && !TextUtils.isEmpty(msg)) {
            Snackbar.make(getLoadingTargetView(), msg, Snackbar.LENGTH_SHORT).show();
        }
    }

    /**
     * toggle show loading
     *
     * @param toggle
     */
    protected void toggleShowLoading(boolean toggle, String msg) {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }

        if (toggle) {
            mVaryViewHelperController.showLoading(msg);
        } else {
            mVaryViewHelperController.restore();
        }
    }

    /**
     * toggle show empty
     *
     * @param toggle
     */
    protected void toggleShowEmpty(boolean toggle, String msg, View.OnClickListener onClickListener) {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }

        if (toggle) {
            mVaryViewHelperController.showEmpty(msg, onClickListener);
        } else {
            mVaryViewHelperController.restore();
        }
    }

    /**
     * toggle show error
     *
     * @param toggle
     */
    protected void toggleShowError(boolean toggle, String msg, View.OnClickListener onClickListener) {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }

        if (toggle) {
            mVaryViewHelperController.showError(msg, onClickListener);
        } else {
            mVaryViewHelperController.restore();
        }
    }

    /**
     * toggle show network error
     *
     * @param toggle
     */
    protected void toggleNetworkError(boolean toggle, View.OnClickListener onClickListener) {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }

        if (toggle) {
            mVaryViewHelperController.showNetworkError(onClickListener);
        } else {
            mVaryViewHelperController.restore();
        }
    }

    protected void toggleRestore() {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }
        mVaryViewHelperController.restore();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBus(EventCenter eventCenter) {
        if (null != eventCenter) {
            onEventComing(eventCenter);
        }
    }

    /**
     * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
     * Android6.0权限控制
     * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
     */
    private Map<Integer, PermissionCallback> mPermissonCallbacks = null;
    private Map<Integer, String[]> mPermissions = null;

    public interface PermissionCallback {
        /**
         * has all permission
         *
         * @param allPerms all permissions
         */
        void hasPermission(List<String> allPerms);

        /**
         * denied some permission
         *
         * @param deniedPerms          denied permission
         * @param grantedPerms         granted permission
         * @param hasPermanentlyDenied has permission denied permanently
         */
        void noPermission(List<String> deniedPerms, List<String> grantedPerms, Boolean hasPermanentlyDenied);

        /**
         * @param dialogType dialogType
         * @param callback   callback from easypermissions
         */
        void showDialog(int dialogType, EasyPermissions.DialogCallback callback);
    }

    /**
     * request permission
     *
     * @param dialogType  dialogType
     * @param requestCode requestCode
     * @param perms       permissions
     * @param callback    callback
     */
    public void performCodeWithPermission(int dialogType,
                                          final int requestCode, @NonNull String[] perms, @NonNull PermissionCallback callback) {
        if (EasyPermissions.hasPermissions(this, perms)) {
            callback.hasPermission(Arrays.asList(perms));
        } else {
            if (mPermissonCallbacks == null) {
                mPermissonCallbacks = new HashMap<>();
            }
            mPermissonCallbacks.put(requestCode, callback);

            if (mPermissions == null) {
                mPermissions = new HashMap<>();
            }
            mPermissions.put(requestCode, perms);

            EasyPermissions.requestPermissions(this, dialogType, requestCode, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (mPermissonCallbacks == null || !mPermissonCallbacks.containsKey(requestCode)) {
            return;
        }
        if (mPermissions == null || !mPermissions.containsKey(requestCode)) {
            return;
        }

        // 100% granted permissions
        if (mPermissions.get(requestCode).length == perms.size()) {
            mPermissonCallbacks.get(requestCode).hasPermission(Arrays.asList(mPermissions.get(requestCode)));
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (mPermissonCallbacks == null || !mPermissonCallbacks.containsKey(requestCode)) {
            return;
        }
        if (mPermissions == null || !mPermissions.containsKey(requestCode)) {
            return;
        }

        //granted permission
        List<String> grantedPerms = new ArrayList<>();
        for (String perm : mPermissions.get(requestCode)) {
            if (!perms.contains(perm)) {
                grantedPerms.add(perm);
            }
        }

        //check has permission denied permanently
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            mPermissonCallbacks.get(requestCode).noPermission(perms, grantedPerms, true);
        } else {
            mPermissonCallbacks.get(requestCode).noPermission(perms, grantedPerms, false);
        }
    }

    @Override
    public void onDialog(int requestCode, int dialogType, EasyPermissions.DialogCallback callback) {
        if (mPermissonCallbacks == null || !mPermissonCallbacks.containsKey(requestCode)) {
            return;
        }
        mPermissonCallbacks.get(requestCode).showDialog(dialogType, callback);
    }

    public void showLoadingDialog() {
        showLoadingDialog(null);
    }

    public void showLoadingDialog(String tips) {
        if (!isFinishing()) {
            try {
                if (loadingDialog == null) {
                    loadingDialog = new LoadingDialog(this);
                    if (!TextUtils.isEmpty(tips)) {
                        loadingDialog.setTip(tips);
                    }
                    loadingDialog.show();
                } else {
                    //相同的上下文，无需重新创建
                    if (loadingDialog.getLoadingDialogContext() == this) {
                        loadingDialog.show();
                    } else {
                        loadingDialog.dismiss();
                        loadingDialog = new LoadingDialog(this);
                        if (!TextUtils.isEmpty(tips)) {
                            loadingDialog.setTip(tips);
                        }
                        loadingDialog.show();
                    }
                }

            } catch (Throwable e) {
            }
        }
    }

    public void dismissLoadingDialog() {
        try {
            if (!isFinishing() && loadingDialog != null && loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
        } catch (Throwable e) {
        }
    }

    public CommonDialog.Builder getDialogBuilder(Context context) {
        return new CommonDialog.Builder(context);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showSuccess() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showFail() {

    }

    @Override
    public void showRetry() {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.<T>bindToLifecycle();
    }
}
