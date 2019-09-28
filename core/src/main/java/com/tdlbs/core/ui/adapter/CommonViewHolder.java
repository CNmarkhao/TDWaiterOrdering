package com.tdlbs.core.ui.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.tdlbs.core.R;


public class CommonViewHolder extends RecyclerView.ViewHolder {
  private SparseArray<View> mViews;
  private View mConvertView;
  private Context mContext;


  public CommonViewHolder(Context context, View itemView, ViewGroup parent) {
    super(itemView);
    mContext = context;
    mConvertView = itemView;
    mViews = new SparseArray<View>();
  }


  public static CommonViewHolder get(Context context, ViewGroup parent, int layoutId) {

    View itemView = LayoutInflater.from(context).inflate(layoutId, parent,
        false);
    CommonViewHolder holder = new CommonViewHolder(context, itemView, parent);
    return holder;
  }


  /**
   * 通过viewId获取控件
   *
   * @param viewId
   * @return
   */
  public <T extends View> T getView(int viewId) {
    View view = mViews.get(viewId);
    if (view == null) {
      view = mConvertView.findViewById(viewId);
      mViews.put(viewId, view);
    }
    return (T) view;
  }

  /**
   * 扩展方法
   */
  public CommonViewHolder setText(int viewId, String text) {
    TextView tv = getView(viewId);
    tv.setText(text);
    return this;
  }

  public CommonViewHolder setImageResource(int viewId, int resId) {
    ImageView view = getView(viewId);
    view.setImageResource(resId);
    return this;
  }

  public CommonViewHolder setImageResourceWithGlide(int viewId, String url) {
    ImageView view = getView(viewId);
    Glide.with(mContext)
        .load(url)
        .transition(new DrawableTransitionOptions().crossFade(200))
        .apply(new RequestOptions().error(R.drawable.image_loading))
        .into(view);
    return this;
  }

  public CommonViewHolder setOnClickListener(int viewId,
                                             View.OnClickListener listener) {
    View view = getView(viewId);
    view.setOnClickListener(listener);
    return this;
  }
}
