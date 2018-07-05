package com.cctv.langduzhe;


import android.os.Environment;

import java.io.File;

/**
 * Created by gentleyin on 2018/1/13.
 * App全局常量
 */

public class AppConstants {

    //选择图片
    public static final int REQUEST_CODE_CHOOSE = 1;
    //拍照
    public static final int REQUEST_CODE_CAPTURE = 2;

    /**
     * 文件保存父路径
     */
    public static final String FILE_PARENT_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "cn.cctv.langduzhe";

    /**
     * 保存视频路径
     */
    public static final String VIDEO_PATH = FILE_PARENT_PATH+"/video";

    /**
     * 保存音频路径
     */
    public static final String AUDIO_PATH = FILE_PARENT_PATH+"/audio";

    /**
     * 友盟推送App key
     */
    public static final String U_PUSH_APP_KEY = "5aa783c8f29d9817a800013a";
    /**
     * 友盟推送Message Secret
     */
    public static final String U_PUSH_MESSAGE_SECRET = "53a9cdc82870b2cb70eccbe51899183a";
    /**
     * 友盟推送Master Secret
     */
    public static final String U_PUSH_MASTER_SECRET = "mdovkgrkpbx9anhtae9kjo3udcekcb0d";
}
