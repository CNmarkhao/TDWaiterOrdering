package com.tdlbs.waiterordering.mvp.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tdlbs.waiterordering.R;
import com.tdlbs.waiterordering.mvp.bean.model.ShopDataPackage;
import com.tdlbs.waiterordering.mvp.widget.BadgeView;

import java.util.List;

/**
 * ================================================
 * ProductTypeAdapter
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-17 15:17
 * ================================================
 */
public class ProductTypeAdapter extends BaseQuickAdapter<ShopDataPackage.ProductCategoryListBean, BaseViewHolder> {
    private int currentPosition;

    public ProductTypeAdapter(@Nullable List<ShopDataPackage.ProductCategoryListBean> data) {
        super(R.layout.item_product_type, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ShopDataPackage.ProductCategoryListBean item) {
        boolean choosed = currentPosition == helper.getAdapterPosition();
        helper.setTextColor(R.id.type_name_tv, helper.itemView.getResources().getColor(choosed ? R.color.gray_4 : R.color.gray_2))
                .setText(R.id.type_name_tv, item.getName())
                .setBackgroundColor(R.id.type_name_tv, helper.itemView.getResources().getColor(choosed ? R.color.white : R.color.white_slight));

        ((BadgeView) helper.getView(R.id.order_bag_num_bv)).showBadge(item.getLocalCount() == 0 ? null : String.valueOf(item.getLocalCount()));
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
        notifyDataSetChanged();
    }
}
