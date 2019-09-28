package com.tdlbs.waiterordering.mvp.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tdlbs.waiterordering.R;
import com.tdlbs.waiterordering.mvp.bean.model.TableTypeParam;

import java.util.List;

/**
 * ================================================
 * FilterTableParamAdapter
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-14 14:53
 * ================================================
 */
public class FilterTableParamAdapter extends BaseQuickAdapter<TableTypeParam, BaseViewHolder> {
    public final static int DEFAULT_POSITION = 2;

    public int getCurrentPos() {
        return currentPos;
    }

    private int currentPos;

    public FilterTableParamAdapter(@Nullable List<TableTypeParam> data) {
        super(R.layout.item_filter_param, data);
        this.currentPos = DEFAULT_POSITION;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, TableTypeParam item) {
        boolean choosed = currentPos == helper.getAdapterPosition();

        helper.setText(R.id.filter_name_tv, item.getName())
                .setTextColor(R.id.filter_name_tv, helper.itemView.getResources().getColor(choosed ? R.color.colorPrimary : R.color.gray_4))
                .setBackgroundRes(R.id.filter_name_tv, choosed ? R.drawable.filter_choosed_bg
                        : R.drawable.filter_normal_bg);
    }

    public void setChoosedPosition(int currentPos) {
        this.currentPos = currentPos;
        notifyDataSetChanged();
    }
}
