package com.tdlbs.waiterordering.app;

import com.tdlbs.waiterordering.mvp.bean.entity.CreateOrderParam;
import com.tdlbs.waiterordering.mvp.bean.entity.DiscountProductParam;
import com.tdlbs.waiterordering.mvp.bean.entity.EmptyParam;
import com.tdlbs.waiterordering.mvp.bean.entity.GetOrderDetailParam;
import com.tdlbs.waiterordering.mvp.bean.entity.LoginInfoParam;
import com.tdlbs.waiterordering.mvp.bean.entity.UpdateOrderParam;
import com.tdlbs.waiterordering.mvp.bean.model.BaseResponse;
import com.tdlbs.waiterordering.mvp.bean.model.LoginInfoBean;
import com.tdlbs.waiterordering.mvp.bean.model.OrderDetail;
import com.tdlbs.waiterordering.mvp.bean.model.ProductMemoList;
import com.tdlbs.waiterordering.mvp.bean.model.ShopDataPackage;
import com.tdlbs.waiterordering.mvp.bean.model.TableList;
import com.tdlbs.waiterordering.mvp.bean.model.TerminalConfigs;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * ================================================
 * MISApis
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-06 10:10
 * ================================================
 */
public interface MISApis {
    String HEADER_CONTENT_TYPE = "content-type:application/json;charset=UTF-8";

    /**
     * 用户登录信息
     *
     * @param mac   设备MAC地址
     * @param param 登录参数
     * @return 用户登录信息
     */
    @Headers({HEADER_CONTENT_TYPE})
    @POST("/mis-cashier-service/v1/login")
    Observable<BaseResponse<LoginInfoBean>> loginStaff(@Header("macAddr") String mac,
                                                       @Body LoginInfoParam param);

    /**
     * 同步服务器商户数据包
     *
     * @param mac     设备MAC地址
     * @param header  商户Token
     * @param uHeader 用户Token
     * @param params  空参数
     * @return 服务器离线数据包
     */
    @Headers({HEADER_CONTENT_TYPE})
    @POST("/mis-cashier-service/v1/shop/dataPackage")
    Observable<BaseResponse<ShopDataPackage>> syncRestServerData(@Header("macAddr") String mac,
                                                                 @Header("shopToken") String header,
                                                                 @Header("userToken") String uHeader,
                                                                 @Body EmptyParam params);

    /**
     * 同步终端配置信息
     *
     * @param mac        设备MAC地址
     * @param shopToken  商户Token
     * @param userToken  用户Token
     * @param emptyParam 空参数
     * @return 终端配置信息
     */
    @Headers({HEADER_CONTENT_TYPE})
    @POST("mis-cashier-service/v1/dict/terminal/configs")
    Observable<BaseResponse<TerminalConfigs>> syncTerminalConfigs(@Header("macAddr") String mac,
                                                                  @Header("shopToken") String shopToken,
                                                                  @Header("userToken") String userToken,
                                                                  @Body EmptyParam emptyParam);

    /**
     * 同步全部单商品备注列表
     *
     * @param mac     设备MAC地址
     * @param header  商户Token
     * @param uHeader 用户Token
     * @param params  空参数
     * @return 全部单商品备注列表
     */
    @Headers({HEADER_CONTENT_TYPE})
    @POST("/mis-cashier-service/v1/product/memo/list")
    Observable<BaseResponse<ProductMemoList>> syncProductMemoList(@Header("macAddr") String mac,
                                                                  @Header("shopToken") String header,
                                                                  @Header("userToken") String uHeader,
                                                                  @Body EmptyParam params);

    /**
     * 更新餐桌列表状态信息
     *
     * @param mac     设备MAC地址
     * @param header  商户Token
     * @param uHeader 用户Token
     * @param params  空参数
     * @return 餐桌列表
     */
    @Headers({HEADER_CONTENT_TYPE})
    @POST("/mis-cashier-service/v1/shop/table/list")
    Observable<BaseResponse<TableList>> updateTablesStatus(@Header("macAddr") String mac,
                                                           @Header("shopToken") String header,
                                                           @Header("userToken") String uHeader,
                                                           @Body EmptyParam params);

    /**
     * 发起开桌操作
     *
     * @param mac       设备MAC地址
     * @param shopToken 商户Token
     * @param userToken 用户Token
     * @param param     开桌信息参数
     * @return 订单详情
     */
    @Headers({HEADER_CONTENT_TYPE})
    @POST("mis-cashier-service/v1/table/order/create")
    Observable<BaseResponse<OrderDetail>> createTableOrder(@Header("macAddr") String mac,
                                                           @Header("shopToken") String shopToken,
                                                           @Header("userToken") String userToken,
                                                           @Body CreateOrderParam param);

    /**
     * 获取订单信息
     *
     * @param mac       设备MAC地址
     * @param shopToken 商户Token
     * @param userToken 用户Token
     * @param param     获取订单信息参数
     * @return 订单详情
     */
    @Headers({HEADER_CONTENT_TYPE})
    @POST("/mis-cashier-service/v1/order/get")
    Observable<BaseResponse<OrderDetail>> getOrderDetail(@Header("macAddr") String mac,
                                                         @Header("shopToken") String shopToken,
                                                         @Header("userToken") String userToken,
                                                         @Body GetOrderDetailParam param);

    /**
     * 更新订单详情
     *
     * @param mac       设备MAC地址
     * @param shopToken 商户Token
     * @param userToken 用户Token
     * @param param     更新订单参数
     * @return 订单详情
     */
    @Headers({HEADER_CONTENT_TYPE})
    @POST("/mis-cashier-service/v1/table/order/update")
    Observable<BaseResponse<OrderDetail>> updateOrder(@Header("macAddr") String mac,
                                                      @Header("shopToken") String shopToken,
                                                      @Header("userToken") String userToken,
                                                      @Body UpdateOrderParam param);

    /**
     * 对订单详情中的商品打折
     *
     * @param mac       设备MAC地址
     * @param shopToken 商户Token
     * @param userToken 用户Token
     * @param param     商品打折参数
     * @return 订单详情
     */
    @Headers({HEADER_CONTENT_TYPE})
    @POST("mis-cashier-service/v1/order/product/discount")
    Observable<BaseResponse<OrderDetail>> discountProduct(@Header("macAddr") String mac,
                                                          @Header("shopToken") String shopToken,
                                                          @Header("userToken") String userToken,
                                                          @Body DiscountProductParam param);

}

