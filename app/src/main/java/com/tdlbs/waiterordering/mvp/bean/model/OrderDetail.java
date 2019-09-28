package com.tdlbs.waiterordering.mvp.bean.model;

import com.blankj.utilcode.util.EncryptUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.tdlbs.waiterordering.constant.AppConstants;
import com.tdlbs.waiterordering.mvp.bean.entity.BaseRequest;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * ================================================
 * OrderDetail
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-15 15:22
 * ================================================
 */
@Setter
@Getter
public class OrderDetail extends BaseRequest implements Serializable {
    private String orderSeq;
    private int version;
    private String orderNo;
    private int status;
    private int printStatus;
    private double totalFee;
    private double additionFee;
    private double productFee;
    private double payableFee;
    private String createTime;
    private String finishTime;
    private String buyerMobile;
    private int waitFlag;
    private String memo;
    private int peopleNum;
    private double userPaidFee;
    private int orderFlag;
    private long tableId;
    private long createCashierId;
    private long receiptCashierId;
    private List<Product> productList;
    private List<Payment> paymentList;

    public void autoSign() {
        timestamp = System.currentTimeMillis();
        createSN();
    }

    @Override
    protected void createSN() {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.PROTECTED)
                .create();
        String json = gson.toJson(this);
        json += timestamp + AppConstants.Request.ACCESS_KEY;
        this.sn = EncryptUtils.encryptMD5ToString(json);
        gson = null;
    }

    @Setter
    @Getter
    public static class Product implements Serializable {
        @Expose(serialize = false)
        private int vipProm;
        private long orderId;
        private long printerId;
        private long productId;
        private long productCategoryId;
        private String productName;
        private int productCount;
        private double originalPrice;
        private int discount;
        private double currentPrice;
        private int status;
        private String detailUuid;
        private String memo;
        private String unit;
    }

    @Getter
    public static class Payment implements Serializable {
        private long id;
        private int payType;
        private String payName;
        private double orderId;
        private double payFee;
        private String tradeNo;
        private int refundStatus;
        private String payTime;
        private int discount;
        private int promotionId;
        private String promotionName;
        private double useCount;
        private String payUuid;
        private String uniqueCode;
        private String cardTailNum;
    }
}
