package com.tdlbs.waiterordering.mvp.dialog.modifyPeople;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.KeyboardUtils;
import com.tdlbs.waiterordering.R;
import com.tdlbs.waiterordering.app.base.BaseDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * ================================================
 * 修改订单人数对话框
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-16 11:04
 * ================================================
 */
public class ModifyPeopleDg extends BaseDialog {
    @BindView(R.id.people_et)
    EditText mPeopleEt;

    OnModifyPeopleListener mOnModifyPeopleListener;

    public interface OnModifyPeopleListener {
        void modifySuccess(int peopleNum);
    }

    public ModifyPeopleDg(OnModifyPeopleListener listener) {
        this.mOnModifyPeopleListener = listener;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        setCancelable(false);

    }

    @Override
    public void onResume() {
        super.onResume();
        mPeopleEt.postDelayed(() -> {
            mPeopleEt.requestFocus();
            KeyboardUtils.showSoftInput(mPeopleEt);
        }, 300);
    }

    @OnClick({R.id.cancel_btn, R.id.confirm_btn})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.cancel_btn:
                dismissAllowingStateLoss();
                break;
            case R.id.confirm_btn:
                modifyPeople();
                break;
            default:
                break;
        }
    }

    /**
     * 修改订单人数
     */
    private void modifyPeople() {
        int peopleNum = -1;
        try {
            peopleNum = Integer.parseInt(mPeopleEt.getText().toString());
        } catch (Exception e) {

        }
        if (peopleNum == -1) {
            showMessage("请先输入人数");
            return;
        }
        if (mOnModifyPeopleListener != null) {
            mOnModifyPeopleListener.modifySuccess(peopleNum);
        }
        dismissAllowingStateLoss();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.dialog_motify_people;
    }
}
