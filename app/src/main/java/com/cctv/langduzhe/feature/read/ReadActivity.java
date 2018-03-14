package com.cctv.langduzhe.feature.read;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gentleyin
 * on 2018/1/20.
 * 说明：朗读
 */
public class ReadActivity extends BaseActivity {
    @BindView(R.id.iv_record_video)
    TextView ivRecordVideo;
    @BindView(R.id.iv_record_voice)
    TextView ivRecordVoice;
    @BindView(R.id.btn_cancel)
    TextView btnCancel;
    @BindView(R.id.read_bg)
    LinearLayout readBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_pop_style);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_record_video, R.id.iv_record_voice, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_record_video:
                //录制视频
                toActivity(RecordVideoActivity.class);
                break;
            case R.id.iv_record_voice:
                toActivity(RecordVoiceActivity.class);
                //录制音频
                break;
            case R.id.btn_cancel:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void setPresenter() {

    }

}
