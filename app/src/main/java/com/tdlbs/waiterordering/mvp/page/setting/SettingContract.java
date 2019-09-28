package com.tdlbs.waiterordering.mvp.page.setting;

import com.tdlbs.core.ui.mvp.BaseContract;
import com.tdlbs.core.ui.mvp.BaseModel;

/**
 * ================================================
 * SettingContract
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-06-11 20:37
 * ================================================
 */
public interface SettingContract {
    interface Model extends BaseModel {

    }

    interface View extends BaseContract.BaseView {
        void saveSettingParam();

        void loadSettingParam();
    }

}
