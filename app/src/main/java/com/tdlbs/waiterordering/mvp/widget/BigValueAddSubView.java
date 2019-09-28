package com.tdlbs.waiterordering.mvp.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.AdaptScreenUtils;
import com.tdlbs.waiterordering.R;


/**
 * ================================================
 * 创建时间：2018/9/12 10:31
 * 作者：markgu
 * 描述：展示 Model 的用法
 * Email：<a href="mailto:guhao561@gmail.com">Contact me</a>
 * ================================================
 */
public class BigValueAddSubView extends FrameLayout {
    protected int mDefaultValue;
    protected int mMinValue;
    protected int mMaxValue;
    protected int mCurrValue;
    protected boolean mIsListenValueChangeAfterOnAddOrSub;
    protected boolean mShouldListenFlag;
    protected boolean mIsOutReset;
    private ImageView mIvSub;
    private ImageView mIvAdd;
    private EditText mEtCount;

    private boolean mIgnore;
    private BigValueAddSubView.OnValueAddOrSubListener mOnValueAddOrSubListener;
    private BigValueAddSubView.OnValueChangeListener mOnValueChangeListener;

    public BigValueAddSubView(@NonNull Context context) {
        this(context, (AttributeSet) null);
    }

    public BigValueAddSubView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public BigValueAddSubView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mDefaultValue = 0;
        this.mMinValue = 1;
        this.mMaxValue = 9999;
        this.mCurrValue = this.mDefaultValue;
        this.mIsListenValueChangeAfterOnAddOrSub = true;
        this.mShouldListenFlag = true;
        this.mIgnore = false;
        this.mIsOutReset = false;
        this.initViews();
        this.init();
        this.initParams(context, attrs);
    }

    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(1, dpVal, context.getResources().getDisplayMetrics());
    }

    private void initViews() {
        View view = inflate(this.getContext(), R.layout.view_number_add_sub, this);
        this.mIvAdd = (ImageView) view.findViewById(R.id.iv_add);
        this.mIvSub = (ImageView) view.findViewById(R.id.iv_sub);
        this.mEtCount = (EditText) view.findViewById(R.id.et_count);

    }

    private void initParams(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ValueAddSubView);
        int textColor = typedArray.getColor(R.styleable.ValueAddSubView_vTextColor, 0);
        int textColorRes = typedArray.getResourceId(R.styleable.ValueAddSubView_vTextColor, 0);
        int textBgColor = typedArray.getColor(R.styleable.ValueAddSubView_vTextBg, 0);
        int textBgRes = typedArray.getResourceId(R.styleable.ValueAddSubView_vTextBg, 0);
        int defaultValue = typedArray.getInt(R.styleable.ValueAddSubView_vDefaultValue, 0);
        int max = typedArray.getInt(R.styleable.ValueAddSubView_vMaxValue, 99);
        int min = typedArray.getInt(R.styleable.ValueAddSubView_vMinValue, 0);
        int maxWidth = typedArray.getInt(R.styleable.ValueAddSubView_vTextMaxWidth, 0);
        int minWidth = typedArray.getInt(R.styleable.ValueAddSubView_vTextMinWidth, 0);
        float dividerWidth = typedArray.getDimension(R.styleable.ValueAddSubView_vDividerWidth, 0.0F);
        int dividerColor = typedArray.getColor(R.styleable.ValueAddSubView_vDividerColor, 0);
        float height = typedArray.getDimension(R.styleable.ValueAddSubView_vHeight, 0.0F);
        typedArray.recycle();
        if (textColor != 0) {
            mEtCount.setTextSize(dp2px(getContext(), 20));
            this.mEtCount.setTextColor(textColor);
        } else if (textColorRes != 0) {
            ColorStateList colorStateList = this.getResources().getColorStateList(textColorRes);
            this.mEtCount.setTextColor(colorStateList);
        }

        if (textBgColor != 0) {
            this.mEtCount.setBackgroundColor(textBgColor);
        } else if (textBgRes != 0) {
            this.mEtCount.setBackgroundResource(textBgRes);
        }
        mEtCount.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        this.mMaxValue = max;
        this.mMinValue = min;
        this.mDefaultValue = defaultValue;
        this.setValue(this.mDefaultValue, false);
        if (maxWidth != 0) {
            this.mEtCount.setMaxWidth(maxWidth);
        }

        if (minWidth != 0) {
            this.mEtCount.setMinWidth(minWidth);
        }

        ViewGroup.LayoutParams leftParams;
        ViewGroup.LayoutParams etParams;

        if (height != 0.0F) {
            etParams = (ViewGroup.LayoutParams) this.mEtCount.getLayoutParams();
            etParams.height = (int) (height + 0.5F);
            this.mEtCount.setLayoutParams(etParams);
            leftParams = (ViewGroup.LayoutParams) this.mIvAdd.getLayoutParams();
            leftParams.height = (int) (height + 0.5F);
            leftParams.width = (int) (height + 0.5F);
            this.mIvAdd.setLayoutParams(leftParams);
            ViewGroup.LayoutParams rightParams = (ViewGroup.LayoutParams) this.mIvSub.getLayoutParams();
            rightParams.height = (int) (height + 0.5F);
            rightParams.width = (int) (height + 0.5F);
            this.mIvSub.setLayoutParams(rightParams);
        }

        this.initIcon();
    }

    private void init() {

        OnClickListener mOnAddSubListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = BigValueAddSubView.this.mCurrValue;
                String format;
                if (v.getId() == R.id.iv_add) {
                    ++temp;
                    if (temp <= BigValueAddSubView.this.mMaxValue) {
                        ++BigValueAddSubView.this.mCurrValue;
                        format = Integer.toString(BigValueAddSubView.this.mCurrValue);
                        BigValueAddSubView.this.mEtCount.setText(format);
                        BigValueAddSubView.this.mEtCount.setSelection(format.length());
                        if (BigValueAddSubView.this.mOnValueAddOrSubListener != null) {
                            BigValueAddSubView.this.mOnValueAddOrSubListener.onValueAddOrSub(1);
                        }

                        BigValueAddSubView.this.initIcon();
                    } else {
                        BigValueAddSubView.this.showOutOfIndexMessage(true);
                    }
                } else {
                    --temp;
                    if (temp >= BigValueAddSubView.this.mMinValue) {
                        --BigValueAddSubView.this.mCurrValue;
                        format = Integer.toString(BigValueAddSubView.this.mCurrValue);
                        BigValueAddSubView.this.mEtCount.setText(format);
                        BigValueAddSubView.this.mEtCount.setSelection(format.length());
                        if (BigValueAddSubView.this.mOnValueAddOrSubListener != null) {
                            BigValueAddSubView.this.mOnValueAddOrSubListener.onValueAddOrSub(-1);
                        }

                        BigValueAddSubView.this.initIcon();
                    } else {
                        BigValueAddSubView.this.showOutOfIndexMessage(false);
                    }
                }

            }
        };
        this.mIvAdd.setOnClickListener(mOnAddSubListener);
        this.mIvSub.setOnClickListener(mOnAddSubListener);
        this.mEtCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                BigValueAddSubView.this.afterTextChange(s);
            }
        });
        this.mEtCount.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String trim = BigValueAddSubView.this.mEtCount.getText().toString().trim();
                    if (trim.isEmpty()) {
                        BigValueAddSubView.this.setValue(BigValueAddSubView.this.mDefaultValue);
                    } else if (BigValueAddSubView.this.parseString2Int(trim) < BigValueAddSubView.this.mMinValue) {
                        BigValueAddSubView.this.showOutOfIndexMessage(false);
                        BigValueAddSubView.this.setValue(BigValueAddSubView.this.mMinValue);
                    }
                }
            }
        });

        mEtCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                s.replace(mEtCount.getSelectionStart(), mEtCount.getSelectionEnd(), "");
            }
        });


    }

    protected void afterTextChange(Editable s) {
        if (!this.mIgnore) {
            String trim = s.toString().trim();
            if (!"".equals(trim)) {
                int strValue = this.parseString2Int(trim);
                if (strValue >= this.mMinValue) {
                    if (strValue == 0 && this.mMinValue > 0) {
                        this.setValue(this.mMinValue);
                        this.toChangeListener(this.mMinValue);
                    } else if (this.mIsOutReset && this.mCurrValue == strValue) {
                        this.mIsOutReset = false;
                    } else if (this.mIsListenValueChangeAfterOnAddOrSub && this.mCurrValue == strValue) {
                        this.toChangeListener(this.mCurrValue);
                    } else if (strValue > this.mMaxValue) {
                        this.mIsOutReset = true;
                        this.handleOut(this.mMaxValue, true);
                        this.toChangeListener(this.mMaxValue);
                    } else {
                        this.mCurrValue = strValue;
                        this.initIcon();
                        this.toChangeListener(strValue);
                    }
                }
            }
        }
    }

    private final void toChangeListener(int value) {
        if (this.mShouldListenFlag && this.mOnValueChangeListener != null) {
            this.mOnValueChangeListener.onValueChange(value);
        }

    }

    protected final void initIcon() {
        this.mIvAdd.setEnabled(this.mCurrValue + 1 <= this.mMaxValue);
        this.mIvSub.setEnabled(this.mCurrValue - 1 >= this.mMinValue);
    }

    private void handleOut(int value, boolean isOutOfMax) {
        this.showOutOfIndexMessage(isOutOfMax);
        this.setValueNoLogic(value);
    }

    protected void showOutOfIndexMessage(boolean isOutMax) {
        if (isOutMax) {
            Toast.makeText(this.getContext(), R.string.out_of_max, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this.getContext(), R.string.out_of_min, Toast.LENGTH_SHORT).show();
        }

    }

    public void setValue(int value, boolean shouldListen) {
        if (value < this.mMinValue) {
            value = this.mMinValue;
        } else if (value > this.mMaxValue) {
            value = this.mMaxValue;
        }

        this.mCurrValue = value;
        this.mShouldListenFlag = shouldListen;
        String valueStr = Integer.toString(this.mCurrValue);
        this.mEtCount.setText(valueStr);
        this.mEtCount.setSelection(valueStr.length());
        this.initIcon();
        this.mShouldListenFlag = true;
    }

    public void setValueHasIndex(int value) {
        if (value < this.mMinValue) {
            value = this.mMinValue;
            this.setValue(value);
        } else if (value > this.mMaxValue) {
            value = this.mMaxValue;
            this.setValue(value);
        } else {
            this.setValueNoLogic(value);
        }

    }

    public final void setValueNoLogic(int value) {
        if (value < this.mMinValue) {
            value = this.mMinValue;
        } else if (value > this.mMaxValue) {
            value = this.mMaxValue;
        }

        this.mCurrValue = value;
        this.mIgnore = true;
        String valueStr = Integer.toString(this.mCurrValue);
        this.mEtCount.setText(valueStr);
        this.mEtCount.setSelection(valueStr.length());
        this.initIcon();
        this.mIgnore = false;
    }

    public int getValue() {
        return this.mCurrValue;
    }

    public void setValue(int value) {
        this.setValue(value, true);
    }

    public int getMaxValue() {
        return this.mMaxValue;
    }

    public void setMaxValue(int max) {
        this.mMaxValue = max;
        this.initIcon();
    }

    public int getMinValue() {
        return this.mMinValue;
    }

    public void setMinValue(int min) {
        this.mMinValue = min;
        this.initIcon();
    }

    public void setDefaultValue(int defaultValue) {
        this.mDefaultValue = defaultValue;
        this.mEtCount.setText(Integer.toString(defaultValue));
    }

    public EditText getEditText() {
        return this.mEtCount;
    }

    public void setOnValueAddOrSubListener(BigValueAddSubView.OnValueAddOrSubListener listener) {
        this.mOnValueAddOrSubListener = listener;
    }

    public void setOnValueChangeListener(BigValueAddSubView.OnValueChangeListener listener) {
        this.mOnValueChangeListener = listener;
    }

    public void isListenValueChangeOnAddOrSub(boolean isListen) {
        this.mIsListenValueChangeAfterOnAddOrSub = isListen;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.mIvSub.setEnabled(enabled);
        this.mIvAdd.setEnabled(enabled);
        this.mEtCount.setEnabled(enabled);
    }

    /**
     * @deprecated
     */
    @Deprecated
    private void setViewGroupChildsEnable(ViewGroup viewGroup, boolean enable) {
        for (int i = 0; i < viewGroup.getChildCount(); ++i) {
            View v = viewGroup.getChildAt(i);
            if (v instanceof ViewGroup) {
                this.setViewGroupChildsEnable((ViewGroup) v, enable);
            } else {
                v.setEnabled(enable);
            }
        }

    }

    private int parseString2Int(String trim) {
        try {
            return Integer.parseInt(trim);
        } catch (Exception var3) {
            return this.mDefaultValue;
        }
    }

    public interface OnValueAddOrSubListener {
        void onValueAddOrSub(int var1);
    }

    public interface OnValueChangeListener {
        void onValueChange(int var1);
    }
}
