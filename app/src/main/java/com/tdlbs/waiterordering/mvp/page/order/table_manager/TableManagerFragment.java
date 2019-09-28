package com.tdlbs.waiterordering.mvp.page.order.table_manager;

import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.AdaptScreenUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tdlbs.core.eventbus.EventCenter;
import com.tdlbs.core.ui.Item_decoration.SpacesItemDecoration;
import com.tdlbs.waiterordering.R;
import com.tdlbs.waiterordering.app.TApplication;
import com.tdlbs.waiterordering.app.base.fragmentation.BaseFFragment;
import com.tdlbs.waiterordering.app.utils.ButtonUtils;
import com.tdlbs.waiterordering.app.utils.LocalCacheUtils;
import com.tdlbs.waiterordering.constant.AppConstants;
import com.tdlbs.waiterordering.constant.EventBusCodes;
import com.tdlbs.waiterordering.di.component.DaggerFragmentComponent;
import com.tdlbs.waiterordering.mvp.adapter.TableExpandableItemAdapter;
import com.tdlbs.waiterordering.mvp.bean.model.ShopDataPackage;
import com.tdlbs.waiterordering.mvp.bean.model.TableType;
import com.tdlbs.waiterordering.mvp.bean.model.TableTypeParam;
import com.tdlbs.waiterordering.mvp.dialog.create_order.CreateOrderDg;
import com.tdlbs.waiterordering.mvp.page.order.choose_product.ChooseProductActivity;
import com.tdlbs.waiterordering.mvp.page.order.table_console.TableConsoleActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * ================================================
 * 餐桌列表管理界面
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-12 17:41
 * ================================================
 */
public class TableManagerFragment extends BaseFFragment<TableManagerPresenter> implements TableManagerContract.View, TableExpandableItemAdapter.OnExpandViewChange {

    @BindView(R.id.table_rv)
    RecyclerView mTableRv;
    @BindView(R.id.table_refresh_layout)
    SmartRefreshLayout mTableRefreshLayout;

    private TableExpandableItemAdapter mTableExpandableItemAdapter;
    private TableType mTableNormalType;
    private TableType mTableRoomType;
    private List<ShopDataPackage.TableListBean> mTableList;
    private GridLayoutManager mManager;
    private boolean mTableNormalExpand, mTableRoomExpand;
    private int mType = TableTypeParam.TYPE_ALL;
    private boolean mShown;
    private ThreadUtils.Task mTask;

    @Override
    public void onPause() {
        super.onPause();
        mShown = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        mShown = true;
        mPresenter.networkUpdateTablesStatus(false);
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        mTableExpandableItemAdapter = new TableExpandableItemAdapter(generateData(), this);
        mManager = new GridLayoutManager(getContext(), 3);
        mTableExpandableItemAdapter.expandAll();
        mTableNormalExpand = true;
        mTableRoomExpand = true;
        mTableRefreshLayout.setEnableLoadMore(false);
        mTableRefreshLayout.setOnRefreshListener(refreshLayout -> {
            mPresenter.networkUpdateTablesStatus(true);
        });
        mTableRv.addItemDecoration(new SpacesItemDecoration(AdaptScreenUtils.pt2Px(20)));
        mTableRv.setLayoutManager(mManager);
        mTableRv.setAdapter(mTableExpandableItemAdapter);
        mManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mTableExpandableItemAdapter.getItemViewType(position) == TableExpandableItemAdapter.TYPE_TABLE_DETAIL ? 1 : mManager.getSpanCount();
            }
        });
        mTask = new ThreadUtils.SimpleTask<Object>() {
            @Override
            public Object doInBackground() {
                if (AppUtils.isAppForeground() && mShown) {
                    mPresenter.networkUpdateTablesStatus(true);
                }
                return null;
            }

            @Override
            public void onSuccess(Object result) {

            }
        };
        ThreadUtils.executeByFixedAtFixRate(5, mTask, 8, TimeUnit.SECONDS);
    }

    @Override
    public void showMessage(String message) {
        super.showMessage(message);
        if (mTableRefreshLayout != null) {
            mTableRefreshLayout.finishRefresh();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mTask != null) {
            ThreadUtils.cancel(mTask);
        }
    }


    private ArrayList<MultiItemEntity> generateData() {
        mTableList = new ArrayList<>();
        mTableList.clear();
        mTableList.addAll(LocalCacheUtils.getCacheTableList());

        ArrayList<MultiItemEntity> res = new ArrayList<>();
        mTableNormalType = new TableType("大厅");
        mTableRoomType = new TableType("包厢");

        res.add(mTableNormalType);
        res.add(mTableRoomType);
        filterTableList(TableTypeParam.TYPE_ALL);
        return res;
    }


    public static TableManagerFragment newInstance() {
        Bundle args = new Bundle();
        TableManagerFragment fragment = new TableManagerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_table_manager;
    }

    @Override
    protected void initInjector() {
        DaggerFragmentComponent.builder().
                applicationComponent(TApplication.getInstance().getApplicationComponent())
                .build().inject(this);
    }

    @Override
    protected void onEventComing(EventCenter eventCenter) {
        if (eventCenter.getEventCode() == EventBusCodes.FILTER_TABLE_LIST) {
            mTableList.clear();
            mTableList.addAll(LocalCacheUtils.getCacheTableList());
            filterTableList((int) eventCenter.getData());
            if (mTableExpandableItemAdapter != null) {
                mTableExpandableItemAdapter.collapse(mTableExpandableItemAdapter.getParentPosition(mTableNormalType));
                mTableExpandableItemAdapter.collapse(mTableExpandableItemAdapter.getParentPosition(mTableRoomType));
                mTableExpandableItemAdapter.expand(mTableExpandableItemAdapter.getParentPosition(mTableNormalType));
                mTableExpandableItemAdapter.expand(mTableExpandableItemAdapter.getParentPosition(mTableRoomType));
                mTableNormalExpand = true;
                mTableRoomExpand = true;
            }
        }
    }

    private void filterTableList(int type) {
        mType = type;
        mTableNormalType.clearSubItem();
        mTableRoomType.clearSubItem();
        for (int i = 0; i < mTableList.size(); i++) {
            ShopDataPackage.TableListBean table = mTableList.get(i);
            if (table.getTableType() == ShopDataPackage.TableListBean.TABLE_TYPE_NORMAL) {
                if (table.getOrderStatus() == ShopDataPackage.TableListBean.ORDER_STATUS_EMPTY) {
                    if (type == TableTypeParam.TYPE_CLOSE || type == TableTypeParam.TYPE_ALL) {
                        mTableNormalType.addSubItem(table);
                    }
                } else {
                    if (type == TableTypeParam.TYPE_OPEN || type == TableTypeParam.TYPE_ALL) {
                        mTableNormalType.addSubItem(table);
                    }
                }
            } else {
                if (table.getOrderStatus() == ShopDataPackage.TableListBean.ORDER_STATUS_EMPTY) {
                    if (type == TableTypeParam.TYPE_CLOSE || type == TableTypeParam.TYPE_ALL) {
                        mTableRoomType.addSubItem(table);
                    }
                } else {
                    if (type == TableTypeParam.TYPE_OPEN || type == TableTypeParam.TYPE_ALL) {
                        mTableRoomType.addSubItem(table);
                    }
                }
            }
        }

        if (mTableExpandableItemAdapter != null) {
            mTableExpandableItemAdapter.getData().clear();
            mTableExpandableItemAdapter.getData().add(mTableNormalType);
            mTableExpandableItemAdapter.getData().add(mTableRoomType);
            mTableExpandableItemAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void updateTablesStatusOk(List<ShopDataPackage.TableListBean> tableList) {
        if (mTableRefreshLayout != null) {
            mTableRefreshLayout.finishRefresh();
        }
        mTableList.clear();
        mTableList.addAll(tableList);
        filterTableList(mType);
        if (mTableExpandableItemAdapter != null) {
            if (mTableNormalExpand) {
                mTableExpandableItemAdapter.collapse(mTableExpandableItemAdapter.getParentPosition(mTableNormalType));
                mTableExpandableItemAdapter.expand(mTableExpandableItemAdapter.getParentPosition(mTableNormalType));
            }
            if (mTableRoomExpand) {
                mTableExpandableItemAdapter.collapse(mTableExpandableItemAdapter.getParentPosition(mTableRoomType));
                mTableExpandableItemAdapter.expand(mTableExpandableItemAdapter.getParentPosition(mTableRoomType));
            }
        }
    }

    @Override
    public void onChange(MultiItemEntity item, boolean isExpand) {
        if ("大厅".equals(((TableType) item).name)) {
            mTableNormalExpand = isExpand;
        } else {
            mTableRoomExpand = isExpand;
        }
    }

    @Override
    public void OnClickTable(ShopDataPackage.TableListBean table) {
        if (ButtonUtils.isFastDoubleClick(-1, 500)) {
            return;
        }
        switch (table.getOrderStatus()) {
            case ShopDataPackage.TableListBean.ORDER_STATUS_EMPTY:
                showCreateOrderDg(table);
                break;
            case ShopDataPackage.TableListBean.ORDER_STATUS_CREATE:
                goChooseProduct(table);
                break;
            case ShopDataPackage.TableListBean.ORDER_STATUS_WAIT_CONFIRM:
                showMessage("此桌处于未审核状态，请先到收银台审核订单");
                break;
            case ShopDataPackage.TableListBean.ORDER_STATUS_WAIT_BILL:
                goTableConsolePage(table);
                break;
            case ShopDataPackage.TableListBean.ORDER_STATUS_BILLING:
                showMessage("此桌处于结算状态，请先到收银台处理支付");
                break;
            default:
                break;
        }
    }

    private void goChooseProduct(ShopDataPackage.TableListBean table) {
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.BundleCode.ORDER_NO, table.getCurrentOrder());
        bundle.putString(AppConstants.BundleCode.TABLE_NAME, table.getName());
        readyGo(ChooseProductActivity.class, bundle);
    }

    private void goTableConsolePage(ShopDataPackage.TableListBean table) {
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.BundleCode.ORDER_NO, table.getCurrentOrder());
        bundle.putString(AppConstants.BundleCode.TABLE_NAME, table.getName());
        readyGo(TableConsoleActivity.class, bundle);
    }

    private void showCreateOrderDg(ShopDataPackage.TableListBean table) {
        CreateOrderDg createOrderDg = new CreateOrderDg();
        createOrderDg.setData(table);
        createOrderDg.show(getSupportFragmentManager(), CreateOrderDg.class.getSimpleName());
    }
}
