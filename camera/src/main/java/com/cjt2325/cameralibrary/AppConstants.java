package com.cjt2325.cameralibrary;


import android.os.Environment;

import java.io.File;

/**
 * Created by gentleyin on 2018/1/13.
 * App全局常量
 */

public class AppConstants {


    /**
     * 文件保存父路径
     */
    public static final String FILE_PARENT_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "cn.cctv.langduzhe";


    /**
     * 保存音频路径
     */
    public static final String AUDIO_PATH = FILE_PARENT_PATH+"/audio";

}
