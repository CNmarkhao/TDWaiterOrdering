package com.tdlbs.waiterordering.mvp.bean.model;

import lombok.Getter;

/**
 * ================================================
 * PrintCommand
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-22 14:29
 * ================================================
 */
@Getter
public class PrintCommand {
    private OrderDetail.Product product;
    private int printType;
    private String tableName;

    public PrintCommand(OrderDetail.Product oProduct,String tableName, int printType) {
        this.product = oProduct;
        this.printType = printType;
        this.tableName = tableName;
    }

}
