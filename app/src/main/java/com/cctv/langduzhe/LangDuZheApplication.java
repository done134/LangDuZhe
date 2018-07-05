package com.cctv.langduzhe;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.util.Log;

import com.cctv.langduzhe.data.http.ApiClient;
import com.cctv.langduzhe.data.http.ApiConstants;
import com.cctv.langduzhe.data.http.configuration.ApiConfiguration;
import com.cctv.langduzhe.feature.message.MessageDetailActivity;
import com.cctv.langduzhe.util.picasco.ImageDownLoader;
import com.cctv.langduzhe.util.picasco.PicassoUtils;
import com.cjt2325.cameralibrary.util.LogUtil;
import com.pgyersdk.crash.PgyCrashManager;
import com.squareup.picasso.Picasso;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;


/**
 * Created by gentleyin on 2018/1/13.
 * 项目Application
 */

public class LangDuZheApplication extends Application {
    private static final String TAG = "LangDuZheApp";


    private static LangDuZheApplication applicationInstance;

    public static LangDuZheApplication getInstance() {

        return applicationInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Log.d(TAG, "attachBaseContext");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate start");
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }
        PgyCrashManager.register(getApplicationContext());

        /**
         * 初始化友盟推送
         */
        PushAgent mPushAgent = PushAgent.getInstance(this);
            //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                LogUtil.i("deviceToken", deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                //注册成功会返回device token
                LogUtil.i("deviceToken", s);
                LogUtil.i("deviceToken", s1);

            }
        });
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
//                Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LangDuZheApplication.this, MessageDetailActivity.class);
                intent.putExtra("title", msg.title);
                intent.putExtra("msgContent", msg.text);
                startActivity(intent);
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);

        //初始化Stetho
//        BuildConfig.STETHO.init(this.getApplicationContext());

        applicationInstance = this;
        Picasso.setSingletonInstance(new Picasso.Builder(this).
                downloader(new ImageDownLoader(PicassoUtils.getOkClient())).loggingEnabled(true)
                .build());
    //初始化ApiClient
        ApiConfiguration apiConfiguration = ApiConfiguration.builder()
                .dataSourceType(ApiConstants.DATA_SOURCE_TYPE_ONLINE)
//                .dataSourceType(ApiConstants.DATA_SOURCE_TYPE_TEST)
                .build();
        ApiClient.init(apiConfiguration);
        Log.d(TAG, "onCreate end");
    }


    public Context getContext() {
        return getApplicationContext();
    }

}
