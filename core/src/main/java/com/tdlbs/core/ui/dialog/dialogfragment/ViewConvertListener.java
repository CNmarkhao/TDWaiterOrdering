package com.tdlbs.core.ui.dialog.dialogfragment;

import java.io.Serializable;

public interface ViewConvertListener extends Serializable {
    long serialVersionUID = System.currentTimeMillis();

    void convertView(ViewHolder holder, BaseDialogFragment dialog);
}
