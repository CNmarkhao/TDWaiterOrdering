package com.tdlbs.core.ui.dialog.dialogactivity;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tdlbs.core.R;


public class LoadingDialog extends Dialog implements View.OnClickListener {
    TextView mCountDownTv;
    CountDownTimer mTimer;
    private final static String TAG = "LoadingDialog";
    private TextView mTipTxt;
    private String mTip;
    private Context loadingDialogContext;

    public LoadingDialog(Context context) {
        super(context, R.style.LoadingDialogStyle);
        loadingDialogContext = context;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        mTipTxt = findViewById(R.id.tipTextView);
        if(!TextUtils.isEmpty(mTip)){
            mTipTxt.setText(mTip);
        }
        mTimer = new CountDownTimer(120000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (mCountDownTv != null) {
                    mCountDownTv.setText(millisUntilFinished / 1000 + "秒后退出");
                }
            }

            @Override
            public void onFinish() {
                dismiss();
            }
        };
    }

    public void setTip(String tip){
        mTip = tip;
        if (mTipTxt!=null)
        mTipTxt.setText(mTip);
    }

    public void onDismiss() {
        if (this.isShowing()) {
            this.dismiss();
        }
    }
    @Override
    public void show() {
        super.show();
        if (mCountDownTv == null) {
            mCountDownTv = findViewById(R.id.text_count_down);
        }
        mTimer.cancel();
        mTimer.start();
    }


    @Override
    public void dismiss() {
        super.dismiss();
        mTimer.cancel();
    }

    public Context getLoadingDialogContext() {
        return loadingDialogContext;
    }

    @Override
    public void onClick(View v) {
        onDismiss();
    }

    public interface LoadingDialogCallback {
        void onDialogCallback(int type, String id);
    }
}
