package com.cctv.langduzhe;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.cctv.langduzhe.data.http.ApiClient;
import com.cctv.langduzhe.data.http.ApiConstants;
import com.cctv.langduzhe.data.http.configuration.ApiConfiguration;
import com.cctv.langduzhe.util.picasco.ImageDownLoader;
import com.cctv.langduzhe.util.picasco.PicassoUtils;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.security.cert.CertificateException;
import java.util.Collections;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;

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

        //初始化Stetho
//        BuildConfig.STETHO.init(this.getApplicationContext());

        applicationInstance = this;
        Picasso.setSingletonInstance(new Picasso.Builder(this).
                downloader(new ImageDownLoader(PicassoUtils.getOkClient())).loggingEnabled(true)
                .build());
    //初始化ApiClient
        ApiConfiguration apiConfiguration = ApiConfiguration.builder()
//                .dataSourceType(ApiConstants.WEATHER_DATA_SOURCE_TYPE_MI)
//                .dataSourceType(ApiConstants.WEATHER_DATA_SOURCE_TYPE_KNOW)
                .dataSourceType(ApiConstants.DATA_SOURCE_TYPE_TEST)
                .build();
        ApiClient.init(apiConfiguration);
        Log.d(TAG, "onCreate end");
    }


    public Context getContext() {
        return getApplicationContext();
    }

}
