package com.tdlbs.waiterordering.mvp.adapter;

import android.graphics.Paint;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tdlbs.waiterordering.R;
import com.tdlbs.waiterordering.app.utils.LocalCacheUtils;
import com.tdlbs.waiterordering.mvp.bean.model.ShopDataPackage;
import com.tdlbs.waiterordering.mvp.widget.BadgeView;

import java.util.List;
import java.util.Locale;

/**
 * ================================================
 * ProductTextAdapter
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-17 16:25
 * ================================================
 */
public class ProductImageAdapter extends ProductTextAdapter {
    public ProductImageAdapter(@Nullable List<ShopDataPackage.ProductListBean> data) {
        super(R.layout.item_product_image, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ShopDataPackage.ProductListBean item) {
        TextView tv = helper.getView(R.id.product_original_price_tv);
        tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        helper.setText(R.id.product_name_tv, item.getName())
                .setText(R.id.product_original_price_tv, String.format(Locale.CHINA, "￥%.2f", item.getPrice()))
                .setText(R.id.product_current_price_tv, String.format(Locale.CHINA, "￥%.2f", item.getPrice() * item.getDiscount() * 0.01))
                .setImageResource(R.id.discount_iv, item.getDiscount() == 0 ? R.mipmap.ic_product_free : R.mipmap.ic_product_discount)
                .setGone(R.id.product_original_price_tv, item.getDiscount() != 100)
                .setGone(R.id.discount_iv, item.getDiscount() != 100);

        RequestOptions requestOptions = new RequestOptions()
                .circleCrop()
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.image_loading);
        ((BadgeView) helper.getView(R.id.order_bag_num_bv)).showBadge(item.getLocalCount() == 0 ? null : String.valueOf(item.getLocalCount()));
        ImageView productIv = helper.getView(R.id.product_iv);
        String url = LocalCacheUtils.getImageHost() + item.getPicPath();
        if (StringUtils.isEmpty(LocalCacheUtils.getImageHost()) || StringUtils.isEmpty(item.getPicPath())) {
            return;
        }
        Glide.with(productIv.getContext()).load(url).apply(requestOptions).into(productIv);
    }


}
