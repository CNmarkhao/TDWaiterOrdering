package com.tdlbs.waiterordering.mvp.bean.model;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;

/**
 * ================================================
 * 单菜品备注列表
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-09 09:24
 * ================================================
 */
@Getter
public class ProductMemoList implements Serializable {
    private List<ProductMemo> list;

    @Getter
    public static class ProductMemo implements Serializable {
        String memos;
        String name;
        long productId;
    }
}
