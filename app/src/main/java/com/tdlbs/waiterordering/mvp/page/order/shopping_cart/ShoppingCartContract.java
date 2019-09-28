package com.tdlbs.waiterordering.mvp.page.order.shopping_cart;

import com.tdlbs.core.ui.mvp.BaseContract;
import com.tdlbs.core.ui.mvp.BaseModel;
import com.tdlbs.waiterordering.mvp.bean.entity.UpdateOrderParam;
import com.tdlbs.waiterordering.mvp.bean.model.OrderDetail;

/**
 * ================================================
 * ShoppingCartContract
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-06-11 20:37
 * ================================================
 */
public interface ShoppingCartContract {
    interface Model extends BaseModel {
        void networkChangeOrder(UpdateOrderParam param);
    }

    interface View extends BaseContract.BaseView {
        void updateOrderOk();
    }

}
