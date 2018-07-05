package com.cctv.langduzhe.feature;

import android.view.WindowManager;

import com.umeng.message.inapp.InAppMessageManager;
import com.umeng.message.inapp.UmengSplashMessageActivity;


/**
 * Created by gentleyin on 2018/1/14.
 */
public class WelcomeActivity extends UmengSplashMessageActivity {

    @Override
    public boolean onCustomPretreatment() {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        InAppMessageManager mInAppMessageManager = InAppMessageManager.getInstance(this);
        //设置应用内消息为Debug模式
        mInAppMessageManager.setInAppMsgDebugMode(true);
        //参数为Activity的完整包路径，下面仅是示例代码，请按实际需求填写
//        String hasLogin = (String) SPUtils.get(this, PreferenceContents.TOKEN, "");
//        if (!TextUtils.isEmpty(hasLogin)) {
            mInAppMessageManager.setMainActivityPath("com.cctv.langduzhe.feature.MainActivity");
//        } else {
//            mInAppMessageManager.setMainActivityPath("com.cctv.langduzhe.feature.LoginActivity");
//        }
        return super.onCustomPretreatment();
    }
}
