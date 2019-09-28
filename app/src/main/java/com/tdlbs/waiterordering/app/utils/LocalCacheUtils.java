package com.tdlbs.waiterordering.app.utils;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tdlbs.waiterordering.constant.CacheConstants;
import com.tdlbs.waiterordering.mvp.bean.model.ProductMemoList;
import com.tdlbs.waiterordering.mvp.bean.model.ShopDataPackage;
import com.tdlbs.waiterordering.mvp.bean.model.TerminalConfigs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ================================================
 * 本地Cache缓存管理类
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-14 16:32
 * ================================================
 */
public class LocalCacheUtils {
    private final static String EMPTY_ARRAY = "[]";
    private final static String EMPTY_JSON = "{}";

    private static Gson sGson = new Gson();

    /**
     * 缓存服务器中商品通用备注
     *
     * @param beans 备注列表
     */
    public static void putCacheCommonMemoList(List<ShopDataPackage.MemoListBean> beans) {
        CacheDiskUtils.getInstance().put(CacheConstants.DataPackage.COMMON_MEMO_CONFIG, sGson.toJson(beans));
    }

    /**
     * 缓存服务器中单商品备注
     *
     * @param beans 单商品备注列表
     */
    public static void putCacheProductMemoList(ProductMemoList beans) {
        CacheDiskUtils.getInstance().put(CacheConstants.DataPackage.PRODUCT_MEMO_CONFIG, sGson.toJson(beans.getList()));
    }

    /**
     * 缓存服务器中终端配置
     *
     * @param beans 终端配置
     */
    public static void putCacheTerminalConfigs(TerminalConfigs beans) {
        CacheDiskUtils.getInstance().put(CacheConstants.DataPackage.TERMINAL_CONFIGS, sGson.toJson(beans));
    }

    /**
     * 缓存服务器中餐桌列表
     *
     * @param beans 餐桌列表
     */
    public static void putCacheTableList(List<ShopDataPackage.TableListBean> beans) {
        Collections.sort(beans, (o1, o2) -> o1.getSort() - o2.getSort());
        CacheDiskUtils.getInstance().put(CacheConstants.DataPackage.TABLE_CONFIG, sGson.toJson(beans));
    }

    /**
     * 缓存服务器中商品分类列表
     *
     * @param beans 商品分类列表
     */
    public static void putCacheTypeList(List<ShopDataPackage.ProductCategoryListBean> beans) {
        Collections.sort(beans, (o1, o2) -> o1.getSort() - o2.getSort());
        CacheDiskUtils.getInstance().put(CacheConstants.DataPackage.TYPE_CONFIG, sGson.toJson(beans));
    }

    /**
     * 缓存服务器中商品列表
     *
     * @param beans 商品列表
     */
    public static void putCacheProductList(List<ShopDataPackage.ProductListBean> beans) {
        Collections.sort(beans, (o1, o2) -> o1.getSort() - o2.getSort());
        CacheDiskUtils.getInstance().put(CacheConstants.DataPackage.DISH_CONFIG, sGson.toJson(beans));
    }

    /**
     * 缓存服务器中打印机信息列表
     *
     * @param beans 打印机信息列表
     */
    public static void putCachePrinterList(List<ShopDataPackage.PrinterListBean> beans) {
        CacheDiskUtils.getInstance().put(CacheConstants.DataPackage.PRINTER_CONFIG, sGson.toJson(beans));
    }

    /**
     * 缓存服务器中商品套餐列表
     *
     * @param beans 商品套餐列表
     */
    public static void putCacheProductGroupList(List<ShopDataPackage.ProductGroupList> beans) {
        Collections.sort(beans, (o1, o2) -> o1.getSort() - o2.getSort());
        CacheDiskUtils.getInstance().put(CacheConstants.DataPackage.DISH_GROUP_CONFIG, sGson.toJson(beans));
    }

    /**
     * 获取缓存中的打印机信息列表
     *
     * @return 打印机信息列表
     */
    public static List<ShopDataPackage.PrinterListBean> getCachePrinterList() {
        String dbData = CacheDiskUtils.getInstance().getString(CacheConstants.DataPackage.PRINTER_CONFIG, EMPTY_ARRAY);
        return sGson.fromJson(dbData, new TypeToken<List<ShopDataPackage.PrinterListBean>>() {
        }.getType());
    }

    /**
     * 获取缓存中的商品套餐列表
     *
     * @return 商品套餐列表
     */
    public static List<ShopDataPackage.ProductGroupList> getCacheProductGroupList() {
        String dbData = CacheDiskUtils.getInstance().getString(CacheConstants.DataPackage.DISH_GROUP_CONFIG, EMPTY_ARRAY);
        return sGson.fromJson(dbData, new TypeToken<List<ShopDataPackage.ProductGroupList>>() {
        }.getType());
    }

    /**
     * 获取缓存中的餐桌列表
     *
     * @return 餐桌列表
     */
    public static List<ShopDataPackage.TableListBean> getCacheTableList() {
        String dbData = CacheDiskUtils.getInstance().getString(CacheConstants.DataPackage.TABLE_CONFIG, EMPTY_ARRAY);
        return sGson.fromJson(dbData, new TypeToken<List<ShopDataPackage.TableListBean>>() {
        }.getType());
    }

    /**
     * 获取缓存中的商品分类列表
     *
     * @return 商品分类列表
     */
    public static List<ShopDataPackage.ProductCategoryListBean> getCacheTypeList() {
        String dbData = CacheDiskUtils.getInstance().getString(CacheConstants.DataPackage.TYPE_CONFIG, EMPTY_ARRAY);
        return sGson.fromJson(dbData, new TypeToken<List<ShopDataPackage.ProductCategoryListBean>>() {
        }.getType());
    }

    /**
     * 获取缓存中的商品列表
     *
     * @return 商品列表
     */
    public static List<ShopDataPackage.ProductListBean> getCacheProductList() {
        String dbData = CacheDiskUtils.getInstance().getString(CacheConstants.DataPackage.DISH_CONFIG, EMPTY_ARRAY);
        return sGson.fromJson(dbData, new TypeToken<List<ShopDataPackage.ProductListBean>>() {
        }.getType());
    }

    /**
     * 获取缓存中的商品通用备注列表
     *
     * @return 商品通用备注列表
     */
    public static List<ShopDataPackage.MemoListBean> getCacheCommonMemoList() {
        String dbData = CacheDiskUtils.getInstance().getString(CacheConstants.DataPackage.COMMON_MEMO_CONFIG, EMPTY_ARRAY);
        return sGson.fromJson(dbData, new TypeToken<List<ShopDataPackage.MemoListBean>>() {
        }.getType());
    }

    /**
     * 获取缓存中的单商品备注列表
     *
     * @return 单商品备注列表
     */
    public static List<ProductMemoList.ProductMemo> getCacheProductMemoList() {
        String dbData = CacheDiskUtils.getInstance().getString(CacheConstants.DataPackage.PRODUCT_MEMO_CONFIG, EMPTY_ARRAY);
        return sGson.fromJson(dbData, new TypeToken<List<ProductMemoList.ProductMemo>>() {
        }.getType());
    }

    /**
     * 获取缓存中的终端配置
     *
     * @return 终端配置
     */
    public static TerminalConfigs getCacheTerminalConfigs() {
        String dbData = CacheDiskUtils.getInstance().getString(CacheConstants.DataPackage.TERMINAL_CONFIGS, EMPTY_JSON);
        return sGson.fromJson(dbData, TerminalConfigs.class);
    }

    /**
     * 通过商品的ID获取该商品的单商品备注列表
     *
     * @return 单商品备注列表
     */
    public static List<ProductMemoList.ProductMemo> getSingleMemo(long productId) {
        List<ProductMemoList.ProductMemo> allData = getCacheProductMemoList();
        List<ProductMemoList.ProductMemo> result = new ArrayList<>();
        for (int i = 0; i < allData.size(); i++) {
            if (allData.get(i).getProductId() == productId) {
                result.add(allData.get(i));
            }
        }
        return result;
    }

    /**
     * 通过商品的ID获取该商品详情
     *
     * @return 商品详情
     */
    public static ShopDataPackage.ProductListBean getProduct(long productId) {
        List<ShopDataPackage.ProductListBean> beans = getCacheProductList();
        for (ShopDataPackage.ProductListBean item : beans) {
            if (item.getId() == productId) {
                return item;
            }
        }

        return null;
    }

    /**
     * 通过打印机的ID获取该打印机详情
     *
     * @return 打印机详情
     */
    public static ShopDataPackage.PrinterListBean getPrinter(long printId) {
        List<ShopDataPackage.PrinterListBean> beans = getCachePrinterList();
        for (ShopDataPackage.PrinterListBean item : beans) {
            if (item.getId() == printId) {
                return item;
            }
        }
        return null;
    }

    /**
     * 通过商品的ID获取该商品包含的套餐商品列表
     *
     * @return 套餐商品列表
     */
    public static List<ShopDataPackage.ProductListBean> getProductGroup(long productId) {
        List<ShopDataPackage.ProductGroupList> beans = getCacheProductGroupList();
        for (ShopDataPackage.ProductGroupList item : beans) {
            if (item.getId() == productId) {
                return item.getGroupProductList();
            }
        }

        return new ArrayList<>();
    }

    /**
     * 获取图片服务器地址
     *
     * @return 图片服务器地址
     */
    public static String getImageHost() {
        return getCacheTerminalConfigs().getImgHost();
    }

}
