package com.tdlbs.waiterordering.mvp.page.order.table_manager;

import android.content.Context;

import com.tdlbs.core.module.network.retrofit.RetrofitManager;
import com.tdlbs.core.module.network.retrofit.exeception.ApiException;
import com.tdlbs.core.module.rxjava.BaseObserver;
import com.tdlbs.core.ui.mvp.BasePresenter;
import com.tdlbs.waiterordering.app.MISApis;
import com.tdlbs.waiterordering.app.utils.LocalCacheUtils;
import com.tdlbs.waiterordering.app.utils.RequestUtils;
import com.tdlbs.waiterordering.app.utils.RxUtils;
import com.tdlbs.waiterordering.constant.AppConstants;
import com.tdlbs.waiterordering.mvp.bean.entity.EmptyParam;
import com.tdlbs.waiterordering.mvp.bean.model.BaseResponse;
import com.tdlbs.waiterordering.mvp.bean.model.TableList;

import javax.inject.Inject;

/**
 * ================================================
 * TableManagerPresenter
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-06-11 20:37
 * ================================================
 */
public class TableManagerPresenter extends BasePresenter<TableManagerContract.View> implements TableManagerContract.Model {

    private RetrofitManager mRetrofitManager;
    private Context mContext;

    @Inject
    public TableManagerPresenter(Context mContext, RetrofitManager mRetrofitManager) {
        this.mContext = mContext;
        this.mRetrofitManager = mRetrofitManager;
    }

    @Override
    public void networkUpdateTablesStatus(boolean isAuto) {
        mRetrofitManager.createApi(MISApis.class).updateTablesStatus(RequestUtils.getMac(),
                RequestUtils.getShopToken(), RequestUtils.getUserToken(), new EmptyParam())
                .compose(RxUtils.applySchedulers(getView(),isAuto))
                .subscribe(new BaseObserver<BaseResponse<TableList>>() {
                    @Override
                    public void onError(ApiException exception) {
                        getView().showMessage(exception.message);
                    }

                    @Override
                    public void onSuccess(BaseResponse<TableList> response) {
                        if (response.getCode() == AppConstants.Request.REQUEST_SUCCESS) {
                            LocalCacheUtils.putCacheTableList(response.getData().getList());
                            getView().updateTablesStatusOk(response.getData().getList());
                        } else {
                            getView().showMessage(response.getMsg());
                        }
                    }
                });
    }
}
