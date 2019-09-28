package com.tdlbs.waiterordering.mvp.page.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.gprinter.aidl.GpService;
import com.gprinter.command.GpCom;
import com.gprinter.service.GpPrintService;
import com.tdlbs.core.eventbus.EventCenter;
import com.tdlbs.printer.receiver.PrintEventReceiver;
import com.tdlbs.printer.util.FormatPrintContent;
import com.tdlbs.printer.util.PrinterUtils;
import com.tdlbs.update.OnDownloadListener;
import com.tdlbs.update.UpdateManager;
import com.tdlbs.waiterordering.R;
import com.tdlbs.waiterordering.app.TApplication;
import com.tdlbs.waiterordering.app.base.fragmentation.BaseFActivity;
import com.tdlbs.waiterordering.app.utils.ButtonUtils;
import com.tdlbs.waiterordering.app.utils.LocalCacheUtils;
import com.tdlbs.waiterordering.app.utils.RequestUtils;
import com.tdlbs.waiterordering.constant.AppConstants;
import com.tdlbs.waiterordering.constant.EventBusCodes;
import com.tdlbs.waiterordering.di.component.DaggerActivityComponent;
import com.tdlbs.waiterordering.di.module.ActivityModule;
import com.tdlbs.waiterordering.mvp.adapter.FilterTableParamAdapter;
import com.tdlbs.waiterordering.mvp.bean.model.PrintCommand;
import com.tdlbs.waiterordering.mvp.bean.model.ShopDataPackage;
import com.tdlbs.waiterordering.mvp.bean.model.TableTypeParam;
import com.tdlbs.waiterordering.mvp.dialog.quit.QuitLoginDg;
import com.tdlbs.waiterordering.mvp.page.login.LoginActivity;
import com.tdlbs.waiterordering.mvp.page.order.table_manager.TableManagerFragment;
import com.tdlbs.waiterordering.mvp.page.setting.SettingActivity;
import com.tdlbs.waiterordering.mvp.widget.menu.CoordinatorMenuView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import me.yokeyword.fragmentation.ISupportFragment;

/**
 * ================================================
 * 服务员点餐主界面
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-08 19:08
 * ================================================
 */
public class MainActivity extends BaseFActivity<MainPresenter> implements MainContract.View {
    public static final int FIRST = 0;
    @BindView(R.id.menu_view)
    CoordinatorMenuView menuView;
    @BindView(R.id.shop_name_tv)
    TextView mShopNameTv;
    @BindView(R.id.staff_name_tv)
    TextView mStaffNameTv;
    @BindView(R.id.menu_version_tv)
    TextView mMenuVersionTv;
    @BindView(R.id.filter_table_fl)
    FrameLayout filterTableFl;
    @BindView(R.id.filter_param_rv)
    RecyclerView filterParamRv;

    private FilterTableParamAdapter mFilterTableParamAdapter;
    private ISupportFragment[] mFragments = new ISupportFragment[2];
    private PrintEventReceiver mPrintReceiver;
    private GpService mGpService = null;
    private PrinterServiceConnection mPrinterServiceConnection = null;
    private PrinterUtils mPrinterUtils;
    private long mPressedTime = 0;

    public class PrinterServiceConnection implements ServiceConnection {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mPrinterUtils = null;
            mGpService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mGpService = GpService.Stub.asInterface(service);
            mPrinterUtils = new PrinterUtils(getApplicationContext(), mGpService);
        }
    }

    /**
     * 接受商品打印命令，并处理
     */
    @Override
    protected void onEventComing(EventCenter eventCenter) {
        if (eventCenter.getEventCode() == EventBusCodes.PRINT_PRODUCT) {
            if (mPrinterUtils == null) {
                ToastUtils.showShort("当前打印服务不可用，请重新登录");
                return;
            }
            PrintCommand printCommand = (PrintCommand) eventCenter.getData();
            ShopDataPackage.ProductListBean productListBean = LocalCacheUtils.getProduct(printCommand.getProduct().getProductId());
            if (productListBean.getGroupFlag() == 0) {
                sendPrintCommand(false, productListBean, printCommand);
            } else {
                List<ShopDataPackage.ProductListBean> groupProductBean = LocalCacheUtils.getProductGroup(printCommand.getProduct().getProductId());
                for (ShopDataPackage.ProductListBean item : groupProductBean) {
                    sendPrintCommand(true, item, printCommand);
                }
            }
        }
    }

    @Override
    public void onBackPressedSupport() {
        long mNowTime = System.currentTimeMillis();
        if ((mNowTime - mPressedTime) > 2000) {
            showMessage("再按一次退出程序");
            mPressedTime = mNowTime;
        } else {
            finish();
            AppUtils.exitApp();
        }
    }

    /**
     * 执行商品打印命令
     */
    private void sendPrintCommand(boolean isGroup, ShopDataPackage.ProductListBean item, PrintCommand command) {
        ShopDataPackage.PrinterListBean printer = LocalCacheUtils.getPrinter(command.getProduct().getPrinterId());
        if (printer == null) {
            return;
        }
        ArrayList<FormatPrintContent> contentList = new ArrayList<>();
        String title = "";
        if (command.getPrintType() == AppConstants.NotifyProduct.PUSH) {
            title = "恢复上菜单";
        }
        if (command.getPrintType() == AppConstants.NotifyProduct.URGE) {
            title = "催菜单";
        }
        if (command.getPrintType() == AppConstants.NotifyProduct.WAIT) {
            title = "暂不上菜单";
        }
        String productName = item.getName();
        int productCount = command.getProduct().getProductCount();
        if (isGroup) {
            productName = "  -" + item.getName() + "(套)";
        } else {
            productCount = command.getProduct().getProductCount() * item.getGroupNum();
        }
        contentList.add(mPrinterUtils.create(title, true, true, true, FormatPrintContent.Justification.CENTER, 0, 0, false));
        contentList.add(mPrinterUtils.create(command.getTableName(), true, true, true, FormatPrintContent.Justification.CENTER, 0, 0, false));
        contentList.add(mPrinterUtils.create(productName + String.format("%4s", "") + productCount + item.getUnitName(), true, true, true, FormatPrintContent.Justification.LEFT, 0, 0, false));
        contentList.add(mPrinterUtils.create("备注：", false, false, false, FormatPrintContent.Justification.LEFT, 0, 0, false));
        contentList.add(mPrinterUtils.create(command.getProduct().getMemo(), true, true, true, FormatPrintContent.Justification.LEFT, 0, 0, false));
        contentList.add(mPrinterUtils.create("打印时间：" + TimeUtils.getNowString(), false, false, true, FormatPrintContent.Justification.LEFT, 0, 0, false));
        contentList.add(mPrinterUtils.create("", false, false, true, FormatPrintContent.Justification.LEFT, 4, 0, true));
        mPrinterUtils.print(printer.getPrintIndex(), printer.getIpAddr(), printer.getPort(), contentList);
    }


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        initFragment();
        initShowInfo();
        initFilterLayout();
        closePrintService();
        openPrintService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closePrintService();
    }

    private void closePrintService() {
        if (mPrinterServiceConnection != null) {
            unbindService(mPrinterServiceConnection);
        }
        if (mPrintReceiver != null) {
            unregisterReceiver(mPrintReceiver);
        }
    }

    private void openPrintService() {
        mPrinterServiceConnection = new MainActivity.PrinterServiceConnection();
        Intent intent = new Intent(this, GpPrintService.class);
        bindService(intent, mPrinterServiceConnection, Context.BIND_AUTO_CREATE);
        mPrintReceiver = new PrintEventReceiver();
        registerReceiver(mPrintReceiver, new IntentFilter(GpCom.ACTION_DEVICE_REAL_STATUS));
    }

    private void initFilterLayout() {
        List<TableTypeParam> data = new ArrayList<>();
        data.add(new TableTypeParam(TableTypeParam.TYPE_CLOSE, "未开单"));
        data.add(new TableTypeParam(TableTypeParam.TYPE_OPEN, "已开单"));
        data.add(new TableTypeParam(TableTypeParam.TYPE_ALL, "全部"));

        mFilterTableParamAdapter = new FilterTableParamAdapter(data);
        filterParamRv.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        filterParamRv.setAdapter(mFilterTableParamAdapter);
        mFilterTableParamAdapter.setOnItemClickListener((adapter, view, position) -> mFilterTableParamAdapter.setChoosedPosition(position));
    }

    private void initShowInfo() {
        mShopNameTv.setText(RequestUtils.getShopInfo().getName());
        mStaffNameTv.setText(String.format(Locale.CHINA, "%s/%d", RequestUtils.getStaffName(),
                RequestUtils.getStaffId()));
        mMenuVersionTv.setText(String.format(Locale.CHINA, "版本号信息：V%s (%d)",
                AppUtils.getAppVersionName(), AppUtils.getAppVersionCode()));
    }

    private void initFragment() {
        ISupportFragment firstFragment = findFragment(TableManagerFragment.class);
        if (firstFragment == null) {
            mFragments[FIRST] = TableManagerFragment.newInstance();
            loadMultipleRootFragment(R.id.content_frame, FIRST,
                    mFragments[FIRST]);
        } else {
            mFragments[FIRST] = firstFragment;
        }
    }


    private void postFilterEvent() {
        filterTableFl.setVisibility(View.GONE);
        int tableType = mFilterTableParamAdapter.getData().get(mFilterTableParamAdapter.getCurrentPos()).getTableType();
        EventBus.getDefault().post(new EventCenter(EventBusCodes.FILTER_TABLE_LIST, new Integer(tableType)));
    }

    private void updateApp() {
        showLoading();
        UpdateManager.setDebuggable(false);
        UpdateManager.create(getApplicationContext())
                .setBaseUrl("http://172.16.0.70:8070")
                .setUrl("mis-center/sys/version/update")
                .setOnDownloadListener(new OnDownloadListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onProgress(int i) {

                    }

                    @Override
                    public void onFinish() {
                        hideLoading();
                    }
                })
                .setOnFailureListener(updateError -> {
                    showMessage(updateError.toString());
                    hideLoading();
                })
                .setVersionStatus(UpdateManager.UPDATE_STATUS_STABLE).check();
    }

    private void reloadData() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(loginIntent);
    }

    private void showQuitDg() {
        if (menuView.isOpened()) {
            QuitLoginDg mQuitLoginDg = new QuitLoginDg();
            mQuitLoginDg.show(getSupportFragmentManager(), QuitLoginDg.class.getSimpleName());
        }
    }

    @Override
    protected void initInjector() {
        DaggerActivityComponent.builder().
                applicationComponent(TApplication.getInstance().getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build().inject(this);
    }

    @OnClick({R.id.left_menu_btn, R.id.right_menu_btn, R.id.quit_login_btn, R.id.menu_setting_btn,
            R.id.menu_reload_btn, R.id.menu_update_btn, R.id.cancel_btn, R.id.confirm_btn})
    public void onViewClicked(View view) {
        if (ButtonUtils.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.quit_login_btn:
                showQuitDg();
                break;
            case R.id.menu_setting_btn:
                readyGo(SettingActivity.class);
                menuView.closeMenu();
                break;
            case R.id.menu_reload_btn:
                reloadData();
                break;
            case R.id.menu_update_btn:
                updateApp();
                break;
            case R.id.left_menu_btn:
                menuView.toogleMenu();
                break;
            case R.id.right_menu_btn:
                filterTableFl.setVisibility(View.VISIBLE);
                break;
            case R.id.cancel_btn:
                mFilterTableParamAdapter.setChoosedPosition(FilterTableParamAdapter.DEFAULT_POSITION);
                postFilterEvent();
                break;
            case R.id.confirm_btn:
                postFilterEvent();
                break;
            default:
                break;
        }
    }
}
