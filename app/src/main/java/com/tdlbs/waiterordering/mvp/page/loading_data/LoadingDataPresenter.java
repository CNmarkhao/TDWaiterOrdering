package com.tdlbs.waiterordering.mvp.page.loading_data;

import android.content.Context;

import com.google.gson.Gson;
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
import com.tdlbs.waiterordering.mvp.bean.model.ProductMemoList;
import com.tdlbs.waiterordering.mvp.bean.model.ProgressMessage;
import com.tdlbs.waiterordering.mvp.bean.model.ShopDataPackage;
import com.tdlbs.waiterordering.mvp.bean.model.TerminalConfigs;

import javax.inject.Inject;

/**
 * ================================================
 * LoadingDataPresenter
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-06-11 20:37
 * ================================================
 */
public class LoadingDataPresenter extends BasePresenter<LoadingDataContract.View> implements LoadingDataContract.Model {

    private RetrofitManager mRetrofitManager;
    private Context mContext;
    private Gson mGson;

    @Inject
    public LoadingDataPresenter(Context mContext, RetrofitManager mRetrofitManager) {
        this.mContext = mContext;
        this.mRetrofitManager = mRetrofitManager;
        this.mGson = new Gson();
    }

    @Override
    public void networkSyncShopData() {
        mRetrofitManager.createApi(MISApis.class).syncRestServerData(RequestUtils.getMac(),
                RequestUtils.getShopToken(), RequestUtils.getUserToken(), new EmptyParam())
                .compose(RxUtils.applySchedulers(getView(), true))
                .subscribe(new BaseObserver<BaseResponse<ShopDataPackage>>() {
                    @Override
                    public void onError(ApiException exception) {
                        getView().loadDataError(exception.message);
                    }

                    @Override
                    public void onSuccess(BaseResponse<ShopDataPackage> response) {
                        if (response.getCode() == AppConstants.Request.REQUEST_SUCCESS) {
                            saveLocalShopDataPackage(response.getData());
                        } else {
                            getView().loadDataError(response.getMsg());
                        }
                    }
                });
    }

    @Override
    public void networkSyncProductMemoList() {
        mRetrofitManager.createApi(MISApis.class).syncProductMemoList(RequestUtils.getMac(),
                RequestUtils.getShopToken(), RequestUtils.getUserToken(), new EmptyParam())
                .compose(RxUtils.applySchedulers(getView(), true))
                .subscribe(new BaseObserver<BaseResponse<ProductMemoList>>() {
                    @Override
                    public void onError(ApiException exception) {
                        getView().loadDataError(exception.message);
                    }

                    @Override
                    public void onSuccess(BaseResponse<ProductMemoList> response) {
                        if (response.getCode() == AppConstants.Request.REQUEST_SUCCESS) {
                            LocalCacheUtils.putCacheProductMemoList(response.getData());
                            getView().updateProgress(new ProgressMessage(100, "加载服务器数据(5/5):同步完成"));
                        } else {
                            getView().loadDataError(response.getMsg());
                        }
                    }
                });
    }

    @Override
    public void networkSyncTerminalConfigs() {
        mRetrofitManager.createApi(MISApis.class).syncTerminalConfigs(RequestUtils.getMac(),
                RequestUtils.getShopToken(), RequestUtils.getUserToken(), new EmptyParam())
                .compose(RxUtils.applySchedulers(getView(), true))
                .subscribe(new BaseObserver<BaseResponse<TerminalConfigs>>() {
                    @Override
                    public void onError(ApiException exception) {
                        networkSyncProductMemoList();
                    }

                    @Override
                    public void onSuccess(BaseResponse<TerminalConfigs> response) {
                        if (response.getCode() == AppConstants.Request.REQUEST_SUCCESS) {
                            LocalCacheUtils.putCacheTerminalConfigs(response.getData());
                        }
                        networkSyncProductMemoList();
                    }
                });
    }

    @Override
    public void saveLocalShopDataPackage(ShopDataPackage dataPackage) {
        getView().updateProgress(new ProgressMessage(15, "加载服务器数据(0/5):同步菜品信息..."));
        LocalCacheUtils.putCacheProductList(dataPackage.getProductList());
        LocalCacheUtils.putCacheProductGroupList(dataPackage.getProductGroupList());

        getView().updateProgress(new ProgressMessage(35, "加载服务器数据(1/5):同步菜品分类信息..."));
        LocalCacheUtils.putCacheTypeList(dataPackage.getProductCategoryList());
        LocalCacheUtils.putCachePrinterList(dataPackage.getPrinterList());

        getView().updateProgress(new ProgressMessage(60, "加载服务器数据(2/5):同步餐桌信息..."));
        LocalCacheUtils.putCacheTableList(dataPackage.getTableList());

        getView().updateProgress(new ProgressMessage(80, "加载服务器数据(3/5):同步通用备注信息..."));
        LocalCacheUtils.putCacheCommonMemoList(dataPackage.getMemoList());

        getView().updateProgress(new ProgressMessage(90, "加载服务器数据(4/5):同步通用备注信息..."));
        networkSyncTerminalConfigs();
    }

}
