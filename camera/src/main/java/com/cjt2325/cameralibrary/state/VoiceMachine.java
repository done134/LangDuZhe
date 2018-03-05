package com.cjt2325.cameralibrary.state;

import android.content.Context;

import com.cjt2325.cameralibrary.view.VoiceView;
import com.czt.mp3recorder.MP3Recorder;
import com.shuyu.waveview.AudioWaveView;

import java.io.IOException;

/**
 * Created by gentleyin
 * on 2018/2/13.
 * 说明：
 */
public class VoiceMachine implements VoiceState {


    private Context context;
    private VoiceState state;
    private VoiceView view;

    private VoiceState previewState;       //浏览状态(空闲)
    private VoiceState borrowVoiceState;   //播放音频


    public VoiceMachine(Context context, VoiceView view) {
        this.context = context;
        previewState = new PreviewVoiceState(this);
        borrowVoiceState = new BorrowVoiceState(this);
        //默认设置为空闲状态
        this.state = previewState;
//        this.cameraOpenOverCallback = cameraOpenOverCallback;
        this.view = view;
    }

    public VoiceView getView() {
        return view;
    }

    public Context getContext() {
        return context;
    }

    public void setState(VoiceState state) {
        this.state = state;
    }

    public VoiceState getState() {
        return this.state;
    }

    public VoiceState getPreviewState() {
        return previewState;
    }

    public VoiceState getBorrowVoiceState() {
        return borrowVoiceState;
    }

    @Override
    public void start(MP3Recorder mRecorder, AudioWaveView audioWave) throws IOException {
        state.start(mRecorder, audioWave);
    }

    @Override
    public void stop() {
        state.stop();
    }

    @Override
    public void restart() {
        state.restart();
    }


    @Override
    public void stopRecord(boolean isShort, long time, String filePath) {
        state.stopRecord(isShort, time, filePath);
    }

    @Override
    public void cancel() {
        state.cancel();
    }

    @Override
    public void confirm() {
        state.confirm();
    }
}
