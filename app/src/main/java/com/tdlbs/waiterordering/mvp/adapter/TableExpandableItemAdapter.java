package com.tdlbs.waiterordering.mvp.adapter;

import androidx.annotation.NonNull;

import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.tdlbs.waiterordering.R;
import com.tdlbs.waiterordering.mvp.bean.model.ShopDataPackage;
import com.tdlbs.waiterordering.mvp.bean.model.TableType;

import java.util.List;
import java.util.Locale;

/**
 * ================================================
 * TableExpandableItemAdapter
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-13 13:51
 * ================================================
 */
public class TableExpandableItemAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    public final static int TYPE_LEVEL_0 = 0;
    public final static int TYPE_TABLE_DETAIL = 1;
    private OnExpandViewChange mOnExpandViewChange;

    public interface OnExpandViewChange {
        void onChange(MultiItemEntity item, boolean isExpand);

        void OnClickTable(ShopDataPackage.TableListBean table);
    }

    public TableExpandableItemAdapter(List<MultiItemEntity> data, OnExpandViewChange change) {
        super(data);
        addItemType(TYPE_LEVEL_0, R.layout.item_table_type);
        addItemType(TYPE_TABLE_DETAIL, R.layout.item_table_detail);
        mOnExpandViewChange = change;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, MultiItemEntity item) {
        switch (holder.getItemViewType()) {
            case TYPE_LEVEL_0:
                TableType type = (TableType) item;
                holder.setText(R.id.table_type_tv, type.name)
                        .setTextColor(R.id.table_type_tv,holder.itemView.getResources().getColor(type.isExpanded() ?
                                R.color.gray_4:R.color.gray_2))
                        .setImageResource(R.id.table_expand_iv, type.isExpanded() ?
                                R.mipmap.ic_expand_up : R.mipmap.ic_expand_down);
                holder.itemView.setOnClickListener(v -> {
                    int pos = holder.getAdapterPosition();
                    if (type.isExpanded()) {
                        if (mOnExpandViewChange != null) {
                            mOnExpandViewChange.onChange(item, false);
                        }
                        collapse(pos, false);
                    } else {
                        if (mOnExpandViewChange != null) {
                            mOnExpandViewChange.onChange(item, true);
                        }
                        expand(pos, false);
                    }
                });
                break;
            case TYPE_TABLE_DETAIL:
                ShopDataPackage.TableListBean table = (ShopDataPackage.TableListBean) item;
                holder.setText(R.id.table_name_tv, table.getName())
                        .setText(R.id.order_status_tv, getTableStatusName(table))
                        .setImageResource(R.id.order_status_iv, getTableStatusIcon(table))
                        .setText(R.id.order_time_tv, getTableTime(table))
                        .setText(R.id.order_fee_tv, getTableFee(table));
                holder.itemView.setOnClickListener(v -> {
                    if (mOnExpandViewChange != null) {
                        mOnExpandViewChange.OnClickTable(table);
                    }
                });
                break;
            default:
                break;
        }
    }

    private String getTableFee(ShopDataPackage.TableListBean table) {
        if (table.getTotalPrice() != 0) {
            return String.format(Locale.CHINA, "￥%.2f", table.getTotalPrice());
        }
        return "";
    }

    private String getTableTime(ShopDataPackage.TableListBean table) {
        if (StringUtils.isEmpty(table.getCreateTime()) ||
                table.getOrderStatus() == ShopDataPackage.TableListBean.ORDER_STATUS_EMPTY ||
                table.getOrderStatus() == ShopDataPackage.TableListBean.ORDER_STATUS_WAIT_CONFIRM ||
                table.getOrderStatus() == ShopDataPackage.TableListBean.ORDER_STATUS_CREATE) {
            return "";
        }
        return String.format(Locale.CHINA, "%.2fh", -TimeUtils.getTimeSpanByNow(table.getCreateTime(), TimeConstants.MIN) / 60f);
    }

    private int getTableStatusIcon(ShopDataPackage.TableListBean bean) {
        int status;
        switch (bean.getOrderStatus()) {
            case ShopDataPackage.TableListBean.ORDER_STATUS_EMPTY:
                status = R.mipmap.ic_table_empty;
                break;
            case ShopDataPackage.TableListBean.ORDER_STATUS_CREATE:
                status = R.mipmap.ic_table_ordering;
                break;
            case ShopDataPackage.TableListBean.ORDER_STATUS_WAIT_CONFIRM:
                status = R.mipmap.ic_table_confirming;
                break;
            case ShopDataPackage.TableListBean.ORDER_STATUS_WAIT_BILL:
                status = R.mipmap.ic_table_eating;
                break;
            case ShopDataPackage.TableListBean.ORDER_STATUS_BILLING:
                status = R.mipmap.ic_table_payment;
                break;
            default:
                status = R.mipmap.ic_table_empty;
                break;
        }
        return status;
    }

    private String getTableStatusName(ShopDataPackage.TableListBean bean) {
        String status;
        switch (bean.getOrderStatus()) {
            case ShopDataPackage.TableListBean.ORDER_STATUS_EMPTY:
                status = "";
                break;
            case ShopDataPackage.TableListBean.ORDER_STATUS_CREATE:
                status = "开单中";
                break;
            case ShopDataPackage.TableListBean.ORDER_STATUS_WAIT_CONFIRM:
                status = "审核中";
                break;
            case ShopDataPackage.TableListBean.ORDER_STATUS_WAIT_BILL:
                status = "就餐中";
                break;
            case ShopDataPackage.TableListBean.ORDER_STATUS_BILLING:
                status = "结账中";
                break;
            default:
                status = "";
                break;
        }
        return status;
    }
}
