package com.tdlbs.waiterordering.mvp.bean.entity;

import com.blankj.utilcode.util.EncryptUtils;
import com.tdlbs.waiterordering.constant.AppConstants;

/**
 * ================================================
 * 空参数
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-09 09:31
 * ================================================
 */
public class EmptyParam extends BaseRequest {

    public EmptyParam() {
        timestamp = System.currentTimeMillis();
        createSN();
    }

    @Override
    protected void createSN() {
        String json = EMPTY_JSON;
        json += timestamp + AppConstants.Request.ACCESS_KEY;
        this.sn = EncryptUtils.encryptMD5ToString(json);
    }
}
