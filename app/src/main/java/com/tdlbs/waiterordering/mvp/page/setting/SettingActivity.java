package com.tdlbs.waiterordering.mvp.page.setting;

import android.os.Bundle;

import com.blankj.swipepanel.SwipePanel;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.tdlbs.waiterordering.R;
import com.tdlbs.waiterordering.app.base.BaseActivity;
import com.tdlbs.waiterordering.constant.SPConstants;
import com.tdlbs.waiterordering.mvp.widget.SwitchView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * ================================================
 * 设置界面
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-14 09:19
 * ================================================
 */
public class SettingActivity extends BaseActivity implements SettingContract.View {
    @BindView(R.id.show_pic_switch)
    SwitchView mShowPicSwitch;

    private boolean isShowPic;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        initSwipePanelView();
        loadSettingParam();
    }

    private void initSwipePanelView() {
        SwipePanel swipePanel = new SwipePanel(this);
        swipePanel.setLeftEdgeSize(SizeUtils.dp2px(100));
        swipePanel.setLeftDrawable(R.mipmap.base_back);
        swipePanel.wrapView(findViewById(R.id.content_ll));
        swipePanel.setOnFullSwipeListener(direction -> {

            finish();
            swipePanel.close(direction);
        });
    }

    @OnClick(R.id.left_menu_btn)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void saveSettingParam() {
        SPUtils.getInstance(SPConstants.FileName.SETTING).
                put(SPConstants.Setting.SHOW_PIC, mShowPicSwitch.isChecked());
    }

    @Override
    public void finish() {
        super.finish();
        saveSettingParam();
    }

    @Override
    public void loadSettingParam() {
        isShowPic = SPUtils.getInstance(SPConstants.FileName.SETTING).
                getBoolean(SPConstants.Setting.SHOW_PIC, true);

        mShowPicSwitch.setChecked(isShowPic);
    }
}
