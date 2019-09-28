package com.tdlbs.waiterordering.mvp.bean.entity;

import com.blankj.utilcode.util.EncryptUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tdlbs.waiterordering.constant.AppConstants;

import java.lang.reflect.Modifier;
import java.util.List;

import lombok.Getter;

/**
 * ================================================
 * 商品打折参数
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-16 16:22
 * ================================================
 */
@Getter
public class DiscountProductParam extends BaseRequest {
    private String orderNo;
    private List<String> detailUuidList;
    private int discount;

    public DiscountProductParam(String orderNo, List<String> detailUuidList, int discount) {
        this.orderNo = orderNo;
        this.detailUuidList = detailUuidList;
        this.discount = discount;
        this.timestamp = System.currentTimeMillis();
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
