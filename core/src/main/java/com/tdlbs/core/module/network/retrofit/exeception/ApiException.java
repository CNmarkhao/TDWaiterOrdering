package com.tdlbs.core.module.network.retrofit.exeception;


public class ApiException extends Exception {

    public int code;
    public String message;

    public ApiException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }
}
