package com.tdlbs.waiterordering.constant;
/*
 * Copyright (c) 2019 Nan Jing Tao Dian <TDLBS>. All rights reserved.
 */

/**
 * ================================================
 * 缓存常量类
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-06-10 08:59
 * ================================================
 */
public interface CacheConstants {
    interface Login {
        String LOGIN_INFO_BEAN = "LOGIN_INFO_BEAN";
    }

    interface DataPackage {
        String DISH_CONFIG = "DISH_CONFIG";
        String DISH_GROUP_CONFIG = "DISH_GROUP_CONFIG";
        String TYPE_CONFIG = "TYPE_CONFIG";
        String PRINTER_CONFIG = "PRINTER_CONFIG";
        String TABLE_CONFIG = "TABLE_CONFIG";
        String COMMON_MEMO_CONFIG = "COMMON_MEMO_CONFIG";
        String PRODUCT_MEMO_CONFIG = "PRODUCT_MEMO_CONFIG";
        String TERMINAL_CONFIGS = "TERMINAL_CONFIGS";
    }
}
