package com.tdlbs.waiterordering.mvp.page.order.table_manager;

import com.tdlbs.core.ui.mvp.BaseContract;
import com.tdlbs.core.ui.mvp.BaseModel;
import com.tdlbs.waiterordering.mvp.bean.model.ShopDataPackage;

import java.util.List;

/**
 * ================================================
 * TableManagerContract
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-06-11 20:37
 * ================================================
 */
public interface TableManagerContract {
    interface Model extends BaseModel {
        void networkUpdateTablesStatus(boolean isAuto);
    }

    interface View extends BaseContract.BaseView {
        void updateTablesStatusOk(List<ShopDataPackage.TableListBean> tableList);
    }

}
