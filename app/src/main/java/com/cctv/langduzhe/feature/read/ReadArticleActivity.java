package com.cctv.langduzhe.feature.read;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;

import com.cctv.langduzhe.AppConstants;
import com.cctv.langduzhe.R;
import com.cctv.langduzhe.SimpleObserve;
import com.cctv.langduzhe.base.BaseActivity;
import com.cctv.langduzhe.contract.read.ReadArticlePresenter;
import com.cctv.langduzhe.contract.read.ReadArticleView;
import com.cctv.langduzhe.util.StatusBarUtil;
import com.cctv.langduzhe.util.ToastUtils;
import com.cjt2325.cameralibrary.YVoiceView;
import com.czt.mp3recorder.MP3Recorder;
import com.tbruyelle.rxpermissions2.RxPermissions;


import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by gentleyin
 * on 2018/3/29.
 * 说明：朗读文字页面
 */
public class ReadArticleActivity extends BaseActivity implements ReadArticleView{

    @BindView(R.id.voice_view)
    YVoiceView voiceView;


    private ReadArticlePresenter presenter;
    private String articleId;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.immersiveStatusBar(this,0);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_record_voice);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            articleId = bundle.getString("articleId");
            content = bundle.getString("content");
        }
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO)
                .subscribe(new SimpleObserve() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            initReadView();
                        } else {
                            ToastUtils.showShort(ReadArticleActivity.this, "需要相应的权限");
                            finish();
                        }
                    }
                });
    }

    private void initReadView() {
        voiceView.setTip(getString(R.string.record_voice_tip));
        voiceView.setRecordLisenter(url -> {
            //获取视频路径
            Bundle videoBundle = new Bundle();
            videoBundle.putString("voice_url", url);
            videoBundle.putString("articleId", articleId);
            toActivity(RecordVoiceOverActivity.class, videoBundle);
        });
        voiceView.setLeftClickListener(this::finish);
        voiceView.setTextContent(content);
    }



    @Override
    public void setPresenter() {
        presenter = new ReadArticlePresenter(this, this);
    }
}
