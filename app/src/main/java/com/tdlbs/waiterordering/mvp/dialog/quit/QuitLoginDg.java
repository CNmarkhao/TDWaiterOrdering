package com.tdlbs.waiterordering.mvp.dialog.quit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.tdlbs.waiterordering.R;
import com.tdlbs.waiterordering.app.base.BaseDialog;
import com.tdlbs.waiterordering.constant.SPConstants;
import com.tdlbs.waiterordering.mvp.page.login.LoginActivity;

import butterknife.OnClick;

/**
 * ================================================
 * 退出登录对话框
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-13 19:50
 * ================================================
 */
public class QuitLoginDg extends BaseDialog {
    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.dialog_quit_login;
    }

    /**
     * 解决Cancel，onResume再次显示问题
     */
    @Override
    public void onStop() {
        super.onStop();
        dismissAllowingStateLoss();
    }

    @OnClick({R.id.cancel_btn, R.id.confirm_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel_btn:
                dismissAllowingStateLoss();
                break;
            case R.id.confirm_btn:
                quitLogin();
                break;
            default:
                break;
        }
    }

    /**
     * 退出登录
     * 清空密码信息，清空跳转登录界面
     */
    private void quitLogin() {
        SPUtils.getInstance(SPConstants.FileName.LOGIN).put(SPConstants.Login.PASSWORD, "");

        Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(loginIntent);
    }
}
