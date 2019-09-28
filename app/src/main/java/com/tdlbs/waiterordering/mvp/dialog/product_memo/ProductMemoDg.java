package com.tdlbs.waiterordering.mvp.dialog.product_memo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.AdaptScreenUtils;
import com.tdlbs.core.ui.Item_decoration.GridSpacingItemDecoration;
import com.tdlbs.waiterordering.R;
import com.tdlbs.waiterordering.app.base.BaseDialog;
import com.tdlbs.waiterordering.app.utils.LocalCacheUtils;
import com.tdlbs.waiterordering.mvp.adapter.ProductMemoAdapter;
import com.tdlbs.waiterordering.mvp.bean.model.ProductMemoList;
import com.tdlbs.waiterordering.mvp.bean.model.ShopDataPackage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * ================================================
 * 商品备注对话框
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-19 09:17
 * ================================================
 */
public class ProductMemoDg extends BaseDialog {
    @BindView(R.id.dialog_title_tv)
    TextView mDialogTitleTv;
    @BindView(R.id.ll_product_demo)
    LinearLayout mLlProductDemo;
    @BindView(R.id.product_memo_tv)
    TextView mProductMemoTv;
    OnChoosedProductListener mOnChoosedProducListener;

    public interface OnChoosedProductListener {
        void chooseMemo(long productId, String memo);
    }

    private ArrayList<String> showMemos = new ArrayList<>();

    public ProductMemoDg(long mProductId, String mProductName, OnChoosedProductListener listener) {
        this.mProductName = mProductName;
        this.mProductId = mProductId;
        this.mOnChoosedProducListener = listener;
    }

    private String mProductName;
    private long mProductId;

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        mDialogTitleTv.setText(mProductName);
        List<ProductMemoList.ProductMemo> productMemo = LocalCacheUtils.getSingleMemo(mProductId);
        if (productMemo.size() == 0) {
            initCommonMemoLayout();
        } else {
            initProductMemoLayout(productMemo);
        }
    }

    /**
     * 解决Cancel，onResume再次显示问题
     */
    @Override
    public void onStop() {
        super.onStop();
        dismissAllowingStateLoss();
    }

    /**
     * 初始化单商品备注
     */
    private void initProductMemoLayout(List<ProductMemoList.ProductMemo> productMemo) {
        for (int i = 0; i < productMemo.size(); i++) {
            ProductMemoList.ProductMemo currentMemo = productMemo.get(i);
            View view = LayoutInflater.from(getContext().getApplicationContext()).inflate(R.layout.layout_product_demo, null);
            ((TextView) view.findViewById(R.id.tv_product_memo_title)).setText(currentMemo.getName());
            RecyclerView productDemoRv = view.findViewById(R.id.rv_product_demo);
            productDemoRv.setLayoutManager(new GridLayoutManager(getContext().getApplicationContext(), 3));
            productDemoRv.setHasFixedSize(true);
            productDemoRv.addItemDecoration(new GridSpacingItemDecoration(3, AdaptScreenUtils.pt2Px(15), true));
            ProductMemoAdapter productMemoAdapter = new ProductMemoAdapter(false);
            productMemoAdapter.setOnItemClickListener((adapter, view1, position) -> {
                if (productMemoAdapter.getClickPosition() > -1) {
                    showMemos.remove(productMemoAdapter.getData().get(productMemoAdapter.getClickPosition()));
                }
                if (productMemoAdapter.getClickPosition() == position) {
                    productMemoAdapter.setClickPosition(-1);
                } else {
                    productMemoAdapter.setClickPosition(position);
                }
                if (productMemoAdapter.getClickPosition() > -1) {
                    showMemos.add(productMemoAdapter.getData().get(productMemoAdapter.getClickPosition()));
                }
                setProductMemo();
            });
            productDemoRv.setAdapter(productMemoAdapter);
            List<String> memo = new ArrayList<>();
            try {
                memo.addAll(Arrays.asList(currentMemo.getMemos().split("#")));
            } catch (Exception e) {
                e.printStackTrace();
            }
            productMemoAdapter.setNewData(memo);

            mLlProductDemo.addView(view);
        }
    }

    /**
     * 初始化商品通用备注
     */
    private void initCommonMemoLayout() {
        View view = LayoutInflater.from(getContext().getApplicationContext()).inflate(R.layout.layout_product_demo, null);
        ((TextView) view.findViewById(R.id.tv_product_memo_title)).setText("");
        RecyclerView productDemoRv = view.findViewById(R.id.rv_product_demo);
        productDemoRv.setLayoutManager(new GridLayoutManager(getContext().getApplicationContext(), 3));
        productDemoRv.setHasFixedSize(true);
        productDemoRv.addItemDecoration(new GridSpacingItemDecoration(3, AdaptScreenUtils.pt2Px(15), true));
        ProductMemoAdapter productMemoAdapter = new ProductMemoAdapter(true);
        productMemoAdapter.setOnItemClickListener((adapter, view1, position) -> {
            List<Integer> choosed = productMemoAdapter.getClickPositionArr();
            if (choosed.contains(position)) {
                choosed.remove(Integer.valueOf(position));
            } else {
                choosed.add(position);
            }
            productMemoAdapter.setClickPositionArr(choosed);
            showMemos.clear();
            for (int i = 0; i < choosed.size(); i++) {
                showMemos.add(productMemoAdapter.getData().get(choosed.get(i)));
            }
            setProductMemo();
        });
        productDemoRv.setAdapter(productMemoAdapter);
        List<String> memo = new ArrayList<>();
        List<ShopDataPackage.MemoListBean> memoListBeans = LocalCacheUtils.getCacheCommonMemoList();
        if (memoListBeans != null) {
            for (int i = 0; i < memoListBeans.size(); i++) {
                memo.add(memoListBeans.get(i).getName());
            }
        }
        productMemoAdapter.setNewData(memo);
        mLlProductDemo.addView(view);
    }

    /**
     * 把选择的商品备注拼接起来展示到界面上
     * 商品备注信息会作为数量统计的因素，所以要进行排序
     */
    private void setProductMemo() {
        StringBuilder sb = new StringBuilder();
        Collections.sort(showMemos, String::compareTo);
        for (int i = 0, len = showMemos.size(); i < len; i++) {
            sb.append(showMemos.get(i));
            if (i < len - 1) {
                sb.append(",");
            }
        }
        mProductMemoTv.setText(sb.toString());
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.dialog_product_memo;
    }

    @OnClick({R.id.cancel_btn, R.id.confirm_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel_btn:
                dismissAllowingStateLoss();
                break;
            case R.id.confirm_btn:
                if (mOnChoosedProducListener != null) {
                    mOnChoosedProducListener.chooseMemo(mProductId, mProductMemoTv.getText().toString());
                }
                dismissAllowingStateLoss();
                break;
            default:
                break;
        }
    }
}
