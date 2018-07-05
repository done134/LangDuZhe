package com.cctv.langduzhe.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cctv.langduzhe.R;

/**
 * Created by gentleyin
 * on 2018/4/16.
 * 说明：OnClickListener添加点击事件时，只触发onclick(),不会改变状态
 */
public class ClickNotToggleCheckBox extends LinearLayout {

    private CheckBox cbHeart;

    private TextView textView;

    private String text;
    public ClickNotToggleCheckBox(Context context) {
        this(context,null);
    }

    public ClickNotToggleCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public ClickNotToggleCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_click_not_toggle_checkbox, this);
        cbHeart = findViewById(R.id.cb_icon);
        textView = findViewById(R.id.tv_text);
        //get AttributeSet
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, com.cjt2325.cameralibrary.R.styleable.LikeCheckBox, defStyleAttr, 0);
        int iconSrc = a.getResourceId(com.cjt2325.cameralibrary.R.styleable.LikeCheckBox_drawable, R.drawable.selector_thumb_gray);
        int textColor = a.getResourceId(com.cjt2325.cameralibrary.R.styleable.LikeCheckBox_textColor, R.color.c_c8c8c8);
        a.recycle();
        textView.setTextColor(getResources().getColor(textColor));
        Drawable drawable = getResources().getDrawable(iconSrc);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        cbHeart.setCompoundDrawables(drawable, null, null, null);
    }

    public boolean isChecked() {
        return cbHeart.isChecked();
    }

    public void setChecked(boolean checked) {
        cbHeart.setChecked(checked);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        textView.setText(text);
    }
}
