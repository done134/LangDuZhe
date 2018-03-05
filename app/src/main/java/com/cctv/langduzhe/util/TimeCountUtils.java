package com.cctv.langduzhe.util;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

import com.cctv.langduzhe.R;


/**
 *
 * 定义一个倒计时工具类(发送验证码)
 * liuzhen
 */
public class TimeCountUtils extends CountDownTimer {

    private TextView btn_get_auth_code;
    private Context context;

    public TimeCountUtils(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
    }

    /**
     *
     * @param millisInFuture 总时长
     * @param
     * //getCode 发送验证码按钮
     */
    public TimeCountUtils(long millisInFuture, TextView btn_get_auth_code, Context context) {
        super(millisInFuture, 1000L);
        this.btn_get_auth_code=btn_get_auth_code;
        this.context=context;
    }


    @Override
    public void onFinish() {// 计时完毕时触发
        String message = "发送验证码";
        btn_get_auth_code.setEnabled(true);
        btn_get_auth_code.setClickable(true);
        btn_get_auth_code.setText(message);
        btn_get_auth_code.setTextColor(context.getResources().getColor(R.color.ff2b2b));
    }

    @Override
    public void onTick(long millisUntilFinished) {// 计时过程显示
        btn_get_auth_code.setEnabled(false);
        btn_get_auth_code.setClickable(false);//防止重复发送
        btn_get_auth_code.setText(String.format("%d秒", millisUntilFinished / 1000));
        btn_get_auth_code.setTextColor(context.getResources().getColor(R.color.c_8c8c8c));
    }


}
