package com.tdlbs.waiterordering.mvp.bean.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.tdlbs.waiterordering.mvp.adapter.TableExpandableItemAdapter;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * ================================================
 * ShopDataPackage
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-09 09:20
 * ================================================
 */
@Getter
public class ShopDataPackage implements Serializable {
    private List<MemoListBean> memoList;
    private List<TableListBean> tableList;
    private List<ProductCategoryListBean> productCategoryList;
    private List<ProductListBean> productList;
    private List<PrinterListBean> printerList;
    private List<ProductGroupList> productGroupList;

    @Getter
    public static class PrinterListBean implements Serializable {
        /**
         * memo : 去冰
         * id : 6
         */
        private int printIndex;
        private int linkType;
        private String ipAddr;
        private int port;
        private long id;
    }


    @Getter
    public static class MemoListBean implements Serializable {
        /**
         * memo : 去冰
         * id : 6
         */
        private String name;
        private long id;
    }

    @Getter
    public static class TableListBean implements Serializable, MultiItemEntity {
        public static final int TABLE_TYPE_NORMAL = 1;
        public static final int TABLE_TYPE_ROOM = 0;

        public static final int ORDER_STATUS_EMPTY = 0;
        public static final int ORDER_STATUS_CREATE = 1;
        public static final int ORDER_STATUS_WAIT_CONFIRM = 2;
        public static final int ORDER_STATUS_WAIT_BILL = 3;
        public static final int ORDER_STATUS_BILLING = 4;

        /**
         * "id": 1187,
         * "name": "扫码点餐",
         * "createTime": "2019-08-09 10:56:07",
         * "shopId": 900000077,
         * "tableType": 0,
         * "tableStatus": 0,
         * "currentOrder": "-1",
         * "sort": 1,
         * "version": 0,
         * "peopleNum": 0,
         * "totalPrice": 0,
         * "orderStatus": 0,
         */
        private long id;
        private String name;
        private String createTime;
        private int tableType;
        private String currentOrder;
        private int sort;
        private int peopleNum;
        private double totalPrice;
        private int orderStatus;


        @Override
        public int getItemType() {
            return TableExpandableItemAdapter.TYPE_TABLE_DETAIL;
        }
    }

    @Getter
    @Setter
    public static class ProductCategoryListBean implements Serializable {
        /**
         * "id": 700062,
         * "name": "衣服",
         * "sort": 11,
         * "level": 11,
         * "discount": 100,
         * "storeId": 1
         */
        private long id;
        private String name;
        private int sort;
        private int discount;
        private int localCount;
    }

    @Getter
    @Setter
    public static class ProductListBean implements Serializable {
        private long id;
        private long productCategoryId;
        private String name;
        private String acronym;
        private String picPath;
        private double vipPrice;
        private double price;
        private int discount;
        private int sort;
        private String unitName;
        private int localCount;
        private int groupNum;
        private int groupFlag;
    }

    @Getter
    @Setter
    public static class ProductGroupList implements Serializable {
        private long id;
        private long productCategoryId;
        private String name;
        private String acronym;
        private String picPath;
        private double vipPrice;
        private double price;
        private int discount;
        private int sort;
        private String unitName;
        private List<ProductListBean> groupProductList;

    }
}
