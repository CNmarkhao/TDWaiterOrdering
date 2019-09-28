package com.tdlbs.waiterordering.mvp.adapter;

import android.graphics.Paint;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tdlbs.waiterordering.R;
import com.tdlbs.waiterordering.mvp.bean.model.OrderDetail;

import java.util.List;
import java.util.Locale;

/**
 * ================================================
 * ConsoleProductAdapter
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-15 19:21
 * ================================================
 */
public class ConsoleProductAdapter extends BaseQuickAdapter<OrderDetail.Product, BaseViewHolder> {
    public ConsoleProductAdapter(@Nullable List<OrderDetail.Product> data) {
        super(R.layout.item_console_product, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, OrderDetail.Product item) {
        TextView tv = helper.getView(R.id.product_original_price_tv);
        tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        helper.setText(R.id.product_name_tv, item.getProductName())
                .setText(R.id.product_memo_tv, item.getMemo())
                .setText(R.id.product_num_tv, String.format(Locale.CHINA, "x%d", item.getProductCount()))
                .setText(R.id.product_original_price_tv, String.format(Locale.CHINA, "￥%.2f", item.getOriginalPrice() * item.getProductCount()))
                .setText(R.id.product_current_price_tv, String.format(Locale.CHINA, "￥%.2f", item.getCurrentPrice() * item.getProductCount()))
                .setImageResource(R.id.discount_iv, item.getDiscount() == 0 ? R.mipmap.ic_product_free : R.mipmap.ic_product_discount)
                .setGone(R.id.product_original_price_tv, item.getDiscount() != 100)
                .setGone(R.id.discount_iv, item.getDiscount() != 100)
                .setGone(R.id.product_memo_tv, !StringUtils.isEmpty(item.getMemo()));
    }
}
