package com.cjt2325.cameralibrary.state;

import com.czt.mp3recorder.MP3Recorder;
import com.shuyu.waveview.AudioWaveView;

import java.io.IOException;

/**
 * Created by gentleyin
 * on 2018/2/13.
 * 说明：
 */
public interface VoiceState {

    void start(MP3Recorder mRecorder) throws IOException;

    void stop();

    void restart();

    void stopRecord(boolean isShort, long time, String filePath);

    void cancel();

    void confirm();
}
