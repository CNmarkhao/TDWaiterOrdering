package com.tdlbs.waiterordering.mvp.bean.entity;

/**
 * ================================================
 * 请求参数基类
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-06 10:02
 * ================================================
 */
public abstract class BaseRequest {
    protected final transient String EMPTY_JSON = "{}";
    protected long timestamp;
    protected String sn;

    protected abstract void createSN();
}
