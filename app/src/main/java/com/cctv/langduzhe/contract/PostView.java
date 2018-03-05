package com.cctv.langduzhe.contract;

import com.cctv.langduzhe.base.BaseView;

/**
 * Created by gentleyin
 * on 2018/2/28.
 * 说明：
 */
public interface PostView extends BaseView {
    void postSucceed(String fileName, int duration, long fileSize);
}
