package com.tdlbs.waiterordering.mvp.page.login;

import android.content.Context;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.blankj.utilcode.util.SPUtils;
import com.tdlbs.core.module.network.retrofit.RetrofitManager;
import com.tdlbs.core.module.network.retrofit.exeception.ApiException;
import com.tdlbs.core.module.rxjava.BaseObserver;
import com.tdlbs.core.ui.mvp.BasePresenter;
import com.tdlbs.waiterordering.app.MISApis;
import com.tdlbs.waiterordering.app.utils.RequestUtils;
import com.tdlbs.waiterordering.app.utils.RxUtils;
import com.tdlbs.waiterordering.constant.AppConstants;
import com.tdlbs.waiterordering.constant.CacheConstants;
import com.tdlbs.waiterordering.constant.SPConstants;
import com.tdlbs.waiterordering.mvp.bean.entity.LoginInfoParam;
import com.tdlbs.waiterordering.mvp.bean.model.BaseResponse;
import com.tdlbs.waiterordering.mvp.bean.model.LoginInfoBean;

import javax.inject.Inject;

/**
 * ================================================
 * LoginPresenter
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-06-11 20:37
 * ================================================
 */
public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Model {

    private RetrofitManager mRetrofitManager;
    private Context mContext;

    @Inject
    public LoginPresenter(Context mContext, RetrofitManager mRetrofitManager) {
        this.mContext = mContext;
        this.mRetrofitManager = mRetrofitManager;
    }

    @Override
    public void getLocalLoginInfo() {
        String shopCode = SPUtils.getInstance(SPConstants.FileName.LOGIN).getString(SPConstants.Login.SHOP_ID);
        String username = SPUtils.getInstance(SPConstants.FileName.LOGIN).getString(SPConstants.Login.STAFF_ID);
        String password = SPUtils.getInstance(SPConstants.FileName.LOGIN).getString(SPConstants.Login.PASSWORD);
        LoginInfoParam param = new LoginInfoParam(shopCode, username, password);
        getView().updateLoginArea(param);
    }

    @Override
    public void saveLocalLoginInfo(LoginInfoParam param, LoginInfoBean bean) {
        SPUtils.getInstance(SPConstants.FileName.LOGIN).put(SPConstants.Login.SHOP_ID, param.getShopCode().trim());
        SPUtils.getInstance(SPConstants.FileName.LOGIN).put(SPConstants.Login.STAFF_ID, param.getUsername().trim());
        SPUtils.getInstance(SPConstants.FileName.LOGIN).put(SPConstants.Login.PASSWORD, param.getPassword().trim());
        CacheDiskUtils.getInstance().put(CacheConstants.Login.LOGIN_INFO_BEAN, bean);
    }

    @Override
    public void networkLogin(LoginInfoParam param) {
        mRetrofitManager.createApi(MISApis.class).loginStaff(RequestUtils.getMac(), param)
                .compose(RxUtils.applySchedulers(getView()))
                .subscribe(new BaseObserver<BaseResponse<LoginInfoBean>>() {
                    @Override
                    public void onError(ApiException exception) {
                        getView().showMessage(exception.message);
                    }

                    @Override
                    public void onSuccess(BaseResponse<LoginInfoBean> response) {
                        if (response.getCode() == AppConstants.Request.REQUEST_SUCCESS) {
                            saveLocalLoginInfo(param, response.getData());
                            getView().loginSuccess();
                        } else {
                            getView().showMessage(response.getMsg());
                        }
                    }
                });


    }
}
