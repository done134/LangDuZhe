package com.cjt2325.cameralibrary;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.cjt2325.cameralibrary.listener.CaptureListener;
import com.cjt2325.cameralibrary.listener.RecordTimeListener;
import com.cjt2325.cameralibrary.util.CheckPermission;
import com.cjt2325.cameralibrary.util.LogUtil;

import static com.cjt2325.cameralibrary.JCameraView.BUTTON_STATE_BOTH;


/**
 * =====================================
 * 作    者: 陈嘉桐 445263848@qq.com
 * 版    本：1.1.4
 * 创建日期：2017/4/25
 * 描    述：拍照按钮
 * =====================================
 */
/**
 * @author 尹振东
 * create at 2018/1/22 下午11:35
 * 方法说明：修改为CheckBox，之前为长按录制，松手停止，现在为点击开始录制，再点击停止
 */
public class CaptureButton extends CheckBox {

    public final static int VOICE = 1;
    public final static int VIDEO = 2;

    private int state;              //当前按钮状态
    private int button_state;       //按钮可执行的功能状态（拍照,录制,两者）

    public static final int STATE_IDLE = 0x001;        //空闲状态
    private int duration;           //录制视频最大时间长度
    private int min_duration;       //最短录制时间限制
    private int recorded_time;      //记录当前录制的时间

//    private RectF rectF;

//    private LongPressRunnable longPressRunnable;    //长按后处理的逻辑Runnable
    private CaptureListener captureListener;        //、按钮回调接口
    private RecordCountDownTimer timer;             //计时器

    private RecordTimeListener recordTimeListener;//时间进度回调

    public void setRecordTimeListener(RecordTimeListener recordTimeListener) {
        this.recordTimeListener = recordTimeListener;
    }

    public CaptureButton(Context context, int type) {
        super(context);
        if (type == VOICE) {
            setButtonDrawable(R.drawable.selector_btn_record_voice);
        } else {
            setButtonDrawable(R.drawable.selector_btn_record);
        }
        state = STATE_IDLE;                //初始化为空闲状态
        button_state = BUTTON_STATE_BOTH;  //初始化按钮为可录制可拍照
        LogUtil.i("CaptureButtom start");
        duration = 10 * 1000;              //默认最长录制时间为10s
        LogUtil.i("CaptureButtom end");
        min_duration = 1500;              //默认最短录制时间为1.5s

        timer = new RecordCountDownTimer(duration, duration / 360);    //录制定时器

        setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //没有录制权限
                    if (CheckPermission.getRecordState() != CheckPermission.STATE_SUCCESS) {
                        state = STATE_IDLE;
                        if (captureListener != null) {
                            captureListener.recordError();
                            return;
                        }
                    }
                    if (captureListener != null)
                        captureListener.recordStart();
                    timer.start();
                }else{
                    timer.cancel(); //停止计时器
                    recordEnd();
                }
            }
        });
    }

    //录制结束
    private void recordEnd() {
        if (captureListener != null) {
            if (recorded_time < min_duration)
                captureListener.recordShort(recorded_time);//回调录制时间过短
            else
                captureListener.recordEnd(recorded_time);  //回调录制结束
        }
        setChecked(false);
//        resetRecordAnim();  //重制按钮状态
    }


    //更新进度条
    private void updateProgress(long millisUntilFinished) {
        recorded_time = (int) (duration - millisUntilFinished);
//        invalidate();
        if (recordTimeListener != null) {
            recordTimeListener.onTick(recorded_time);
        }
    }

    //录制视频计时器
    private class RecordCountDownTimer extends CountDownTimer {
        RecordCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            updateProgress(millisUntilFinished);
        }

        @Override
        public void onFinish() {
            updateProgress(0);
            recordEnd();
        }
    }
    /**************************************************
     * 对外提供的API                     *
     **************************************************/

    //设置最长录制时间
    public void setDuration(int duration) {
        this.duration = duration;
        timer = new RecordCountDownTimer(duration, duration / 360);    //录制定时器
    }

    //设置最短录制时间
    public void setMinDuration(int duration) {
        this.min_duration = duration;
    }

    //设置回调接口
    public void setCaptureListener(CaptureListener captureListener) {
        this.captureListener = captureListener;
    }

    //设置按钮功能（拍照和录像）
    public void setButtonFeatures(int state) {
        this.button_state = state;
    }

    //是否空闲状态
    public boolean isIdle() {
        return state == STATE_IDLE;
    }

    //设置状态
    public void resetState() {
        state = STATE_IDLE;
    }
}
