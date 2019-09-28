package com.tdlbs.waiterordering.mvp.bean.model;

import lombok.Getter;

/**
 * ================================================
 * TableTypeParam
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-14 14:55
 * ================================================
 */
@Getter
public class TableTypeParam {
    public final static int TYPE_ALL = 0;
    public final static int TYPE_CLOSE = 1;
    public final static int TYPE_OPEN = 2;
    private int tableType;
    private String name;

    public TableTypeParam(int tableType, String name) {
        this.tableType = tableType;
        this.name = name;
    }

}
