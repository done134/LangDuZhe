package com.cjt2325.cameralibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.cjt2325.cameralibrary.listener.CaptureListener;
import com.cjt2325.cameralibrary.listener.ClickListener;
import com.cjt2325.cameralibrary.listener.RecordVoiceListener;
import com.cjt2325.cameralibrary.listener.TypeListener;
import com.cjt2325.cameralibrary.state.VoiceMachine;
import com.cjt2325.cameralibrary.util.FileUtil;
import com.cjt2325.cameralibrary.util.LogUtil;
import com.cjt2325.cameralibrary.util.ScreenUtils;
import com.cjt2325.cameralibrary.view.VoiceView;
import com.czt.mp3recorder.MP3Recorder;
import com.shuyu.waveview.AudioPlayer;
import com.shuyu.waveview.AudioWaveView;
import com.shuyu.waveview.FileUtils;

import java.io.File;
import java.util.UUID;

/**
 * Created by gentleyin
 * on 2018/2/12.
 * 说明：录音页面
 */
public class YVoiceView extends FrameLayout implements VoiceView{

    //录音状态的类型
    public static final int TYPE_VIDEO = 0x002;
    public static final int TYPE_SHORT = 0x003;
    public static final int TYPE_DEFAULT = 0x004;

    //回调监听
    private RecordVoiceListener recordLisenter;
    private ClickListener leftClickListener;
    private ClickListener rightClickListener;
    private VoiceMachine voiceMachine;

    private Context mContext;
    private AudioWaveView audioWave;
    private CaptureLayout mCaptureLayout;
    private Chronometer tvRecordTime;


    //切换摄像头按钮的参数
    private int iconSize = 0;       //图标大小
    private int iconMargin = 0;     //右上边距
    private int iconSrc = 0;        //图标资源
    private int iconLeft = 0;       //左图标
    private int iconRight = 0;      //右图标
    private int duration = 0;       //录制时间

    private float screenProp = 0f;

    private boolean flagRecord = false;//是否正在录像


    private MP3Recorder mRecorder;
    private AudioPlayer audioPlayer;


    private String filePath;
    
    boolean mIsPlay = false;

    private int curPosition;

    public YVoiceView(@NonNull Context context) {
        this(context,null);
    }

    public YVoiceView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public YVoiceView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        //get AttributeSet
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.JCameraView, defStyleAttr, 0);
        iconSize = a.getDimensionPixelSize(R.styleable.JCameraView_iconSize, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 35, getResources().getDisplayMetrics()));
        iconMargin = a.getDimensionPixelSize(R.styleable.JCameraView_iconMargin, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 15, getResources().getDisplayMetrics()));
        iconSrc = a.getResourceId(R.styleable.JCameraView_iconSrc, R.drawable.ic_camera);
        iconLeft = a.getResourceId(R.styleable.JCameraView_iconLeft, 0);
        iconRight = a.getResourceId(R.styleable.JCameraView_iconRight, 0);
        duration = a.getInteger(R.styleable.JCameraView_duration_max, 10 * 1000);       //没设置默认为10s
        a.recycle();
        initView();
        initData();
    }


    private void initData() {
        filePath = FileUtils.getAppPath();
        File file = new File(filePath);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Toast.makeText(getContext(), "创建文件失败", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        voiceMachine = new VoiceMachine(getContext(),this);

        int offset = ScreenUtils.dip2px(getContext(), 1);
        filePath = FileUtils.getAppPath() + UUID.randomUUID().toString() + ".mp3";
        mRecorder = new MP3Recorder(new File(filePath));
        int size = ScreenUtils.getScreenWidth(getContext()) / offset;//控件默认的间隔是1
        mRecorder.setDataList(audioWave.getRecList(), size);
        audioPlayer = new AudioPlayer(getContext(), new Handler());
    }


    private void initView() {
        setWillNotDraw(false);
        View view = LayoutInflater.from(mContext).inflate(R.layout.voice_view, this);
        audioWave = (AudioWaveView) view.findViewById(R.id.voice_preview);
        tvRecordTime = (Chronometer) view.findViewById(R.id.tv_record_time);
        mCaptureLayout = (CaptureLayout) view.findViewById(R.id.capture_layout);
        mCaptureLayout.setDuration(duration);
        mCaptureLayout.setIconSrc(iconLeft, iconRight);

        //拍照 录像
        mCaptureLayout.setCaptureLisenter(new CaptureListener() {
            @Override
            public void takePictures() {
                //Do Nothing!
            }

            @Override
            public void recordStart() {
                flagRecord = true;
                try {
                    voiceMachine.start(mRecorder,audioWave);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "录音出现异常", Toast.LENGTH_SHORT).show();
                    recordError();
                }
                timerStart();
            }

            @Override
            public void recordShort(final long time) {
                mCaptureLayout.setTextWithAnimation("录制时间过短");
                tvRecordTime.setVisibility(GONE);
                tvRecordTime.stop();
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        voiceMachine.stopRecord(true,time, filePath);
                    }
                }, 1500 - time);
            }

            @Override
            public void recordEnd(long time) {
                flagRecord = false;
                if (mRecorder != null && mRecorder.isRecording()) {
                    mRecorder.setPause(false);
                    mRecorder.stop();
                    audioWave.stopView();
                }
                voiceMachine.stopRecord(false, time,filePath);
                tvRecordTime.stop();
            }

            @Override
            public void recordZoom(float zoom) {
                LogUtil.i("recordZoom");
            }

            @Override
            public void recordError() {
                FileUtils.deleteFile(filePath);
//                filePath = "";
                if (mRecorder != null && mRecorder.isRecording()) {
                    mRecorder.stop();
                    audioWave.stopView();
                }
            }
        });
        //确认 取消
        mCaptureLayout.setTypeLisenter(new TypeListener() {
            @Override
            public void cancel() {
                voiceMachine.cancel();
            }

            @Override
            public void confirm() {
                voiceMachine.confirm();
            }
        });

        mCaptureLayout.setLeftClickListener(new ClickListener() {
            @Override
            public void onClick() {
                if (leftClickListener != null) {
                    leftClickListener.onClick();
                }
            }
        });
        mCaptureLayout.setRightClickListener(new ClickListener() {
            @Override
            public void onClick() {
                if (rightClickListener != null) {
                    rightClickListener.onClick();
                }
            }
        });
    }

    /**
     * @author 尹振东
     * create at 2018/1/21 下午9:00
     * 方法说明：计时器展示并开始计时
     */
    private void timerStart() {
        tvRecordTime.setVisibility(VISIBLE);
        tvRecordTime.setBase(SystemClock.elapsedRealtime());//计时器清零
        tvRecordTime.start();
    }

    @Override
    public void resetState(int type) {
        switch (type) {
            case TYPE_VIDEO:
                stopVoice();    //停止播放
                break;
            case TYPE_SHORT:
                break;
            case TYPE_DEFAULT:
                if (mIsPlay) {
                    mIsPlay = false;
                    audioPlayer.pause();
                }
                break;
        }

        tvRecordTime.setVisibility(GONE);
        tvRecordTime.stop();
        mCaptureLayout.resetCaptureLayout();
    }

    @Override
    public void confirmState(int type) {
        switch (type) {
            case TYPE_VIDEO:
                stopVoice();    //停止播放
                if (recordLisenter != null) {
                    recordLisenter.recordSuccess(filePath);
                }
                break;
            case TYPE_SHORT:
                break;
            case TYPE_DEFAULT:
                break;
        }
        mCaptureLayout.resetCaptureLayout();
    }

    @Override
    public void playVoice(String url) {
        if (TextUtils.isEmpty(filePath) || !new File(filePath).exists()) {
            Toast.makeText(getContext(), "文件不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        mIsPlay = true;
        audioPlayer.playUrl(filePath);
    }

    @Override
    public void stopVoice() {
        if (mIsPlay) {
            mIsPlay = false;
            audioPlayer.pause();
        }
    }

    @Override
    public void setTip(String tip) {
        mCaptureLayout.setTip(tip);
    }

    public void setRecordLisenter(RecordVoiceListener recordLisenter) {
        this.recordLisenter = recordLisenter;
    }
}
