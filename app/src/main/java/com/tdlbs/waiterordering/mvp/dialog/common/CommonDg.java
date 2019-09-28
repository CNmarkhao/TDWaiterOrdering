package com.tdlbs.waiterordering.mvp.dialog.common;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tdlbs.waiterordering.R;
import com.tdlbs.waiterordering.app.base.BaseDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * ================================================
 * 通用对话框
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-15 18:46
 * ================================================
 */
public class CommonDg extends BaseDialog {
    @BindView(R.id.dialog_title_tv)
    TextView mDialogTitleTv;
    @BindView(R.id.dialog_msg_tv)
    TextView mDialogMsgTv;

    private String mTitle;
    private String mMessage;
    private OnConfirmListener mOnConfirmListener;

    public interface OnConfirmListener {
        void confirm();

        void cancel();
    }

    public CommonDg(String mTitle, String mMessage, OnConfirmListener listener) {
        this.mTitle = mTitle;
        this.mMessage = mMessage;
        this.mOnConfirmListener = listener;
    }


    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        setCancelable(false);
        mDialogTitleTv.setText(mTitle);
        mDialogMsgTv.setText(mMessage);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.dialog_quit_login;
    }


    @OnClick({R.id.cancel_btn, R.id.confirm_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel_btn:
                dismiss();
                if (mOnConfirmListener != null) {
                    mOnConfirmListener.cancel();
                }
                break;
            case R.id.confirm_btn:
                dismiss();
                if (mOnConfirmListener != null) {
                    mOnConfirmListener.confirm();
                }
                break;
            default:
                break;
        }
    }

}
