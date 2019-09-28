package com.tdlbs.waiterordering.mvp.bean.entity;

import com.blankj.utilcode.util.EncryptUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tdlbs.waiterordering.constant.AppConstants;

import java.lang.reflect.Modifier;

/**
 * ================================================
 * 获取订单详情参数
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-15 18:09
 * ================================================
 */
public class GetOrderDetailParam extends BaseRequest {
    private String orderNo;

    public GetOrderDetailParam(String orderNo) {
        this.orderNo = orderNo;
        this.timestamp = System.currentTimeMillis();
        createSN();
    }

    @Override
    protected void createSN() {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.PROTECTED)
                .create();
        String json = gson.toJson(this);
        json += timestamp +  AppConstants.Request.ACCESS_KEY;
        this.sn = EncryptUtils.encryptMD5ToString(json);
        gson = null;
    }
}
