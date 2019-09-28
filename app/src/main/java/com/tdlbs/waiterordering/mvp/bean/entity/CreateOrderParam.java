package com.tdlbs.waiterordering.mvp.bean.entity;

import com.blankj.utilcode.util.EncryptUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tdlbs.waiterordering.constant.AppConstants;

import java.lang.reflect.Modifier;

/**
 * ================================================
 * 开单参数
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-15 14:02
 * ================================================
 */
public class CreateOrderParam extends BaseRequest {
    private long tableId;
    private String memo;
    private int isWaiting;
    private int peopleNum;

    public CreateOrderParam(long tableId, int peopleNum) {
        this.tableId = tableId;
        this.peopleNum = peopleNum;
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
