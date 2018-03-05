package com.cjt2325.cameralibrary.state;

import com.cjt2325.cameralibrary.YVoiceView;
import com.czt.mp3recorder.MP3Recorder;
import com.shuyu.waveview.AudioWaveView;

import java.io.IOException;

/**
 * Created by gentleyin
 * on 2018/2/13.
 * 说明：
 */
public class BorrowVoiceState implements VoiceState {
    private VoiceMachine voiceMachine;

    public BorrowVoiceState(VoiceMachine voiceMachine) {
        this.voiceMachine = voiceMachine;
    }

    @Override
    public void start(MP3Recorder mRecorder, AudioWaveView audioWave) throws IOException {
        mRecorder.start();
        audioWave.startView();

    }

    @Override
    public void stop() {

    }

    @Override
    public void restart() {

    }


    @Override
    public void stopRecord(boolean isShort, long time, String filePath) {

    }

    @Override
    public void cancel() {
        voiceMachine.getView().resetState(YVoiceView.TYPE_VIDEO);
        voiceMachine.setState(voiceMachine.getPreviewState());
    }

    @Override
    public void confirm() {
        voiceMachine.getView().confirmState(YVoiceView.TYPE_VIDEO);
        voiceMachine.setState(voiceMachine.getPreviewState());
    }
}
