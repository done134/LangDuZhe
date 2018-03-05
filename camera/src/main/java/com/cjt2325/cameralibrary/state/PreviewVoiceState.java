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
public class PreviewVoiceState implements VoiceState {

    private VoiceMachine voiceMachine;
    public PreviewVoiceState(VoiceMachine voiceMachine) {
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
        if (isShort) {
            voiceMachine.getView().resetState(YVoiceView.TYPE_SHORT);
        } else {
            voiceMachine.getView().playVoice(filePath);
            voiceMachine.setState(voiceMachine.getBorrowVoiceState());
        }

    }

    @Override
    public void cancel() {

    }

    @Override
    public void confirm() {

    }
}
