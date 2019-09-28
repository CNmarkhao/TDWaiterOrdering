package com.tdlbs.core.module.network.retrofit;


import com.tdlbs.core.constant.Constant;

public class CommonEmptyDataException extends RuntimeException {
    //错误码
    private int errorCode = Constant.ERROR_CODE_EMPTYDATA;
    //错误消息
    private String errorMsg;


    public CommonEmptyDataException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
