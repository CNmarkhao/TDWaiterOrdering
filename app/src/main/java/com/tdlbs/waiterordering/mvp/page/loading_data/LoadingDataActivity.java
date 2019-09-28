package com.tdlbs.waiterordering.mvp.page.loading_data;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.tdlbs.waiterordering.R;
import com.tdlbs.waiterordering.app.TApplication;
import com.tdlbs.waiterordering.app.base.BaseActivity;
import com.tdlbs.waiterordering.di.component.DaggerActivityComponent;
import com.tdlbs.waiterordering.di.module.ActivityModule;
import com.tdlbs.waiterordering.mvp.bean.model.ProgressMessage;
import com.tdlbs.waiterordering.mvp.page.main.MainActivity;

import butterknife.BindView;

/**
 * ================================================
 * 更新服务器数据页
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-06-11 20:36
 * ================================================
 */
public class LoadingDataActivity extends BaseActivity<LoadingDataPresenter> implements LoadingDataContract.View {
    private final static int PROGRESS_COMPLETED = 100;

    @BindView(R.id.bg_fl)
    FrameLayout mBgFl;
    @BindView(R.id.update_pb)
    NumberProgressBar mUpdatePb;
    @BindView(R.id.update_tv)
    TextView mUpdateTv;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_loading_data;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        mBgFl.setBackground(new BitmapDrawable(getResources(),
                ImageUtils.fastBlur(ImageUtils.getBitmap(R.mipmap.app_bg), 0.9f, 15, true)));

        mPresenter.networkSyncShopData();
    }

    @Override
    protected void initInjector() {
        DaggerActivityComponent.builder().
                applicationComponent(TApplication.getInstance().getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build().inject(this);
    }

    @Override
    public void updateProgress(ProgressMessage msg) {
        runOnUiThread(() -> {
            mUpdateTv.setText(msg.getMessage());
            mUpdatePb.setProgress(msg.getProgress());
            if (msg.getProgress() == PROGRESS_COMPLETED) {
                readyGoThenKill(MainActivity.class);
            }
        });
    }

    @Override
    public void loadDataError(String msg) {
        showMessage(String.format("同步失败，即将退出程序。\n原因：%s", msg));
        mBgFl.postDelayed(AppUtils::exitApp, 4000);
    }
}
