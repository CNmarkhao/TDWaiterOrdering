package com.tdlbs.waiterordering.mvp.bean.model;

import java.io.Serializable;

import lombok.Getter;

/**
 * ================================================
 * 登录信息
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-06 11:37
 * ================================================
 */
@Getter
public class LoginInfoBean implements Serializable {
    private ShopBean shop;
    private CashDevice cashDevice;
    private UserBean user;

    @Getter
    public static class FloorAdmin implements Serializable {
        private String phone;
        private String name;
    }

    @Getter
    public static class ShopBean implements Serializable {
        private long id;
        private long storeId;
        private long shopCategoryId;
        private String shopCode;
        private String outShopCode;
        private String name;
        private String picPath;
        private String shopToken;
        private String contactUser;
        private String expireTime;
        private int coopType;
        private FloorAdmin floorAdmin;
    }

    @Getter
    public static class UserBean implements Serializable {
        private long id;
        private String loginNo;
        private String billCodePrefix;
        private String cashierExtNo;
        private String expireTime;
        private String mobile;
        private String name;
        private String userToken;
    }

    @Getter
    public static class CashDevice implements Serializable {
        private long id;
        private long shopId;
        private long storeId;
        private String deviceNo;
        private String name;
        private String macAddr;
        private String outDeviceNo;
    }
}
