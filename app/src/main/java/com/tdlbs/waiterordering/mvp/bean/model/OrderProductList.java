package com.tdlbs.waiterordering.mvp.bean.model;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;

/**
 * ================================================
 * OrderProductList
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-20 15:46
 * ================================================
 */
@Getter
public class OrderProductList implements Serializable {
    List<OrderDetail.Product> mProductList;

    public OrderProductList(List<OrderDetail.Product> mProductList) {
        this.mProductList = mProductList;
    }
}
