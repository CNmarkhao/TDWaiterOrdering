package com.tdlbs.waiterordering.mvp.page.order.table_console;

import android.content.Context;

import com.tdlbs.core.module.network.retrofit.RetrofitManager;
import com.tdlbs.core.module.network.retrofit.exeception.ApiException;
import com.tdlbs.core.module.rxjava.BaseObserver;
import com.tdlbs.core.ui.mvp.BasePresenter;
import com.tdlbs.waiterordering.app.MISApis;
import com.tdlbs.waiterordering.app.utils.RequestUtils;
import com.tdlbs.waiterordering.app.utils.RxUtils;
import com.tdlbs.waiterordering.constant.AppConstants;
import com.tdlbs.waiterordering.mvp.bean.entity.DiscountProductParam;
import com.tdlbs.waiterordering.mvp.bean.entity.GetOrderDetailParam;
import com.tdlbs.waiterordering.mvp.bean.entity.UpdateOrderParam;
import com.tdlbs.waiterordering.mvp.bean.model.BaseResponse;
import com.tdlbs.waiterordering.mvp.bean.model.OrderDetail;

import javax.inject.Inject;

/**
 * ================================================
 * TableConsolePresenter
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-06-11 20:37
 * ================================================
 */
public class TableConsolePresenter extends BasePresenter<TableConsoleContract.View> implements TableConsoleContract.Model {

    private RetrofitManager mRetrofitManager;
    private Context mContext;

    @Inject
    public TableConsolePresenter(Context mContext, RetrofitManager mRetrofitManager) {
        this.mContext = mContext;
        this.mRetrofitManager = mRetrofitManager;
    }


    @Override
    public void networkGetOrderDetail(GetOrderDetailParam param) {
        mRetrofitManager.createApi(MISApis.class).getOrderDetail(RequestUtils.getMac(),
                RequestUtils.getShopToken(), RequestUtils.getUserToken(), param)
                .compose(RxUtils.applySchedulers(getView()))
                .subscribe(new BaseObserver<BaseResponse<OrderDetail>>() {
                    @Override
                    public void onError(ApiException exception) {
                        getView().getOrderDetailError(param, exception.message);
                    }

                    @Override
                    public void onSuccess(BaseResponse<OrderDetail> response) {
                        if (response.getCode() == AppConstants.Request.REQUEST_SUCCESS) {
                            getView().getOrderDetailOk(response.getData());
                        } else {
                            getView().getOrderDetailError(param, response.getMsg());
                        }
                    }
                });
    }

    @Override
    public void networkUpdateOrder(UpdateOrderParam param) {
        mRetrofitManager.createApi(MISApis.class).updateOrder(RequestUtils.getMac(),
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
                            getView().updateOrderOk(response.getData(), param.getIsChangedTable() == 1);
                        } else {
                            getView().showMessage(response.getMsg());
                        }
                    }
                });
    }

    @Override
    public void networkDiscountProduct(DiscountProductParam param) {
        mRetrofitManager.createApi(MISApis.class).discountProduct(RequestUtils.getMac(),
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
                            getView().discountProduct(response.getData(), param.getDiscount() == 0);
                        } else {
                            getView().showMessage(response.getMsg());
                        }
                    }
                });
    }
}
