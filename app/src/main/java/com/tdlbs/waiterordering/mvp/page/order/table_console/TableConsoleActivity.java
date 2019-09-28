package com.tdlbs.waiterordering.mvp.page.order.table_console;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.swipepanel.SwipePanel;
import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tdlbs.core.eventbus.EventCenter;
import com.tdlbs.waiterordering.R;
import com.tdlbs.waiterordering.app.TApplication;
import com.tdlbs.waiterordering.app.base.BaseActivity;
import com.tdlbs.waiterordering.app.utils.ButtonUtils;
import com.tdlbs.waiterordering.app.utils.LocalCacheUtils;
import com.tdlbs.waiterordering.constant.AppConstants;
import com.tdlbs.waiterordering.constant.EventBusCodes;
import com.tdlbs.waiterordering.di.component.DaggerActivityComponent;
import com.tdlbs.waiterordering.di.module.ActivityModule;
import com.tdlbs.waiterordering.mvp.adapter.ConsoleProductAdapter;
import com.tdlbs.waiterordering.mvp.adapter.ConsoleTableAdapter;
import com.tdlbs.waiterordering.mvp.bean.entity.DiscountProductParam;
import com.tdlbs.waiterordering.mvp.bean.entity.GetOrderDetailParam;
import com.tdlbs.waiterordering.mvp.bean.entity.UpdateOrderParam;
import com.tdlbs.waiterordering.mvp.bean.model.OrderDetail;
import com.tdlbs.waiterordering.mvp.bean.model.PrintCommand;
import com.tdlbs.waiterordering.mvp.bean.model.ShopDataPackage;
import com.tdlbs.waiterordering.mvp.dialog.common.CommonDg;
import com.tdlbs.waiterordering.mvp.dialog.modifyPeople.ModifyPeopleDg;
import com.tdlbs.waiterordering.mvp.dialog.product_opt.ProductOptDg;
import com.tdlbs.waiterordering.mvp.page.order.choose_product.ChooseProductActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * ================================================
 * 餐桌订单详情管理界面
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-15 15:59
 * ================================================
 */
public class TableConsoleActivity extends BaseActivity<TableConsolePresenter> implements TableConsoleContract.View {
    @BindView(R.id.table_no_tv)
    TextView mTableNoTv;
    @BindView(R.id.people_num_tv)
    TextView mPeopleNumTv;
    @BindView(R.id.bill_amount_tv)
    TextView mBillAmountTv;
    @BindView(R.id.eat_time_tv)
    TextView mEatTimeTv;
    @BindView(R.id.modify_table_fl)
    RelativeLayout mModifyTableFl;
    @BindView(R.id.table_type_rg)
    RadioGroup mTableTypeRg;
    @BindView(R.id.modify_table_normal_rb)
    AppCompatRadioButton mModifyTableNormalRb;
    @BindView(R.id.product_rv)
    RecyclerView mProductRv;
    @BindView(R.id.modify_table_rv)
    RecyclerView mModifyTableRv;

    private String mTableName;
    private ConsoleProductAdapter mConsoleProductAdapter;
    private ConsoleTableAdapter mConsoleTableAdapter;
    private OrderDetail mOptOrderDetail;
    private ShopDataPackage.TableListBean mModifyTable;
    private OrderDetail.Product mOptProduct;
    private ProductOptDg mProductOptDg;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_table_console;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        initSwipePanelView();
        resumeOrderDetail();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mConsoleProductAdapter = new ConsoleProductAdapter(new ArrayList<>());
        mConsoleProductAdapter.setOnItemClickListener((adapter1, view, position) -> {
            mOptProduct = mConsoleProductAdapter.getData().get(position);
            mProductOptDg = new ProductOptDg(mOptProduct.getProductName(), mOptProduct, new ProductOptDg.OnProductOptListener() {
                @Override
                public void discountProduct(int discount) {
                    List<String> ids = new ArrayList<>();
                    ids.add(mOptProduct.getDetailUuid());
                    mPresenter.networkDiscountProduct(new DiscountProductParam(mOptOrderDetail.getOrderNo(),
                            ids, discount));
                }

                @Override
                public void notifyProduct(int type, OrderDetail.Product product) {
                    EventBus.getDefault().post(new EventCenter(EventBusCodes.PRINT_PRODUCT, new PrintCommand(product, mTableName, type)));
                }
            });
            mProductOptDg.show(getSupportFragmentManager(), ProductOptDg.class.getSimpleName());
        });
        mProductRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mProductRv.setAdapter(mConsoleProductAdapter);

        mConsoleTableAdapter = new ConsoleTableAdapter(new ArrayList<>());
        mModifyTableRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mModifyTableRv.setAdapter(mConsoleTableAdapter);
        mTableTypeRg.setOnCheckedChangeListener((group, checkedId) -> {
            List<ShopDataPackage.TableListBean> tables = new ArrayList<>();
            List<ShopDataPackage.TableListBean> allTables = LocalCacheUtils.getCacheTableList();
            for (int i = 0; i < allTables.size(); i++) {
                ShopDataPackage.TableListBean bean = allTables.get(i);
                if (checkedId == R.id.modify_table_normal_rb) {
                    if (bean.getTableType() == ShopDataPackage.TableListBean.TABLE_TYPE_NORMAL) {
                        tables.add(bean);
                    }
                } else {
                    if (bean.getTableType() == ShopDataPackage.TableListBean.TABLE_TYPE_ROOM) {
                        tables.add(bean);
                    }
                }

            }
            mConsoleTableAdapter.resetData(tables);
        });
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

    private void initSwipePanelView() {
        SwipePanel swipePanel = new SwipePanel(this);
        swipePanel.setLeftEdgeSize(SizeUtils.dp2px(100));
        swipePanel.setLeftDrawable(R.mipmap.base_back);
        swipePanel.wrapView(findViewById(R.id.content_ll));
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

    private void goChooseProduct() {
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.BundleCode.ORDER_NO, mOptOrderDetail.getOrderNo());
        bundle.putString(AppConstants.BundleCode.TABLE_NAME, mTableName);
        readyGo(ChooseProductActivity.class, bundle);
    }

    private void showModifyPeopleView() {
        ModifyPeopleDg mModifyPeopleDg = new ModifyPeopleDg(peopleNum -> {
            UpdateOrderParam param = new UpdateOrderParam();
            param.setIsWaiting(mOptOrderDetail.getWaitFlag());
            param.setBuyerMobile(mOptOrderDetail.getBuyerMobile());
            param.setIsChangedTable(0);
            param.setOrderNo(mOptOrderDetail.getOrderNo());
            param.setOrderStatus(mOptOrderDetail.getStatus());
            param.setPeopleNum(peopleNum);
            param.setVersion(mOptOrderDetail.getVersion());
            param.setDishList(mOptOrderDetail.getProductList());
            param.setTableId(mOptOrderDetail.getTableId());
            param.autoSign();
            mPresenter.networkUpdateOrder(param);
        });
        mModifyPeopleDg.show(getSupportFragmentManager(), ModifyPeopleDg.class.getSimpleName());
    }

    @Override
    protected void onEventComing(EventCenter eventCenter) {
        if (eventCenter.getEventCode() == EventBusCodes.SUBMIT_CART_PAGE) {
            mPresenter.networkGetOrderDetail(new GetOrderDetailParam(mOptOrderDetail.getOrderNo()));
        }
    }

    private void modifyTable() {
        if (mConsoleTableAdapter.getCurrentPosition() == -1) {
            showMessage("请先选择需要更换到桌台");
            return;
        }
        mModifyTable = mConsoleTableAdapter.getData().get(mConsoleTableAdapter.getCurrentPosition());
        UpdateOrderParam param = new UpdateOrderParam();
        param.setIsWaiting(mOptOrderDetail.getWaitFlag());
        param.setBuyerMobile(mOptOrderDetail.getBuyerMobile());
        param.setIsChangedTable(1);
        param.setOrderNo(mOptOrderDetail.getOrderNo());
        param.setOrderStatus(mOptOrderDetail.getStatus());
        param.setPeopleNum(mOptOrderDetail.getPeopleNum());
        param.setVersion(mOptOrderDetail.getVersion());
        param.setDishList(mOptOrderDetail.getProductList());
        param.setTableId(mModifyTable.getId());
        param.autoSign();
        mPresenter.networkUpdateOrder(param);
        mModifyTableFl.setVisibility(View.GONE);
    }

    private void showModifyTableView() {
        mModifyTableFl.setVisibility(View.VISIBLE);
        mTableTypeRg.check(R.id.modify_table_normal_rb);
    }


    @Override
    public void updateOrderOk(OrderDetail detail, boolean isChangeTable) {
        if (isChangeTable) {
            mTableName = mModifyTable.getName();
            showMessage("换桌成功");
        } else {
            showMessage("修改就餐人数成功");
        }
        getOrderDetailOk(detail);
    }

    @Override
    public void discountProduct(OrderDetail detail, boolean isFree) {
        showMessage(isFree ? "菜品赠送成功" : "菜品折扣成功");
        getOrderDetailOk(detail);
    }

    @Override
    public void getOrderDetailOk(OrderDetail detail) {
        if (detail.getStatus() != ShopDataPackage.TableListBean.ORDER_STATUS_WAIT_BILL) {
            ToastUtils.showShort("当前订单状态不正确");
            finish();
            return;
        }
        mOptOrderDetail = detail;
        mTableNoTv.setText(mTableName);
        mPeopleNumTv.setText(String.valueOf(detail.getPeopleNum()));
        mBillAmountTv.setText(String.format(Locale.CHINA, "￥%.2f", detail.getProductFee()));
        String time = String.format(Locale.CHINA, "%.2fh", -TimeUtils.getTimeSpanByNow(detail.getCreateTime(), TimeConstants.MIN) / 60f);
        mEatTimeTv.setText(time);
        Collections.reverse(detail.getProductList());
        mConsoleProductAdapter.getData().clear();
        mConsoleProductAdapter.getData().addAll(detail.getProductList());
        mConsoleProductAdapter.notifyDataSetChanged();
    }

    @Override
    public void getOrderDetailError(GetOrderDetailParam param, String reason) {
        CommonDg mCommonDg = new CommonDg("订单获取失败", reason + "是否发起重试？", new CommonDg.OnConfirmListener() {
            @Override
            public void confirm() {
                mPresenter.networkGetOrderDetail(param);
            }

            @Override
            public void cancel() {
                finish();
            }
        });
        mCommonDg.show(getSupportFragmentManager(), CommonDg.class.getSimpleName());
    }

    @OnClick({R.id.left_menu_btn, R.id.modify_people_num_btn, R.id.modify_table_btn, R.id.add_product_btn,
            R.id.merge_table_btn, R.id.modify_table_cancel_btn, R.id.modify_table_confirm_btn, R.id.modify_table_fl})
    public void onViewClicked(View view) {
        if (ButtonUtils.isFastDoubleClick()){
            return;
        }
        switch (view.getId()) {
            case R.id.merge_table_btn:
                showMessage("敬请期待");
                break;
            case R.id.left_menu_btn:
                finish();
                break;
            case R.id.modify_people_num_btn:
                showModifyPeopleView();
                break;
            case R.id.modify_table_btn:
                showModifyTableView();
                break;
            case R.id.add_product_btn:
                goChooseProduct();
                break;
            case R.id.modify_table_confirm_btn:
                modifyTable();
                break;
            case R.id.modify_table_cancel_btn:
                mModifyTableFl.setVisibility(View.GONE);
                break;
            case R.id.modify_table_fl:
                mModifyTableFl.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }
}
