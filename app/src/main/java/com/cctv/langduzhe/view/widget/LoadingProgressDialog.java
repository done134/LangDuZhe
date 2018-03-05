package com.cctv.langduzhe.view.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.cctv.langduzhe.R;
import com.cjt2325.cameralibrary.util.LogUtil;

public class LoadingProgressDialog extends ProgressDialog {

    private static final String TAG = LoadingProgressDialog.class.getSimpleName();

    public LoadingProgressDialog(Context context) {
        super(context,R.style.ios_bottom_dialog);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_all_loading);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutParams);
       /* Button benCancel = (Button) findViewById(R.id.btn_cancel);
        benCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d(TAG,"benCancel================");
                mOnClickCancelListener.singleClick();
            }
        });*/
    }

    public OnClickCancelListener mOnClickCancelListener;

    public void setOnClickCancelListener(OnClickCancelListener onClickCancelListener){
        this.mOnClickCancelListener = onClickCancelListener;
    }

    public interface OnClickCancelListener{
        void singleClick();
    }
}
