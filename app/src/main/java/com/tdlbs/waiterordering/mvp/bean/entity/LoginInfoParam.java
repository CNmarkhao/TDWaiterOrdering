package com.tdlbs.waiterordering.mvp.bean.entity;

import com.blankj.utilcode.util.EncryptUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tdlbs.waiterordering.constant.AppConstants;

import java.lang.reflect.Modifier;

import lombok.Data;
import lombok.Getter;

/**
 * ================================================
 * 登录参数
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-06 09:46
 * ================================================
 */
@Getter
public class LoginInfoParam extends BaseRequest{
    private String username;
    private String password;
    private String shopCode;

    public LoginInfoParam(String shopCode,String username, String password) {
        this.username = username;
        this.password = password;
        this.shopCode = shopCode;
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
