package com.tdlbs.waiterordering.mvp.dialog.product_opt;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.tdlbs.waiterordering.R;
import com.tdlbs.waiterordering.app.base.BaseDialog;
import com.tdlbs.waiterordering.constant.AppConstants;
import com.tdlbs.waiterordering.mvp.bean.model.OrderDetail;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * ================================================
 * 商品操作对话框
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-16 14:47
 * ================================================
 */
public class ProductOptDg extends BaseDialog {
    @BindView(R.id.dialog_title_tv)
    TextView mDialogTitleTv;
    @BindView(R.id.discount_et)
    EditText mDiscountEt;
    @BindView(R.id.discount_fl)
    FrameLayout mDiscountFl;
    private String mProductName;
    private OrderDetail.Product mProduct;
    OnProductOptListener mOnProductOptListener;

    public interface OnProductOptListener {
        void discountProduct(int discount);

        void notifyProduct(int type, OrderDetail.Product product);
    }

    public ProductOptDg(String mProductName, OrderDetail.Product productId, OnProductOptListener listener) {
        this.mProductName = mProductName;
        this.mOnProductOptListener = listener;
        this.mProduct = productId;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        mDialogTitleTv.setText(String.format(Locale.CHINA, "操作-%s", mProductName));
    }

    @Override
    public void onStop() {
        super.onStop();
        dismissAllowingStateLoss();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.dialog_product_opt;
    }

    @OnClick({R.id.opt_discount_btn, R.id.discount_confirm_btn, R.id.opt_free_btn, R.id.opt_urge_btn, R.id.opt_wait_btn, R.id.opt_push_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.opt_discount_btn:
                toggleDiscountLayout();
                break;
            case R.id.discount_confirm_btn:
                confirmDiscount();
                break;
            case R.id.opt_free_btn:
                if (mOnProductOptListener != null) {
                    mOnProductOptListener.discountProduct(0);
                }
                dismissAllowingStateLoss();
                break;
            case R.id.opt_urge_btn:
                if (mOnProductOptListener != null) {
                    showMessage("已通知厨房催菜");
                    mOnProductOptListener.notifyProduct(AppConstants.NotifyProduct.URGE, mProduct);
                }
                dismissAllowingStateLoss();
                break;
            case R.id.opt_wait_btn:
                if (mOnProductOptListener != null) {
                    showMessage("已通知厨房暂不上菜");
                    mOnProductOptListener.notifyProduct(AppConstants.NotifyProduct.WAIT, mProduct);
                }
                dismissAllowingStateLoss();
                break;
            case R.id.opt_push_btn:
                if (mOnProductOptListener != null) {
                    showMessage("已通知厨房恢复上菜");
                    mOnProductOptListener.notifyProduct(AppConstants.NotifyProduct.PUSH, mProduct);
                }
                dismissAllowingStateLoss();
                break;
            default:
                break;
        }
    }

    /**
     * 对商品进行打折
     */
    private void confirmDiscount() {
        int discount = -1;
        try {
            discount = Integer.parseInt(mDiscountEt.getText().toString());
        } catch (Exception e) {

        }
        if (discount == -1 || discount > 100) {
            showMessage("请正确输入折扣率");
            return;
        }
        if (mOnProductOptListener != null) {
            mOnProductOptListener.discountProduct(discount);
        }
        dismissAllowingStateLoss();
    }

    private void toggleDiscountLayout() {
        if (mDiscountFl.getVisibility() == View.VISIBLE) {
            mDiscountFl.setVisibility(View.GONE);
            KeyboardUtils.hideSoftInput(mDiscountEt);
        } else {
            mDiscountFl.setVisibility(View.VISIBLE);
            mDiscountEt.findFocus();
            KeyboardUtils.showSoftInput(mDiscountEt);
        }
    }
}
