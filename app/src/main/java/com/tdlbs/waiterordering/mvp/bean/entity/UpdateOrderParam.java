package com.tdlbs.waiterordering.mvp.bean.entity;

import com.blankj.utilcode.util.EncryptUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tdlbs.waiterordering.constant.AppConstants;
import com.tdlbs.waiterordering.mvp.bean.model.OrderDetail;

import java.lang.reflect.Modifier;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * ================================================
 * 更新订单参数
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-16 11:53
 * ================================================
 */
@Getter
@Setter
public class UpdateOrderParam extends BaseRequest {
    private String buyerMobile;
    private String orderNo;
    private int version;
    private int isChangedTable;
    private int peopleNum;
    private int orderStatus;
    private int isWaiting;
    private long tableId;
    private List<OrderDetail.Product> dishList;

    public void autoSign() {
        timestamp = System.currentTimeMillis();
        createSN();
    }

    @Override
    protected void createSN() {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.PROTECTED)
                .create();
        String json = gson.toJson(this);
        json += timestamp + AppConstants.Request.ACCESS_KEY;
        this.sn = EncryptUtils.encryptMD5ToString(json);
        gson = null;
    }
}
