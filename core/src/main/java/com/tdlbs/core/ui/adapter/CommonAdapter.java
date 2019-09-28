package com.tdlbs.core.ui.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tdlbs.core.tool.LogUtil;

import java.util.List;

public abstract class CommonAdapter<T> extends RecyclerView.Adapter<CommonViewHolder> {
    private final String TAG = "CommonAdapter";

    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    protected OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener)
    {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    public CommonAdapter(Context context, int layoutId, List<T> datas) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;
    }

    /**
     * data的更新方法
     *
     * @param data
     */
    public void update(List<T> data) {
        if (data != null) {
            mDatas = data;
            this.notifyDataSetChanged();
        }
    }

    /**
     * data的增加方法
     *
     * @param data
     */
    public void add(List<T> data) {
        if (data != null) {
            mDatas.addAll(data);
            this.notifyDataSetChanged();
        }
    }

    @Override
    public CommonViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        CommonViewHolder viewHolder = CommonViewHolder.get(mContext, parent, mLayoutId);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        convert(holder, mDatas.get(position));
    }

    public abstract void convert(CommonViewHolder holder, T t);

    @Override
    public int getItemCount() {
        LogUtil.i(TAG, "getItemCount() = " + mDatas.size());
        return mDatas.size();
    }

}
