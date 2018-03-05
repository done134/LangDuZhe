package com.cctv.langduzhe.contract;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;


import com.alibaba.fastjson.JSONObject;
import com.cctv.langduzhe.base.BasePresenter;
import com.cctv.langduzhe.data.http.ApiClient;
import com.cctv.langduzhe.data.http.RxSchedulerUtils;
import com.cctv.langduzhe.data.preference.PreferenceContents;
import com.cctv.langduzhe.data.preference.SPUtils;
import com.cctv.langduzhe.feature.LoginActivity;
import com.cctv.langduzhe.feature.MainActivity;
import com.cctv.langduzhe.util.TimeCountUtils;
import com.cjt2325.cameralibrary.util.LogUtil;

import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by gentleyin
 * on 2018/1/15.
 * 说明：
 */
public class MainPresenter implements BasePresenter {

    private String TAG = "MainPresenter";
    private final Context context;
    private final MainView mainView;
    private CompositeSubscription subscriptions;

    public MainPresenter(Context context, MainView view) {
        this.context = context;
        this.mainView = view;
        this.subscriptions = new CompositeSubscription();

    }

    @Override
    public void subscribe() {
        resetToken();
    }

    private void resetToken() {
        Observable<String> observable = ApiClient.apiService.refreshToken();
        Subscription subscription = observable
                .compose(RxSchedulerUtils.normalSchedulersTransformer())
                .subscribe(this::handleToken, throwable -> {
                    handleToken("{\"message\": \"登录已失效\"}");
                });
        subscriptions.add(subscription);
    }

    private void handleToken(String s) {
        LogUtil.i(TAG,s);
        JSONObject jsonObject = JSONObject.parseObject(s);
        if (!TextUtils.isEmpty(jsonObject.getString("code")) && jsonObject.getString("code").equals(RESULT_OK)) {
            String userName = jsonObject.getString("loginName");
            String token = "Bearer "+jsonObject.getString("token");
            SPUtils.put(context, PreferenceContents.USER_NAME, userName);
            SPUtils.put(context,PreferenceContents.TOKEN,token);
        }else{
            mainView.showToast(jsonObject.getString("message"));
            mainView.toLogin();
        }

    }

    @Override
    public void unSubscribe() {

    }
}
