package com.tdlbs.waiterordering.mvp.bean.model;

import lombok.Getter;

/**
 * ================================================
 * 数据更新进度信息
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-08 20:51
 * ================================================
 */
@Getter
public class ProgressMessage {
    int progress;
    String message;

    public ProgressMessage(int progress, String message) {
        this.progress = progress;
        this.message = message;
    }
}
