package com.cctv.langduzhe.feature.read;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.base.BaseActivity;
import com.cctv.langduzhe.contract.PostPresenter;
import com.cctv.langduzhe.contract.PostView;
import com.cctv.langduzhe.contract.read.RecordVoiceOverPresenter;
import com.cctv.langduzhe.contract.read.RecordVoiceOverView;
import com.cctv.langduzhe.feature.MainActivity;
import com.cctv.langduzhe.util.DateConvertUtils;
import com.piterwilson.audio.MP3RadioStreamDelegate;
import com.piterwilson.audio.MP3RadioStreamPlayer;
import com.shuyu.waveview.AudioWaveView;
import com.shuyu.waveview.FileUtils;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cctv.langduzhe.util.CommonUtil.dip2px;
import static com.cctv.langduzhe.util.CommonUtil.getScreenWidth;

/**
 * Created by gentleyin
 * on 2018/2/26.
 * 说明：
 */
public class RecordVoiceOverActivity extends BaseActivity implements RecordVoiceOverView, MP3RadioStreamDelegate, PostView {

    private final String TAG = "RecordVoiceOverActivity";

    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.voice_view)
    AudioWaveView voiceView;
    @BindView(R.id.et_video_name)
    EditText etVideoName;
    @BindView(R.id.btn_save_the_local)
    Button btnSaveTheLocal;
    @BindView(R.id.btn_post_video)
    Button btnPostVideo;
    @BindView(R.id.tv_current_progress)
    TextView tvCurrentProgress;
    @BindView(R.id.seekbar_voice)
    SeekBar seekBar;
    @BindView(R.id.tv_total_progress)
    TextView tvTotalProgress;
    @BindView(R.id.cb_play_pause)
    CheckBox cbPlayPause;

    private MP3RadioStreamPlayer player;

    private Timer timer;

    private boolean playEnd;

    private boolean seekBarTouch;
    private String voiceUrl;
    private RecordVoiceOverPresenter presenter;
    private PostPresenter postPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_voice_over);
        ButterKnife.bind(this);
        tvTitle.setText("发布");
        //获取视频路径
        Bundle videoBundle = getIntent().getExtras();
        voiceUrl = videoBundle.getString("voice_url", "");
        initStatus();
    }

    private void initStatus() {
        new Handler().postDelayed(this::play, 100);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvCurrentProgress.setText(DateConvertUtils.secToTime(player.getCurPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBarTouch = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBarTouch = false;
                if (!playEnd) {
                    player.seekTo(seekBar.getProgress());
                }
            }
        });

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (playEnd || player == null || !seekBar.isEnabled()) {
                    return;
                }
                long position = player.getCurPosition();
                if (position > 0 && !seekBarTouch) {
                    seekBar.setProgress((int) position);
                }
            }
        }, 1000, 1000);
    }

    @Override
    public void setPresenter() {
        presenter = new RecordVoiceOverPresenter(this, this);
        postPresenter = new PostPresenter(this, this);
    }

    @OnClick({R.id.btn_back, R.id.btn_save_the_local, R.id.btn_post_video,R.id.cb_play_pause})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_save_the_local:
                String name = etVideoName.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    showToast("请输入作品名称！");
                    return;
                }
                presenter.saveToSDCard(voiceUrl, name);
                toActivity(MainActivity.class);
                showToast("已保存在本地");
                break;
            case R.id.btn_post_video:
                String title = etVideoName.getText().toString().trim();
                if (TextUtils.isEmpty(title)) {
                    showToast("请输入作品名称！");
                    return;
                }
                postPresenter.postFile(PostPresenter.VOICE_TYPE, voiceUrl);
                showProgress();
                break;
            case R.id.cb_play_pause:
                playOrPause();
                break;
        }
    }

    /**
     * 播放暂停按钮
     */
    private void playOrPause() {
        if (playEnd) {
            stop();
            seekBar.setEnabled(true);
            play();
            return;
        }

        if (player.isPause()) {
            player.setPause(false);
            seekBar.setEnabled(false);
        } else {
            player.setPause(true);
            seekBar.setEnabled(true);
        }
    }

    private void play() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
        player = new MP3RadioStreamPlayer();
        //player.setUrlString(this, true, "http://www.stephaniequinn.com/Music/Commercial%20DEMO%20-%2005.mp3");
        player.setUrlString(voiceUrl);
        player.setDelegate(this);

        int size = getScreenWidth(this) / dip2px(this, 1);//控件默认的间隔是1
        player.setDataList(voiceView.getRecList(), size);
        voiceView.setBaseRecorder(player);
        voiceView.startView();
        try {
            player.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FileUtils.deleteFile(voiceUrl);
    }

    private void stop() {
        player.stop();
    }

    /****************************************
     * Delegate methods. These are all fired from a background thread so we have to call any GUI code on the main thread.
     ****************************************/

    @Override
    public void onRadioPlayerPlaybackStarted(final MP3RadioStreamPlayer player) {
        Log.i(TAG, "onRadioPlayerPlaybackStarted");
        this.runOnUiThread(() -> {
            playEnd = false;
            seekBar.setMax((int) player.getDuration());
            seekBar.setEnabled(true);
            tvTotalProgress.setText(DateConvertUtils.secToTime(player.getDuration()));
        });
    }

    @Override
    public void onRadioPlayerStopped(MP3RadioStreamPlayer player) {
        Log.i(TAG, "onRadioPlayerStopped");
        this.runOnUiThread(() -> {
            playEnd = true;
            seekBar.setEnabled(false);
            cbPlayPause.setChecked(true);
            if (!player.isPause()) {
                seekBar.setProgress(100);
            }
        });

    }

    @Override
    public void onRadioPlayerError(MP3RadioStreamPlayer player) {
        Log.i(TAG, "onRadioPlayerError");
        this.runOnUiThread(() -> {
            playEnd = false;
            seekBar.setEnabled(false);
        });

    }

    @Override
    public void onRadioPlayerBuffering(MP3RadioStreamPlayer player) {
        Log.i(TAG, "onRadioPlayerBuffering");
        this.runOnUiThread(() -> seekBar.setEnabled(false));

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        voiceView.stopView();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        stop();
    }

    @Override
    public void postSucceed(String fileName, int duration, long fileSize) {
        dismissProgress();
        presenter.saveVoice(fileName, etVideoName.getText().toString().trim(), duration, fileSize);
    }
}
