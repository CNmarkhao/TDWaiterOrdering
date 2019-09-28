package com.tdlbs.waiterordering.mvp.page.order.choose_product;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.swipepanel.SwipePanel;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tdlbs.core.eventbus.EventCenter;
import com.tdlbs.waiterordering.R;
import com.tdlbs.waiterordering.app.TApplication;
import com.tdlbs.waiterordering.app.base.BaseActivity;
import com.tdlbs.waiterordering.app.utils.BigDecimalUtils;
import com.tdlbs.waiterordering.app.utils.ButtonUtils;
import com.tdlbs.waiterordering.app.utils.LocalCacheUtils;
import com.tdlbs.waiterordering.app.utils.RequestUtils;
import com.tdlbs.waiterordering.constant.AppConstants;
import com.tdlbs.waiterordering.constant.EventBusCodes;
import com.tdlbs.waiterordering.constant.SPConstants;
import com.tdlbs.waiterordering.di.component.DaggerActivityComponent;
import com.tdlbs.waiterordering.di.module.ActivityModule;
import com.tdlbs.waiterordering.mvp.adapter.ProductImageAdapter;
import com.tdlbs.waiterordering.mvp.adapter.ProductOrderedAdapter;
import com.tdlbs.waiterordering.mvp.adapter.ProductTextAdapter;
import com.tdlbs.waiterordering.mvp.adapter.ProductTypeAdapter;
import com.tdlbs.waiterordering.mvp.bean.entity.GetOrderDetailParam;
import com.tdlbs.waiterordering.mvp.bean.model.OrderDetail;
import com.tdlbs.waiterordering.mvp.bean.model.OrderProductList;
import com.tdlbs.waiterordering.mvp.bean.model.ShopDataPackage;
import com.tdlbs.waiterordering.mvp.dialog.common.CommonDg;
import com.tdlbs.waiterordering.mvp.dialog.product_memo.ProductMemoDg;
import com.tdlbs.waiterordering.mvp.page.order.shopping_cart.ShoppingCartActivity;
import com.tdlbs.waiterordering.mvp.widget.BadgeView;
import com.tdlbs.waiterordering.mvp.widget.MaxHeightRecyclerView;
import com.tdlbs.waiterordering.mvp.widget.searchview.RevealSearchView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * ================================================
 * 选择商品界面
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-15 15:59
 * ================================================
 */
public class ChooseProductActivity extends BaseActivity<ChooseProductPresenter> implements ChooseProductContract.View {

    @BindView(R.id.page_title_tv)
    TextView mPageTitleTv;
    @BindView(R.id.product_type_rv)
    RecyclerView mProductTypeRv;
    @BindView(R.id.product_detail_rv)
    RecyclerView mProductDetailRv;
    @BindView(R.id.ordered_product_rv)
    MaxHeightRecyclerView mOrderedProductRv;
    @BindView(R.id.ordered_product_ll)
    LinearLayout mOrderedProductLl;
    @BindView(R.id.order_price_tv)
    TextView mOrderPriceTv;
    @BindView(R.id.order_bag_btn)
    ImageView mOrderBagBtn;
    @BindView(R.id.order_confirm_btn)
    TextView mOrderConfirmBtn;
    @BindView(R.id.order_bag_num_bv)
    BadgeView mOrderBagNumBv;
    @BindView(R.id.product_search_view)
    RevealSearchView mProductSearchView;

    private CommonDg mCommonDg;
    private boolean mShouldClosed;
    private List<ShopDataPackage.ProductCategoryListBean> mTypeList;
    private List<ShopDataPackage.ProductListBean> mAllProductList;
    private String mTableName;
    private OrderDetail mOptOrderDetail;
    private boolean isShowPic;
    private ProductTypeAdapter mProductTypeAdapter;
    private ProductTextAdapter mProductTextAdapter;
    private ProductOrderedAdapter mProductOrderedAdapter;
    private ImageView mBall;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_choose_product;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        initSwipePanelView();
        initSearchView();
        loadLocalData();
        initRecyclerView();
        resumeOrderDetail();
    }

    private void loadLocalData() {
        mTypeList = new ArrayList<>();
        mTypeList.addAll(LocalCacheUtils.getCacheTypeList());
        mAllProductList = LocalCacheUtils.getCacheProductList();
        isShowPic = SPUtils.getInstance(SPConstants.FileName.SETTING).
                getBoolean(SPConstants.Setting.SHOW_PIC, true);
    }

    private void initRecyclerView() {
        mProductTypeAdapter = new ProductTypeAdapter(mTypeList);
        mProductTypeRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mProductTypeRv.setAdapter(mProductTypeAdapter);
        mProductTypeAdapter.setOnItemClickListener((adapter, view, position) -> {
            mProductTypeAdapter.setCurrentPosition(position);
            filterProductData(mProductTypeAdapter.getData().get(position).getId());
        });

        mProductTextAdapter = isShowPic ? new ProductImageAdapter(new ArrayList<>()) :
                new ProductTextAdapter(new ArrayList<>());
        mProductDetailRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mProductDetailRv.setAdapter(mProductTextAdapter);
        mProductTextAdapter.setOnItemClickListener((adapter, view, position) -> {
            int[] startLocation = new int[2];
            view.findViewById(R.id.ball_tv).getLocationInWindow(startLocation);
            showDemoLayout(mProductTextAdapter.getData().get(position), startLocation);
        });

        if (mTypeList.size() > 0) {
            mProductTypeAdapter.setOnItemClick(null, 0);
        }
        mProductOrderedAdapter = new ProductOrderedAdapter(new ArrayList<>(), () -> refreshLayout());
        mOrderedProductRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mOrderedProductRv.setAdapter(mProductOrderedAdapter);
    }

    private void showDemoLayout(ShopDataPackage.ProductListBean productListBean, int[] location) {
        ProductMemoDg mProductMemoDg = new ProductMemoDg(productListBean.getId(), productListBean.getName(), (productId, memo) -> {
            mBall = new ImageView(getApplicationContext());
            mBall.setImageResource(R.mipmap.ic_ball);
            setAnim(mBall, location);
            addProduct(productId, memo, 1);
        });
        mProductMemoDg.show(getSupportFragmentManager(), ProductMemoDg.class.getSimpleName());
    }

    private void addProduct(long productId, String memo, int count) {
        ShopDataPackage.ProductListBean productListBean = LocalCacheUtils.getProduct(productId);
        if (productListBean == null) {
            return;
        }
        OrderDetail.Product selected = null;
        Iterator<OrderDetail.Product> iterator = mProductOrderedAdapter.getData().iterator();
        while (iterator.hasNext()) {
            OrderDetail.Product item = iterator.next();
            if (item.getMemo().equals(memo) && item.getProductId() == productId) {
                item.setProductCount(item.getProductCount() + count);
                selected = item;
                iterator.remove();
            }
        }
        if (selected == null) {
            selected = new OrderDetail.Product();
            selected.setProductId(productListBean.getId());
            selected.setProductCount(count);
            selected.setMemo(memo);
            selected.setStatus(2);
            selected.setOriginalPrice(productListBean.getPrice());
            selected.setProductCategoryId(productListBean.getProductCategoryId());
            selected.setProductName(productListBean.getName());
            selected.setDiscount(productListBean.getDiscount());
            selected.setCurrentPrice(BigDecimalUtils.mul(productListBean.getPrice() * productListBean.getDiscount(), 0.01).doubleValue());
            selected.setDetailUuid(RequestUtils.getUUID());
        }
        if (selected.getProductCount() != 0) {
            mProductOrderedAdapter.getData().add(0, selected);
        }
        mProductOrderedAdapter.notifyDataSetChanged();
        refreshLayout();
    }

    private void refreshLayout() {
        for (ShopDataPackage.ProductCategoryListBean item : mProductTypeAdapter.getData()) {
            int count = 0;
            for (OrderDetail.Product bean : mProductOrderedAdapter.getData()) {
                if (bean.getProductCategoryId() == item.getId()) {
                    count += bean.getProductCount();
                }
            }
            item.setLocalCount(count);
        }

        for (ShopDataPackage.ProductListBean item : mProductTextAdapter.getData()) {
            int count = 0;
            for (OrderDetail.Product bean : mProductOrderedAdapter.getData()) {
                if (bean.getProductId() == item.getId()) {
                    count += bean.getProductCount();
                }
            }
            item.setLocalCount(count);
        }

        for (ShopDataPackage.ProductListBean item : mAllProductList) {
            int count = 0;
            for (OrderDetail.Product bean : mProductOrderedAdapter.getData()) {
                if (bean.getProductId() == item.getId()) {
                    count += bean.getProductCount();
                }
            }
            item.setLocalCount(count);
        }

        mProductTypeAdapter.notifyDataSetChanged();
        mProductTextAdapter.notifyDataSetChanged();
        if (mProductOrderedAdapter.getData().size() == 0) {
            mOrderBagBtn.setImageDrawable(getDrawable(R.mipmap.ic_bag_empty));
            mOrderConfirmBtn.setBackgroundColor(getResources().getColor(R.color.gray_6));
            mOrderConfirmBtn.setTextColor(getResources().getColor(R.color.gray_2));
            mOrderPriceTv.setTextColor(getResources().getColor(R.color.gray_2));
            mOrderPriceTv.setText("未选购商品");
            mOrderBagNumBv.showBadge(null);
            mOrderedProductLl.setVisibility(View.INVISIBLE);
        } else {
            mOrderBagBtn.setImageDrawable(getDrawable(R.mipmap.ic_bag_full));
            mOrderConfirmBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            mOrderConfirmBtn.setTextColor(getResources().getColor(R.color.white));
            mOrderPriceTv.setTextColor(getResources().getColor(R.color.white));
            int count = 0;
            double price = 0;
            for (OrderDetail.Product item : mProductOrderedAdapter.getData()) {
                count += item.getProductCount();
                price = BigDecimalUtils.add(price, BigDecimalUtils.mul(item.getProductCount(), item.getCurrentPrice()).doubleValue()).doubleValue();
            }

            mOrderPriceTv.setText(String.format(Locale.CHINA, "￥%.2f", price));
            mOrderBagNumBv.showBadge(String.valueOf(count));
        }
    }

    private void filterProductData(long id) {
        List<ShopDataPackage.ProductListBean> itemProductList = new ArrayList<>();
        for (int i = 0; i < mAllProductList.size(); i++) {
            ShopDataPackage.ProductListBean bean = mAllProductList.get(i);
            if (bean.getProductCategoryId() == id) {
                itemProductList.add(bean);
            }
        }
        mProductTextAdapter.getData().clear();
        mProductTextAdapter.getData().addAll(itemProductList);
        mProductTextAdapter.notifyDataSetChanged();
    }

    private void filterProductDataByName(String name) {
        List<ShopDataPackage.ProductListBean> itemProductList = new ArrayList<>();
        for (int i = 0; i < mAllProductList.size(); i++) {
            ShopDataPackage.ProductListBean bean = mAllProductList.get(i);
            if (!TextUtils.isEmpty(name) && bean.getName().contains(name)) {
                itemProductList.add(bean);
            }
        }
        mProductTextAdapter.getData().clear();
        mProductTextAdapter.getData().addAll(itemProductList);
        mProductTextAdapter.notifyDataSetChanged();
    }

    private void resumeOrderDetail() {
        String orderNo = getIntent().getExtras().getString(AppConstants.BundleCode.ORDER_NO);
        mTableName = getIntent().getExtras().getString(AppConstants.BundleCode.TABLE_NAME);
        if (StringUtils.isEmpty(orderNo)) {
            finish();
            return;
        }
        mPresenter.networkGetOrderDetail(new GetOrderDetailParam(orderNo));
    }

    private void initSearchView() {
        mProductSearchView.setOnQueryTextListener(new RevealSearchView.OnQueryTextListener() {
            @Override
            public void onQueryTextSubmit(String queryText) {
            }

            @Override
            public void onHideSearchView() {
                mProductTypeRv.setVisibility(View.VISIBLE);
                if (mTypeList.size() > 0) {
                    mProductTypeAdapter.setOnItemClick(null, 0);
                }
            }

            @Override
            public void onShowSearchView() {
                mProductTypeRv.setVisibility(View.INVISIBLE);
                mProductTextAdapter.getData().clear();
                mProductTextAdapter.notifyDataSetChanged();
            }

            @Override
            public void onQueryTextChange(String queryText) {
                filterProductDataByName(queryText);
            }
        });
    }

    private void initSwipePanelView() {
        SwipePanel swipePanel = new SwipePanel(this);
        swipePanel.setLeftEdgeSize(SizeUtils.dp2px(100));
        swipePanel.setLeftDrawable(R.mipmap.base_back);
        swipePanel.wrapView(findViewById(R.id.content_fl));
        swipePanel.setOnFullSwipeListener(direction -> {
            finish();
            swipePanel.close(direction);
        });
    }

    @Override
    public void finish() {
        if (mShouldClosed) {
            mShouldClosed = false;
            super.finish();
            return;
        }
        showBackWarning();
    }

    private void showBackWarning() {
        mCommonDg = new CommonDg("取消点菜", "取消点菜将清空购物车，是否继续？", new CommonDg.OnConfirmListener() {
            @Override
            public void confirm() {
                mShouldClosed = true;
                finish();
            }

            @Override
            public void cancel() {
                mCommonDg.dismiss();
            }
        });
        mCommonDg.show(getSupportFragmentManager(), CommonDg.class.getSimpleName());
    }

    @Override
    public void onBackPressed() {
        if (mProductSearchView.isSearchOpen()) {
            mProductSearchView.hideSearch();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void initInjector() {
        DaggerActivityComponent.builder().
                applicationComponent(TApplication.getInstance().getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build().inject(this);
    }

    private void goShoppingCart() {
        if (mProductOrderedAdapter.getData().size() == 0) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.BundleCode.TABLE_TITLE, mPageTitleTv.getText().toString());
        bundle.putSerializable(AppConstants.BundleCode.ORDER_DETAIL, mOptOrderDetail);
        bundle.putSerializable(AppConstants.BundleCode.PRODUCT_LIST, new OrderProductList(mProductOrderedAdapter.getData()));
        readyGo(ShoppingCartActivity.class, bundle);
    }


    private void showOrderedBag() {
        if (mProductOrderedAdapter.getData().size() == 0) {
            return;
        }
        if (mOrderedProductLl.getVisibility() == View.VISIBLE) {
            mOrderedProductLl.setVisibility(View.INVISIBLE);
        } else {
            mOrderedProductLl.setVisibility(View.VISIBLE);
        }
    }

    private void clearAllOrderedProduct() {
        mProductOrderedAdapter.getData().clear();
        mProductOrderedAdapter.notifyDataSetChanged();
        refreshLayout();
        showMessage("购物车已被清空");
    }

    private void showSearchView() {
        mProductSearchView.showSearch();
    }

    @Override
    protected void onEventComing(EventCenter eventCenter) {
        if (eventCenter.getEventCode() == EventBusCodes.FINISH_CART_PAGE) {
            mProductOrderedAdapter.getData().clear();
            mProductOrderedAdapter.getData().addAll(((OrderProductList) eventCenter.getData()).getMProductList());
            mProductOrderedAdapter.notifyDataSetChanged();
            refreshLayout();
        } else if (eventCenter.getEventCode() == EventBusCodes.SUBMIT_CART_PAGE) {
            mShouldClosed = true;
            finish();
        }
    }

    @Override
    public void getOrderDetailError(GetOrderDetailParam param, String reason) {
        if (mCommonDg == null) {
            mCommonDg = new CommonDg("订单获取失败", reason + "是否发起重试？", new CommonDg.OnConfirmListener() {
                @Override
                public void confirm() {
                    mPresenter.networkGetOrderDetail(param);
                }

                @Override
                public void cancel() {
                    finish();
                }
            });
        }
        mCommonDg.show(getSupportFragmentManager(), CommonDg.class.getSimpleName());
    }

    @Override
    public void getOrderDetailOk(OrderDetail detail) {
        if (detail.getStatus() != ShopDataPackage.TableListBean.ORDER_STATUS_WAIT_BILL &&
                detail.getStatus() != ShopDataPackage.TableListBean.ORDER_STATUS_CREATE) {
            ToastUtils.showShort("当前订单状态不正确");
            mShouldClosed = true;
            finish();
            return;
        }
        mOptOrderDetail = detail;
        mPageTitleTv.setText(String.format(Locale.CHINA, "%s(%d人)点菜", mTableName, mOptOrderDetail.getPeopleNum()));
    }

    @OnClick({R.id.left_menu_btn, R.id.right_menu_btn, R.id.ordered_empty_btn, R.id.order_confirm_btn,
            R.id.ordered_product_ll, R.id.order_bag_btn})
    public void onViewClicked(View view) {
        if (ButtonUtils.isFastDoubleClick()){
            return;
        }
        switch (view.getId()) {
            case R.id.left_menu_btn:
                finish();
                break;
            case R.id.ordered_product_ll:
                mOrderedProductLl.setVisibility(View.INVISIBLE);
                break;
            case R.id.right_menu_btn:
                showSearchView();
                break;
            case R.id.ordered_empty_btn:
                clearAllOrderedProduct();
                break;
            case R.id.order_confirm_btn:
                goShoppingCart();
                break;
            case R.id.order_bag_btn:
                showOrderedBag();
                break;
            default:
                break;
        }
    }

    private void setAnim(final View v, int[] startLocation) {
        ViewGroup animViewGroup = null;
        animViewGroup = createAnimLayout();
        animViewGroup.addView(v);
        final View view = addViewToAnimLayout(animViewGroup, v,
                startLocation);
        int[] endLocation = new int[2];
        mOrderBagBtn.getLocationInWindow(endLocation);

        // 计算位移
        int endX = 0 - startLocation[0] + 40;
        int endY = endLocation[1] - startLocation[1];
        TranslateAnimation translateAnimationX = new TranslateAnimation(0,
                endX, 0, 0);
        translateAnimationX.setInterpolator(new LinearInterpolator());
        translateAnimationX.setRepeatCount(0);
        translateAnimationX.setFillAfter(true);

        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
                0, endY);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);
        translateAnimationX.setFillAfter(true);

        final AnimationSet set = new AnimationSet(false);
        set.setFillAfter(false);
        set.addAnimation(translateAnimationY);
        set.addAnimation(translateAnimationX);
        set.setDuration(300);
        view.startAnimation(set);

        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.GONE);
                set.cancel();
                animation.cancel();
            }
        });

    }

    private ViewGroup createAnimLayout() {
        ViewGroup rootView = (ViewGroup) this.getWindow().getDecorView();
        LinearLayout animLayout = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setId(Integer.MAX_VALUE);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;
    }

    private View addViewToAnimLayout(final ViewGroup parent, final View view,
                                     int[] location) {
        int x = location[0];
        int y = location[1];
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setLayoutParams(lp);
        return view;
    }

}
