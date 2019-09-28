package com.tdlbs.waiterordering.mvp.page.loading_data;

import com.tdlbs.core.ui.mvp.BaseContract;
import com.tdlbs.core.ui.mvp.BaseModel;
import com.tdlbs.waiterordering.mvp.bean.model.ProgressMessage;
import com.tdlbs.waiterordering.mvp.bean.model.ShopDataPackage;

/**
 * ================================================
 * LoadingDataContract
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-06-11 20:37
 * ================================================
 */
public interface LoadingDataContract {

    interface Model extends BaseModel {
        void networkSyncShopData();

        void networkSyncProductMemoList();

        void networkSyncTerminalConfigs();

        void saveLocalShopDataPackage(ShopDataPackage dataPackage);
    }

    interface View extends BaseContract.BaseView {
        /**
         * 展示更新进度
         *
         * @param msg 进度信息
         */
        void updateProgress(ProgressMessage msg);

        /**
         * 更新出错
         *
         * @param msg 错误信息
         */
        void loadDataError(String msg);
    }

}
