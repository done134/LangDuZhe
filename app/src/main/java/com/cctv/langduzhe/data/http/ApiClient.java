package com.cctv.langduzhe.data.http;

import android.text.TextUtils;

import com.baronzhang.retrofit2.converter.FastJsonConverterFactory;
import com.cctv.langduzhe.LangDuZheApplication;
import com.cctv.langduzhe.data.preference.PreferenceContents;
import com.cctv.langduzhe.data.preference.SPUtils;
import com.cctv.langduzhe.util.CommonUtil;
import com.cctv.langduzhe.BuildConfig;
import com.cctv.langduzhe.data.http.configuration.ApiConfiguration;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by gentleyin on 2018/1/13.
 */
public final class ApiClient {

    public static ApiService apiService;

    public static ApiConfiguration configuration;

    public static void init(ApiConfiguration apiConfiguration) {

        configuration = apiConfiguration;
        String weatherApiHost = "";
        switch (configuration.getDataSourceType()) {
            case ApiConstants.DATA_SOURCE_TYPE_TEST:
                weatherApiHost = ApiConstants.TEST_API_HOST;
                break;
            case ApiConstants.DATA_SOURCE_TYPE_ONLINE:
                weatherApiHost = ApiConstants.ONLINE_API_HOST;
                break;

        }
        apiService = initApiService(weatherApiHost, ApiService.class);
    }

    private static <T> T initApiService(String baseUrl, Class<T> clazz) {

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(httpLoggingInterceptor);
//            BuildConfig.STETHO.addNetworkInterceptor(builder);、
        }
        builder.addInterceptor(initCacheInterceptor());
        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        return retrofit.create(clazz);
    }

    /**
    * @author 尹振东
    * create at 2018/2/11 上午9:39
    * 方法说明：无网络时显示缓存数据
    */
    private static Interceptor initCacheInterceptor(){
//        File cacheFile = new File(LangDuZheApplication.getInstance().getContext().getExternalCacheDir(), "LangDuzheCache");
//        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        return chain -> {
            Request original = chain.request();
            Request request;
            String token =   (String) SPUtils.get(LangDuZheApplication.getInstance().getContext(), PreferenceContents.TOKEN,"");
            if (TextUtils.isEmpty(token)) {
                request = original.newBuilder()
                        .method(original.method(), original.body())
                        .build();
            }else {
                request = original.newBuilder()
                        .header("Authorization", token)
                        .method(original.method(), original.body())
                        .build();
            }

            if (CommonUtil.isNetworkConnected(LangDuZheApplication.getInstance().getContext())) {
                int maxAge = 0;
                // 有网络时 设置缓存超时时间0个小时

            } else {
                // 无网络时，设置超时为4周
                int maxStale = 60 * 60 * 24 * 28;

            }
            return chain.proceed(request);
        };
    }


}

