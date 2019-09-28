package com.tdlbs.waiterordering.mvp.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tdlbs.waiterordering.R;
import com.tdlbs.waiterordering.mvp.bean.model.ShopDataPackage;

import java.util.List;

/**
 * ================================================
 * ConsoleTableAdapter
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-16 10:26
 * ================================================
 */
public class ConsoleTableAdapter extends BaseQuickAdapter<ShopDataPackage.TableListBean, BaseViewHolder> {
    private int currentPosition;

    public ConsoleTableAdapter(@Nullable List<ShopDataPackage.TableListBean> data) {
        super(R.layout.item_table_motify, data);
        currentPosition = -1;
    }

    public void resetData(List<ShopDataPackage.TableListBean> data) {
        currentPosition = -1;
        getData().clear();
        getData().addAll(data);
        notifyDataSetChanged();
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ShopDataPackage.TableListBean item) {
        boolean choosed = helper.getAdapterPosition() == currentPosition;
        helper.setText(R.id.table_name_tv, item.getName())
                .setTextColor(R.id.table_name_tv, helper.itemView.getResources().getColor(choosed ? R.color.colorPrimary : R.color.gray_4))
                .setVisible(R.id.table_choosed_iv, choosed);
        helper.itemView.setOnClickListener(v -> {
            currentPosition=helper.getAdapterPosition();
            notifyDataSetChanged();
        });
    }
}
