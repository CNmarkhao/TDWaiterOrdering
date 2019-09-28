package com.tdlbs.waiterordering.mvp.page.login;

import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.tdlbs.waiterordering.R;
import com.tdlbs.waiterordering.app.TApplication;
import com.tdlbs.waiterordering.app.base.BaseActivity;
import com.tdlbs.waiterordering.app.utils.ButtonUtils;
import com.tdlbs.waiterordering.di.component.DaggerActivityComponent;
import com.tdlbs.waiterordering.di.module.ActivityModule;
import com.tdlbs.waiterordering.mvp.bean.entity.LoginInfoParam;
import com.tdlbs.waiterordering.mvp.page.loading_data.LoadingDataActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * ================================================
 * 服务员点餐登录页
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-06-11 20:36
 * ================================================
 */
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

    @BindView(R.id.et_shop_id)
    EditText mEtShopId;
    @BindView(R.id.et_staff_id)
    EditText mEtStaffId;
    @BindView(R.id.et_staff_password)
    EditText mEtStaffPassword;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        if (!isTaskRoot()) {
            finish();
            return;
        }
        XXPermissions.with(this).constantRequest().permission(Permission.Group.STORAGE).request(new OnPermission() {
            @Override
            public void hasPermission(List<String> granted, boolean isAll) {
                requestPermissionSuccess();
            }

            @Override
            public void noPermission(List<String> denied, boolean quick) {
                AppUtils.exitApp();
            }
        });
    }

    /**
     * 获取权限成功，初始化界面
     */
    private void requestPermissionSuccess() {
        KeyboardUtils.fixAndroidBug5497(LoginActivity.this);
        KeyboardUtils.fixSoftInputLeaks(LoginActivity.this);

        mPresenter.getLocalLoginInfo();
        mEtStaffPassword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginAction();
                KeyboardUtils.hideSoftInput(LoginActivity.this);
                return true;
            }
            return false;
        });

    }

    @Override
    public void updateLoginArea(LoginInfoParam param) {
        mEtShopId.setText(param.getShopCode());
        mEtStaffId.setText(param.getUsername());
        mEtStaffPassword.setText(param.getPassword());

        mEtShopId.setSelection(mEtShopId.length());
        mEtStaffId.setSelection(mEtStaffId.length());
        mEtStaffPassword.setSelection(mEtStaffPassword.length());

        if (StringUtils.isEmpty(param.getShopCode())) {
            mEtShopId.requestFocus();
            return;
        }
        if (StringUtils.isEmpty(param.getUsername())) {
            KeyboardUtils.showSoftInput(mEtStaffId);
            return;
        }
        if (StringUtils.isEmpty(param.getPassword())) {
            KeyboardUtils.showSoftInput(mEtStaffPassword);
            return;
        }

        mPresenter.networkLogin(param);
    }

    private void loginAction() {
        if (mEtShopId.length() == 0) {
            showMessage("请先填写商户账号");
            return;
        }
        if (mEtStaffId.length() == 0) {
            showMessage("请先填写服务员账号");
            return;
        }
        if (mEtStaffPassword.length() == 0) {
            showMessage("请先填写员工密码");
            return;
        }

        mPresenter.networkLogin(new LoginInfoParam(mEtShopId.getText().toString()
                , mEtStaffId.getText().toString(), mEtStaffPassword.getText().toString()));
    }

    @Override
    public void showLoading() {
        super.showLoading();
        mLoadingDialog.setTip("登录中");
    }

    @Override
    public void loginSuccess() {
        ToastUtils.showShort("登录成功");
        readyGoThenKill(LoadingDataActivity.class);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AppUtils.exitApp();
    }

    @Override
    protected void initInjector() {
        DaggerActivityComponent.builder().
                applicationComponent(TApplication.getInstance().getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build().inject(this);
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        if (ButtonUtils.isFastDoubleClick()) {
            return;
        }
        loginAction();
    }
}
