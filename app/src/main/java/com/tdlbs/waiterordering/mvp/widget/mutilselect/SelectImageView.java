package com.tdlbs.waiterordering.mvp.widget.mutilselect;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.AdaptScreenUtils;

/**
 * ================================================
 * 对类的描述
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-20 16:59
 * ================================================
 */
@SuppressLint("AppCompatCustomView")
public class SelectImageView extends ImageView implements Checkable {
    private boolean isChecked;
    private int selectRes;
    private int noSelectRes;

    public SelectImageView(Context context) {
        super(context);
        init();
    }

    public SelectImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SelectImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        selectRes = android.R.drawable.radiobutton_on_background;
        noSelectRes = android.R.drawable.radiobutton_off_background;
    }

    public void setSelectRes(int selectRes) {
        if (selectRes == 0) {
            return;
        }
        this.selectRes = selectRes;
    }

    public void setNoSelectRes(int noSelectRes) {
        if (noSelectRes == 0) {
            return;
        }
        this.noSelectRes = noSelectRes;
    }

    @Override
    public void setChecked(boolean checked) {
        this.isChecked = checked;
        if (isChecked) {
            setImageResource(selectRes);
        } else {
            setImageResource(noSelectRes);
        }
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        if (isChecked) {
            isChecked = false;
        } else {
            isChecked = true;
        }
        setChecked(isChecked);
    }
}
