package com.tdlbs.waiterordering.mvp.adapter;

import android.graphics.Color;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tdlbs.waiterordering.R;

import java.util.ArrayList;
import java.util.List;

/**
 * ================================================
 * ProductMemoAdapter
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-19 10:20
 * ================================================
 */
public class ProductMemoAdapter  extends BaseQuickAdapter<String, BaseViewHolder> {
    public int getClickPosition() {
        return mClickPosition;
    }

    private int mClickPosition;
    boolean mIsMutiChoose;

    public List<Integer> getClickPositionArr() {
        return mClickPositionArr;
    }

    public void setClickPositionArr(List<Integer> mClickPositionArr) {
        this.mClickPositionArr = mClickPositionArr;
        notifyDataSetChanged();
    }

    private List<Integer> mClickPositionArr;

    public ProductMemoAdapter(boolean isMutiChoose) {
        super(R.layout.item_product_memo);
        this.mClickPosition = -1;
        this.mIsMutiChoose = isMutiChoose;
        mClickPositionArr = new ArrayList<>();
    }

    public void setClickPosition(int mClickPosition) {
        this.mClickPosition = mClickPosition;
        notifyDataSetChanged();
    }


    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_product_memo, item);
        if (mIsMutiChoose) {
            if (mClickPositionArr.contains(helper.getAdapterPosition())) {
                helper.setBackgroundRes(R.id.tv_product_memo, R.drawable.bg_product_memo_select);
                ((TextView) helper.getView(R.id.tv_product_memo)).setTextColor(Color.parseColor("#006aff"));
            } else {
                helper.setBackgroundRes(R.id.tv_product_memo, R.drawable.bg_product_memo_normal);
                ((TextView) helper.getView(R.id.tv_product_memo)).setTextColor(Color.parseColor("#000000"));
            }
            return;
        }
        if (helper.getAdapterPosition() == mClickPosition) {
            helper.setBackgroundRes(R.id.tv_product_memo, R.drawable.bg_product_memo_select);
            ((TextView) helper.getView(R.id.tv_product_memo)).setTextColor(Color.parseColor("#006aff"));
        } else {
            helper.setBackgroundRes(R.id.tv_product_memo, R.drawable.bg_product_memo_normal);
            ((TextView) helper.getView(R.id.tv_product_memo)).setTextColor(Color.parseColor("#000000"));
        }
    }
}
