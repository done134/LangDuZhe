package com.cjt2325.cameralibrary.view;

import android.graphics.Bitmap;

/**
 * Created by gentleyin
 * on 2018/2/11.
 * 说明：录音页面状态控制
 */
public interface VoiceView {
    void resetState(int type);

    void confirmState(int type);

    void playVoice( String url);

    void stopVoice();

    void setTip(String tip);


}
