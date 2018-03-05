package com.cctv.langduzhe.feature.read;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.cctv.langduzhe.AppConstants;
import com.cctv.langduzhe.R;
import com.cctv.langduzhe.SimpleObserve;
import com.cctv.langduzhe.base.BaseActivity;
import com.cctv.langduzhe.util.CommonUtil;
import com.cctv.langduzhe.util.StatusBarUtil;
import com.cctv.langduzhe.util.ToastUtils;
import com.cjt2325.cameralibrary.JCameraView;
import com.cjt2325.cameralibrary.YVoiceView;
import com.cjt2325.cameralibrary.listener.ErrorListener;
import com.cjt2325.cameralibrary.listener.RecordVoiceListener;
import com.cjt2325.cameralibrary.util.FileUtil;
import com.cjt2325.cameralibrary.util.LogUtil;
import com.czt.mp3recorder.MP3Recorder;
import com.shuyu.waveview.AudioWaveView;
import com.shuyu.waveview.FileUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gentleyin
 * on 2018/1/20.
 * 说明：录制音频页面
 */
public class RecordVoiceActivity extends BaseActivity {

    @BindView(R.id.voice_view)
    YVoiceView voiceView;


    private MP3Recorder mRecorder;
    private String filePath;
    private boolean mIsRecord = false;

    private int duration;
    private int curPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.immersiveStatusBar(this,0);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_record_voice);
        ButterKnife.bind(this);
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO)
                .subscribe(new SimpleObserve() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            initCameraView();
                        } else {
                            ToastUtils.showShort(RecordVoiceActivity.this, "需要相应的权限");
                            finish();
                        }
                    }
                });
    }

    private void initCameraView() {
        voiceView.setTip(getString(R.string.record_voice_tip));
        voiceView.setRecordLisenter(new RecordVoiceListener() {
            @Override
            public void recordSuccess(String url) {
                //获取视频路径
                Bundle videoBundle = new Bundle();
                videoBundle.putString("voice_url", url);
                toActivity(RecordVoiceOverActivity.class, videoBundle);
            }
        });

    }
    @Override
    public void setPresenter() {

    }
}
