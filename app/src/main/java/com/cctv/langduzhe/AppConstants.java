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
     * 七牛图片域名
     */
    public static final String QI_NIU_IMAGE_DOMAIN = "http://p4v9f6w5q.bkt.clouddn.com/";
}
