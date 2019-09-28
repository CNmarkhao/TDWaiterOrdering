package com.tdlbs.core.ui.toastbar;


/**
 * ================================================
 * 对类的描述
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-06 16:36
 * ================================================
 */
public interface ToastListener {
    void beforeToastShow(Toast toast);

    void afterToastShow(Toast toast);

    void beforeToastDismiss(Toast toast);

    void afterToastDismiss(Toast toast);
}