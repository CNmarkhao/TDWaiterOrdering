package com.tdlbs.waiterordering.mvp.page.order.table_console;

import com.tdlbs.core.ui.mvp.BaseContract;
import com.tdlbs.core.ui.mvp.BaseModel;
import com.tdlbs.waiterordering.mvp.bean.entity.DiscountProductParam;
import com.tdlbs.waiterordering.mvp.bean.entity.GetOrderDetailParam;
import com.tdlbs.waiterordering.mvp.bean.entity.UpdateOrderParam;
import com.tdlbs.waiterordering.mvp.bean.model.OrderDetail;

/**
 * ================================================
 * TableConsoleContract
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-06-11 20:37
 * ================================================
 */
public interface TableConsoleContract {
    interface Model extends BaseModel {
        void networkGetOrderDetail(GetOrderDetailParam param);

        void networkUpdateOrder(UpdateOrderParam param);

        void networkDiscountProduct(DiscountProductParam param);
    }

    interface View extends BaseContract.BaseView {
        void updateOrderOk(OrderDetail detail, boolean isChangeTable);

        void discountProduct(OrderDetail detail, boolean isFree);

        void getOrderDetailOk(OrderDetail detail);

        void getOrderDetailError(GetOrderDetailParam param, String reason);
    }

}
