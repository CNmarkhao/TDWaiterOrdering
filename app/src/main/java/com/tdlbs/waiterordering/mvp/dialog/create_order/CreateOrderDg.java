package com.tdlbs.waiterordering.mvp.dialog.create_order;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.tdlbs.waiterordering.R;
import com.tdlbs.waiterordering.app.TApplication;
import com.tdlbs.waiterordering.app.base.BaseDialog;
import com.tdlbs.waiterordering.constant.AppConstants;
import com.tdlbs.waiterordering.di.component.DaggerDialogComponent;
import com.tdlbs.waiterordering.mvp.bean.entity.CreateOrderParam;
import com.tdlbs.waiterordering.mvp.bean.model.OrderDetail;
import com.tdlbs.waiterordering.mvp.bean.model.ShopDataPackage;
import com.tdlbs.waiterordering.mvp.page.order.choose_product.ChooseProductActivity;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * ================================================
 * 开单对话框
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-13 19:50
 * ================================================
 */
public class CreateOrderDg extends BaseDialog<CreateOrderPresenter> implements CreateOrderContract.View {
    @BindView(R.id.dialog_title_tv)
    TextView mDialogTitleTv;
    @BindView(R.id.people_et)
    EditText mPeopleEt;
    ShopDataPackage.TableListBean mTable;

    public void setData(ShopDataPackage.TableListBean table) {
        mTable = table;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        setCancelable(false);
        mDialogTitleTv.setText(String.format(Locale.CHINA, "开桌（%s：%s）",
                mTable.getTableType() == ShopDataPackage.TableListBean.TABLE_TYPE_NORMAL ? "大厅" : "包厢", mTable.getName()));
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.dialog_create_order;
    }


    @OnClick({R.id.cancel_btn, R.id.confirm_btn})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.cancel_btn:
                dismissAllowingStateLoss();
                break;
            case R.id.confirm_btn:
                createOrder();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //呼起软键盘
        mPeopleEt.postDelayed(() -> {
            mPeopleEt.requestFocus();
            KeyboardUtils.showSoftInput(mPeopleEt);
        }, 300);
    }

    private void createOrder() {
        int peopleNum = 1;
        try {
            peopleNum = Integer.parseInt(mPeopleEt.getText().toString());
        } catch (Exception e) {

        }
        mPresenter.networkCreateOrder(new CreateOrderParam(mTable.getId(), peopleNum));
    }

    /**
     * 开单成功
     * @param detail 订单详情
     */
    @Override
    public void createOrderOk(OrderDetail detail) {
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.BundleCode.ORDER_NO, detail.getOrderNo());
        bundle.putString(AppConstants.BundleCode.TABLE_NAME, mTable.getName());
        readyGo(ChooseProductActivity.class, bundle);
        dismissAllowingStateLoss();
    }

    @Override
    protected void initInjector() {
        DaggerDialogComponent.builder().
                applicationComponent(TApplication.getInstance().getApplicationComponent())
                .build().inject(this);
    }
}
