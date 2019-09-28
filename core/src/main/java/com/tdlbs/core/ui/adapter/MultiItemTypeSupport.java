package com.tdlbs.core.ui.adapter;


public interface MultiItemTypeSupport<T> {

    int getLayoutId(int itemType);

    int getItemViewType(int position, T t);
}
