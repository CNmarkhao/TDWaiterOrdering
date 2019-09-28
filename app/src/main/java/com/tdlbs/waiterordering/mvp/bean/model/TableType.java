package com.tdlbs.waiterordering.mvp.bean.model;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.tdlbs.waiterordering.mvp.adapter.TableExpandableItemAdapter;

import java.util.ArrayList;

/**
 * ================================================
 * TableType
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-13 14:13
 * ================================================
 */
public class TableType extends AbstractExpandableItem<ShopDataPackage.TableListBean> implements MultiItemEntity {
    public TableType(String name) {
        this.name = name;
    }

    public String name;

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getItemType() {
        return TableExpandableItemAdapter.TYPE_LEVEL_0;
    }

    public void clearSubItem() {
        if (mSubItems != null) {
            mSubItems.clear();
            mSubItems = new ArrayList<>();
        }
    }
}
