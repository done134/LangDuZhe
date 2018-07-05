package com.cctv.langduzhe.contract;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;


import com.alibaba.fastjson.JSONObject;
import com.cctv.langduzhe.base.BasePresenter;
import com.cctv.langduzhe.data.http.ApiClient;
import com.cctv.langduzhe.data.http.RxSchedulerUtils;
import com.cctv.langduzhe.data.preference.PreferenceContents;
import com.cctv.langduzhe.data.preference.SPUtils;
import com.cctv.langduzhe.feature.MainActivity;
import com.cjt2325.cameralibrary.util.LogUtil;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

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
        String hasLogin = (String) SPUtils.get(context, PreferenceContents.TOKEN, "");
        if (!TextUtils.isEmpty(hasLogin)) {
            resetToken();
        }
    }

    private void resetToken() {
        Observable<String> observable = ApiClient.apiService.refreshToken();
        Subscription subscription = observable
                .compose(RxSchedulerUtils.normalSchedulersTransformer())
                .subscribe(this::handleToken, throwable -> handleToken("{\"message\": \"登录已失效\"}"));
        subscriptions.add(subscription);
    }

    private void handleToken(String s) {
        LogUtil.i(TAG, s);
        JSONObject jsonObject = JSONObject.parseObject(s);
        if (!TextUtils.isEmpty(jsonObject.getString("code")) && jsonObject.getString("code").equals(RESULT_OK)) {
            String userName = jsonObject.getString("loginName");
            String token = "Bearer " + jsonObject.getString("token");
            SPUtils.put(context, PreferenceContents.USER_NAME, userName);
            SPUtils.put(context, PreferenceContents.TOKEN, token);
            loadReadInfo();
        } else {
            mainView.showToast(jsonObject.getString("message"));
            SPUtils.put(context, PreferenceContents.TOKEN, "");
//            mainView.toLogin();
        }

    }

    public void loadReadInfo() {
        Observable<String> observable = ApiClient.apiService.getUserInfo();
        Subscription subscription = observable
                .compose(RxSchedulerUtils.normalSchedulersTransformer())
                .subscribe(this::handleUserInfo, throwable -> {
                    mainView.showToast(throwable.getMessage());
                });
        subscriptions.add(subscription);
    }

    private void handleUserInfo(String userInfo) {
        LogUtil.i(userInfo);
        JSONObject jsonObject = JSONObject.parseObject(userInfo);
        JSONObject dataObj = jsonObject.getJSONObject("data");
        SPUtils.put(context, PreferenceContents.USER_NAME, dataObj.getString("name"));
        SPUtils.put(context, PreferenceContents.USER_IMG, dataObj.getString("img"));
    }
    @Override
    public void unSubscribe() {

    }



    /**
     * @author 尹振东
     * create at 2018/4/16 下午3:10
     * 方法说明：检查更新
     */
    public void checkUpdateApk(MainActivity mainActivity) {
        // 版本检测方式2：带更新回调监听
        PgyUpdateManager.register(mainActivity,
                new UpdateManagerListener() {
                    @Override
                    public void onUpdateAvailable(final String result) {
                        String url;
                        JSONObject jsonData;
                        jsonData = JSONObject.parseObject(result);
                        if ("0".equals(jsonData.getString("code"))) {
                            JSONObject jsonObject = jsonData.getJSONObject("data");
                            String version = jsonObject.getString("version");
                            url = jsonObject.getString("downloadURL");
                            new AlertDialog.Builder(mainActivity)
                                    .setTitle("更新")
                                    .setMessage(String.format("有新版本(%s)，请更新", version))
                                    .setNegativeButton("确定", (dialog, which) -> startDownloadTask(
                                            mainActivity,
                                            url))
                                    .setPositiveButton("取消", (dialog, which) -> dialog.dismiss())
                                    .show();

                        }
                    }

                    @Override
                    public void onNoUpdateAvailable() {
                    }
                });
    }
}
