package com.tdlbs.waiterordering.constant;
/*
 * Copyright (c) 2019 Nan Jing Tao Dian <TDLBS>. All rights reserved.
 */

/**
 * ================================================
 * APP常量类
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-06-10 09:00
 * ================================================
 */
public interface AppConstants {

    interface FilePath {
        String APP_LOG_PATH = "/TDWaiterOrdering/Log/";
        String APP_CRASH_PATH = "/TDWaiterOrdering/Crash/";
    }

    interface NotifyProduct {
        int URGE = 0x19900;
        int PUSH = 0x19901;
        int WAIT = 0x19902;
    }

    interface BundleCode {
        String ORDER_NO = "order_no";
        String TABLE_NAME = "table_name";
        String TABLE_TITLE = "table_title";
        String ORDER_DETAIL = "order_detail";
        String PRODUCT_LIST = "product_list";
    }

    interface Request {
        int REQUEST_SUCCESS = 0;
        String ACCESS_KEY = "sadfasd9xzwang";
    }
}
