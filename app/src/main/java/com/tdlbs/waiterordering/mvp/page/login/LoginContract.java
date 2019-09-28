package com.tdlbs.waiterordering.mvp.page.login;

import com.tdlbs.core.ui.mvp.BaseContract;
import com.tdlbs.core.ui.mvp.BaseModel;
import com.tdlbs.waiterordering.mvp.bean.entity.LoginInfoParam;
import com.tdlbs.waiterordering.mvp.bean.model.LoginInfoBean;

/**
 * ================================================
 * LoginContract
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-06-11 20:37
 * ================================================
 */
public interface LoginContract {
    interface Model extends BaseModel {
        void getLocalLoginInfo();

        void saveLocalLoginInfo(LoginInfoParam param, LoginInfoBean bean);

        void networkLogin(LoginInfoParam param);
    }

    interface View extends BaseContract.BaseView {
        void updateLoginArea(LoginInfoParam param);

        void loginSuccess();
    }

}
