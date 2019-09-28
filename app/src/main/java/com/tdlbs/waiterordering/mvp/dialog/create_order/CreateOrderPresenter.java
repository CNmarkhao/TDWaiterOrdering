package com.tdlbs.waiterordering.mvp.dialog.create_order;

import android.content.Context;

import com.tdlbs.core.module.network.retrofit.RetrofitManager;
import com.tdlbs.core.module.network.retrofit.exeception.ApiException;
import com.tdlbs.core.module.rxjava.BaseObserver;
import com.tdlbs.core.ui.mvp.BasePresenter;
import com.tdlbs.waiterordering.app.MISApis;
import com.tdlbs.waiterordering.app.utils.RequestUtils;
import com.tdlbs.waiterordering.app.utils.RxUtils;
import com.tdlbs.waiterordering.constant.AppConstants;
import com.tdlbs.waiterordering.mvp.bean.entity.CreateOrderParam;
import com.tdlbs.waiterordering.mvp.bean.model.BaseResponse;
import com.tdlbs.waiterordering.mvp.bean.model.OrderDetail;

import javax.inject.Inject;

/**
 * ================================================
 * CreateOrderPresenter
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-06-11 20:37
 * ================================================
 */
public class CreateOrderPresenter extends BasePresenter<CreateOrderContract.View> implements CreateOrderContract.Model {

    private RetrofitManager mRetrofitManager;
    private Context mContext;

    @Inject
    public CreateOrderPresenter(Context mContext, RetrofitManager mRetrofitManager) {
        this.mContext = mContext;
        this.mRetrofitManager = mRetrofitManager;
    }

    @Override
    public void networkCreateOrder(CreateOrderParam param) {
        mRetrofitManager.createApi(MISApis.class).createTableOrder(RequestUtils.getMac(),
                RequestUtils.getShopToken(), RequestUtils.getUserToken(), param)
                .compose(RxUtils.applySchedulers(getView()))
                .subscribe(new BaseObserver<BaseResponse<OrderDetail>>() {
                    @Override
                    public void onError(ApiException exception) {
                        getView().showMessage(exception.message);
                    }

                    @Override
                    public void onSuccess(BaseResponse<OrderDetail> response) {
                        if (response.getCode() == AppConstants.Request.REQUEST_SUCCESS) {
                            getView().createOrderOk(response.getData());
                        } else {
                            getView().showMessage(response.getMsg());
                        }
                    }
                });
    }
}
