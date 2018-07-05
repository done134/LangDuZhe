package com.cctv.langduzhe.feature.read;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.cctv.langduzhe.AppConstants;
import com.cctv.langduzhe.R;
import com.cctv.langduzhe.SimpleObserve;
import com.cctv.langduzhe.base.BaseActivity;
import com.cctv.langduzhe.util.StatusBarUtil;
import com.cctv.langduzhe.util.ToastUtils;
import com.cjt2325.cameralibrary.JCameraView;
import com.cjt2325.cameralibrary.listener.ClickListener;
import com.cjt2325.cameralibrary.listener.ErrorListener;
import com.cjt2325.cameralibrary.listener.JCameraListener;
import com.cjt2325.cameralibrary.util.FileUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;


import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by gentleyin
 * on 2018/1/20.
 * 说明：录制视频页面
 */
public class RecordVideoActivity extends BaseActivity {
    @BindView(R.id.camera_view)
    com.cjt2325.cameralibrary.JCameraView jCameraView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.immersiveStatusBar(this,0);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_record_video);
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
                            ToastUtils.showShort(RecordVideoActivity.this, "需要相应的权限");
                            finish();
                        }
                    }
                });
    }

    /**
    * @author 尹振东
    * create at 2018/1/21 下午6:35
    * 方法说明：初始化录视频控件
    */
    private void initCameraView() {
        //设置视频保存路径
        jCameraView.setSaveVideoPath(AppConstants.VIDEO_PATH);
        jCameraView.setFeatures(JCameraView.BUTTON_STATE_ONLY_RECORDER);
        jCameraView.setTip(getString(R.string.record_video_tip));
        jCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_HIGH);
        jCameraView.setErrorLisenter(new ErrorListener() {
            @Override
            public void onError() {
                //错误监听
                Log.i("CJT", "camera error");
                Intent intent = new Intent();
                setResult(103, intent);
                finish();
            }

            @Override
            public void AudioPermissionError() {
                Toast.makeText(RecordVideoActivity.this, "给点录音权限可以?", Toast.LENGTH_SHORT).show();
            }
        });
        //JCameraView监听
        jCameraView.setJCameraLisenter((url, firstFrame) -> {
            //获取视频路径
            String path = FileUtil.saveBitmap("JCamera", firstFrame);
            Bundle videoBundle = getIntent().getExtras();
            videoBundle.putString("video_path", url);
            videoBundle.putString("thumb_image", path);
            boolean isPortrait = jCameraView.getRotationFlag() == 0;
            videoBundle.putBoolean("isPortrait", isPortrait);
            toActivity(ReadOverActivity.class, videoBundle);
        });

        jCameraView.setLeftClickListener(RecordVideoActivity.this::finish);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //全屏显示
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        jCameraView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        jCameraView.onPause();
    }

    @Override
    public void setPresenter() {

    }
}
