package com.tdlbs.core.module.network.retrofit.exeception;


public class ServerException extends RuntimeException{
    // 异常处理，为速度，不必要设置getter和setter
    public int code;
    public String message;

    public ServerException( int code,String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
