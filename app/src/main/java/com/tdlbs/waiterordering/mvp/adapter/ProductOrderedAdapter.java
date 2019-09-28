package com.tdlbs.waiterordering.mvp.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tdlbs.waiterordering.R;
import com.tdlbs.waiterordering.mvp.bean.model.OrderDetail;
import com.tdlbs.waiterordering.mvp.widget.BigValueAddSubView;

import java.util.List;
import java.util.Locale;

/**
 * ================================================
 * ProductOrderedAdapter
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-19 14:08
 * ================================================
 */
public class ProductOrderedAdapter extends BaseQuickAdapter<OrderDetail.Product, BaseViewHolder> {

    public interface OnOrderedListener {
        void onChangeNum();
    }

    OnOrderedListener mOnOrderedListener;

    public ProductOrderedAdapter(@Nullable List<OrderDetail.Product> data, OnOrderedListener listener) {
        super(R.layout.item_product_ordered, data);
        this.mOnOrderedListener = listener;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, OrderDetail.Product item) {
        helper.setIsRecyclable(false);
        helper.setText(R.id.product_name_tv, item.getProductName())
                .setText(R.id.product_memo_tv, item.getMemo())
                .setText(R.id.product_current_price_tv, String.format(Locale.CHINA, "￥%.2f", item.getCurrentPrice() * item.getProductCount()))
                .setImageResource(R.id.discount_iv, item.getDiscount() == 0 ? R.mipmap.ic_product_free : R.mipmap.ic_product_discount)
                .setGone(R.id.discount_iv, item.getDiscount() != 100)
                .setGone(R.id.product_memo_tv, !StringUtils.isEmpty(item.getMemo()));
        BigValueAddSubView bigValueAddSubView = helper.getView(R.id.nb_view);
        bigValueAddSubView.setValue(item.getProductCount());
        bigValueAddSubView.setOnValueChangeListener(var -> {
            if (mOnOrderedListener != null) {
                if (var == 0) {
                    getData().remove(helper.getAdapterPosition());
                }else {
                    item.setProductCount(var);
                }
                notifyDataSetChanged();
                mOnOrderedListener.onChangeNum();
            }
        });

    }
}
