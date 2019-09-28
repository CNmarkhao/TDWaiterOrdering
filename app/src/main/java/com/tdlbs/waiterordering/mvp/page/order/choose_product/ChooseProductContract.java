package com.tdlbs.waiterordering.mvp.page.order.choose_product;

import com.tdlbs.core.ui.mvp.BaseContract;
import com.tdlbs.core.ui.mvp.BaseModel;
import com.tdlbs.waiterordering.mvp.bean.entity.GetOrderDetailParam;
import com.tdlbs.waiterordering.mvp.bean.entity.LoginInfoParam;
import com.tdlbs.waiterordering.mvp.bean.model.LoginInfoBean;
import com.tdlbs.waiterordering.mvp.bean.model.OrderDetail;

/**
 * ================================================
 * ChooseProductContract
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-06-11 20:37
 * ================================================
 */
public interface ChooseProductContract {
    interface Model extends BaseModel {
        void networkGetOrderDetail(GetOrderDetailParam param);
    }

    interface View extends BaseContract.BaseView {
        void getOrderDetailError(GetOrderDetailParam param, String reason);
        void getOrderDetailOk(OrderDetail detail);
    }

}
