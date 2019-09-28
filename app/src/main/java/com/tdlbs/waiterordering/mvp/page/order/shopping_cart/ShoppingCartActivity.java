package com.tdlbs.waiterordering.mvp.page.order.shopping_cart;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.swipepanel.SwipePanel;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tdlbs.core.eventbus.EventCenter;
import com.tdlbs.waiterordering.R;
import com.tdlbs.waiterordering.app.TApplication;
import com.tdlbs.waiterordering.app.base.BaseActivity;
import com.tdlbs.waiterordering.app.utils.ButtonUtils;
import com.tdlbs.waiterordering.constant.AppConstants;
import com.tdlbs.waiterordering.constant.EventBusCodes;
import com.tdlbs.waiterordering.di.component.DaggerActivityComponent;
import com.tdlbs.waiterordering.di.module.ActivityModule;
import com.tdlbs.waiterordering.mvp.adapter.ProductCartAdapter;
import com.tdlbs.waiterordering.mvp.bean.entity.UpdateOrderParam;
import com.tdlbs.waiterordering.mvp.bean.model.OrderDetail;
import com.tdlbs.waiterordering.mvp.bean.model.OrderProductList;
import com.tdlbs.waiterordering.mvp.widget.mutilselect.MutilSelectAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * ================================================
 * 商品购物车下单界面
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-15 15:59
 * ================================================
 */
public class ShoppingCartActivity extends BaseActivity<ShoppingCartPresenter> implements ShoppingCartContract.View {
    @BindView(R.id.page_title_tv)
    TextView mPageTitleTv;
    @BindView(R.id.right_menu_btn)
    TextView mRightMenuBtn;
    @BindView(R.id.ordered_product_rv)
    RecyclerView mOrderedProductRv;
    @BindView(R.id.confirm_order_btn)
    TextView mConfirmOrderBtn;
    @BindView(R.id.opt_ll)
    LinearLayout mOptLl;
    @BindView(R.id.ordered_discount_tv)
    TextView mOrderedDiscountTv;
    @BindView(R.id.ordered_payable_tv)
    TextView mOrderedPayableTv;
    @BindView(R.id.ordered_num_tv)
    TextView mOrderedNumTv;
    @BindView(R.id.order_price_fl)
    FrameLayout mOrderPriceFl;

    private String mPageTitle;
    private OrderDetail mOptOrderDetail;
    private List<OrderDetail.Product> mOptProductList;
    private ProductCartAdapter mProductCartAdapter;
    private MutilSelectAdapter mMutilSelectAdapter;
    private boolean selected = false;
    private boolean isSubmit = false;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_shopping_cart;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        initSwipePanelView();

        resumeOrderDetail();
    }

    private void initRecyclerView() {
        mProductCartAdapter = new ProductCartAdapter(mOptProductList);
        mMutilSelectAdapter = new MutilSelectAdapter.MutiSelectAdapterBuilder()
                .setContext(getApplicationContext())
                .setStyle(MutilSelectAdapter.MutiSelectAdapterBuilder.Style.Left)
                .setAdapter(mProductCartAdapter)
                .setMutiSelectDrawable(R.mipmap.radio_on, R.mipmap.radio_off)
                .addOnItemClickListerns((view, position) -> {

                })
                .build();

        mOrderedProductRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mOrderedProductRv.setAdapter(mMutilSelectAdapter);
    }

    private void resumeOrderDetail() {
        Bundle extras = getIntent().getExtras();
        mPageTitle = extras.getString(AppConstants.BundleCode.TABLE_TITLE, "");
        mOptOrderDetail = (OrderDetail) extras.getSerializable(AppConstants.BundleCode.ORDER_DETAIL);
        OrderProductList orderProductList = (OrderProductList) extras.getSerializable(AppConstants.BundleCode.PRODUCT_LIST);
        if (StringUtils.isEmpty(mPageTitle) || mOptOrderDetail == null || orderProductList == null) {
            ToastUtils.showShort("订单参数有误");
            finish();
            return;
        }

        mOptProductList = orderProductList.getMProductList();
        initRecyclerView();
        refreshPageLayout();
    }

    private void refreshPageLayout() {
        mMutilSelectAdapter.notiAdapter(mProductCartAdapter);
        mPageTitleTv.setText(mPageTitle);
        int count = 0;
        double discountPrice = 0, payablePrice = 0;
        for (OrderDetail.Product item : mProductCartAdapter.getData()) {
            count += item.getProductCount();
            discountPrice += item.getProductCount() * (1 - item.getDiscount() * 0.01) * item.getOriginalPrice();
            payablePrice += item.getProductCount() * item.getDiscount() * 0.01 * item.getOriginalPrice();
        }

        mOrderedDiscountTv.setText(String.format(Locale.CHINA, "已优惠￥%.2f", discountPrice));
        mOrderedPayableTv.setText(String.format(Locale.CHINA, "￥%.2f", payablePrice));
        mOrderedNumTv.setText(String.format(Locale.CHINA, "x%d", count));
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
    protected void initInjector() {
        DaggerActivityComponent.builder().
                applicationComponent(TApplication.getInstance().getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build().inject(this);
    }

    private void confirmOrder() {
        mOptOrderDetail.getProductList().addAll(mProductCartAdapter.getData());

        UpdateOrderParam param = new UpdateOrderParam();
        param.setIsWaiting(mOptOrderDetail.getWaitFlag());
        param.setBuyerMobile(mOptOrderDetail.getBuyerMobile());
        param.setIsChangedTable(0);
        param.setOrderNo(mOptOrderDetail.getOrderNo());
        param.setOrderStatus(2);
        param.setPeopleNum(mOptOrderDetail.getPeopleNum());
        param.setVersion(mOptOrderDetail.getVersion());
        param.setDishList(mOptOrderDetail.getProductList());
        param.setTableId(mOptOrderDetail.getTableId());
        mPresenter.networkChangeOrder(param);

    }

    private void confirmDeleteProduct() {
        List<OrderDetail.Product> sProduct = new ArrayList<>();
        List<Integer> selected = mMutilSelectAdapter.getSelectListForPosition();
        for (int i = 0; i < selected.size(); i++) {
            sProduct.add(mProductCartAdapter.getData().get(selected.get(i)));
        }
        for (OrderDetail.Product item : sProduct) {
            mProductCartAdapter.getData().remove(item);
        }
        sProduct.clear();
        mProductCartAdapter.notifyDataSetChanged();
        refreshPageLayout();
        toggleRightMenu();
        if (mProductCartAdapter.getData().size() == 0) {
            ToastUtils.showShort("购物车已被清空");
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (isSubmit) {
            EventBus.getDefault().post(new EventCenter(EventBusCodes.SUBMIT_CART_PAGE));
        } else {
            EventBus.getDefault().post(new EventCenter(EventBusCodes.FINISH_CART_PAGE, new OrderProductList(mProductCartAdapter.getData())));
        }
    }

    private void toggleRightMenu() {
        if (selected) {
            selected = false;
            mRightMenuBtn.setText("操作");
            mMutilSelectAdapter.setSelect(false);
            mOrderPriceFl.setVisibility(View.VISIBLE);
            mConfirmOrderBtn.setVisibility(View.VISIBLE);
            mOptLl.setVisibility(View.GONE);
        } else {
            selected = true;
            mRightMenuBtn.setText("取消操作");
            mMutilSelectAdapter.setSelect(true);
            mOrderPriceFl.setVisibility(View.GONE);
            mConfirmOrderBtn.setVisibility(View.GONE);
            mOptLl.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void updateOrderOk() {
        ToastUtils.showShort("已成功下单");
        isSubmit = true;
        finish();
    }

    @OnClick({R.id.left_menu_btn, R.id.right_menu_btn, R.id.confirm_order_btn, R.id.all_selected_btn, R.id.confirm_delete_btn})
    public void onViewClicked(View view) {
        if (ButtonUtils.isFastDoubleClick()){
            return;
        }
        switch (view.getId()) {
            case R.id.left_menu_btn:
                finish();
                break;
            case R.id.right_menu_btn:
                toggleRightMenu();
                break;
            case R.id.confirm_order_btn:
                confirmOrder();
                break;
            case R.id.all_selected_btn:
                if (mMutilSelectAdapter.isSelectAll()) {
                    mMutilSelectAdapter.clearAllGenerations();
                } else {
                    mMutilSelectAdapter.setFutureGenerations();
                }
                break;
            case R.id.confirm_delete_btn:
                confirmDeleteProduct();
                break;
            default:
                break;
        }
    }
}
