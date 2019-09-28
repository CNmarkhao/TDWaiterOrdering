package com.tdlbs.waiterordering.app.utils;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.StringUtils;
import com.tdlbs.waiterordering.BuildConfig;
import com.tdlbs.waiterordering.constant.CacheConstants;
import com.tdlbs.waiterordering.mvp.bean.model.LoginInfoBean;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ================================================
 * 创建时间：2019/8/8 10:21
 * 作者：markgu
 * 描述：RequestUtils
 * Email：<a href="mailto:guhao561@gmail.com">Contact me</a>
 * ================================================
 */
public class RequestUtils {

    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "").toLowerCase();
    }

    public static String getMac() {
        String mac = DeviceUtils.getMacAddress();
        if (StringUtils.isEmpty(mac)) {
            mac = "02:00:00:00:00:00";
        }
        return BuildConfig.DEBUG ? "0c2576405520" :
                mac.replace(":", "")
                        .toLowerCase();
    }

    /**
     * Token过期或者无信息是返回空字符
     */
    public static String getShopToken() {
        LoginInfoBean bean = (LoginInfoBean) CacheDiskUtils.getInstance().getSerializable(CacheConstants.Login.LOGIN_INFO_BEAN);
        if (bean == null) {
            return "";
        }

        return bean.getShop().getShopToken();
    }

    /**
     * Token过期或者无信息是返回空字符
     */
    public static String getUserToken() {
        LoginInfoBean bean = (LoginInfoBean) CacheDiskUtils.getInstance().getSerializable(CacheConstants.Login.LOGIN_INFO_BEAN);
        if (bean == null) {
            return "";
        }
        return bean.getUser().getUserToken();
    }

    public static String getFloorAdminPhone() {
        LoginInfoBean bean = (LoginInfoBean) CacheDiskUtils.getInstance().getSerializable(CacheConstants.Login.LOGIN_INFO_BEAN);
        if (bean == null || bean.getShop() == null || bean.getShop().getFloorAdmin() == null) {
            return "";
        }
        return bean.getShop().getFloorAdmin().getPhone();
    }

    public static String getStaffName() {
        if (getStaffInfo() == null) {
            return "";
        }
        if (getStaffInfo().getUser() == null) {
            return "";
        }
        return getStaffInfo().getUser().getName();
    }

    public static long getStaffId() {
        if (getStaffInfo() == null) {
            return 0;
        }
        if (getStaffInfo().getUser() == null) {
            return 0;
        }
        return getStaffInfo().getUser().getId();
    }

    public static long getRestId() {
        if (getStaffInfo() == null) {
            return 0;
        }
        if (getStaffInfo().getShop() == null) {
            return 0;
        }
        return getStaffInfo().getShop().getId();
    }

    public static String getStaffOderSeqPrefix() {
        if (getStaffInfo() == null) {
            return "";
        }
        return getStaffInfo().getUser().getBillCodePrefix();
    }

    public static LoginInfoBean getStaffInfo() {
        LoginInfoBean bean = (LoginInfoBean) CacheDiskUtils.getInstance().getSerializable(CacheConstants.Login.LOGIN_INFO_BEAN);
        return bean;
    }

    public static LoginInfoBean.ShopBean getShopInfo() {
        LoginInfoBean bean = (LoginInfoBean) CacheDiskUtils.getInstance().getSerializable(CacheConstants.Login.LOGIN_INFO_BEAN);
        if (bean == null) {
            return new LoginInfoBean.ShopBean();
        }

        return bean.getShop();
    }

    public static String getSafeShopName() {
        String name = getShopInfo().getName();

        String regs = "([^\\u4e00-\\u9fa5\\w\\(\\)（）])+?";
        Pattern pattern = Pattern.compile(regs);
        Matcher matcher = pattern.matcher(name);
        name = matcher.replaceAll("");

        return name;
    }


    public static String getMerchantId() {
        return getShopInfo().getOutShopCode();
    }

    public static String getTerminalId() {
        return getStaffInfo().getCashDevice().getOutDeviceNo();
    }

    public static String getDeviceId() {
        return getStaffInfo().getCashDevice().getDeviceNo();
    }
}
