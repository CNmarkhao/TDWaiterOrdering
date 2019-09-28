package com.tdlbs.waiterordering.mvp.bean.model;

import com.tdlbs.waiterordering.constant.AppConstants;

import lombok.Getter;
import lombok.Setter;

/**
 * ================================================
 * 请求结果基类
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-06 11:38
 * ================================================
 */

@Getter
@Setter
public class BaseResponse<T> {
    private T data;
    private int code;
    private String msg;


    /**
     * 请求是否成功
     *
     * @return
     */
    public boolean isSuccess() {
        if (code == AppConstants.Request.REQUEST_SUCCESS) {
            return true;
        } else {
            return false;
        }
    }
}
