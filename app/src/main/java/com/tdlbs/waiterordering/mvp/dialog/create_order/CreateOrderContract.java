package com.tdlbs.waiterordering.mvp.dialog.create_order;

import com.tdlbs.core.ui.mvp.BaseContract;
import com.tdlbs.core.ui.mvp.BaseModel;
import com.tdlbs.waiterordering.mvp.bean.entity.CreateOrderParam;
import com.tdlbs.waiterordering.mvp.bean.model.OrderDetail;

/**
 * ================================================
 * CreateOrderContract
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-06-11 20:37
 * ================================================
 */
public interface CreateOrderContract {
    interface Model extends BaseModel {
        void networkCreateOrder(CreateOrderParam param);
    }

    interface View extends BaseContract.BaseView {
        void createOrderOk(OrderDetail param);
    }
}
