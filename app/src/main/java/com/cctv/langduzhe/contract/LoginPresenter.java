package com.cctv.langduzhe.contract;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cctv.langduzhe.base.BasePresenter;
import com.cctv.langduzhe.contract.mine.EditUserInfoView;
import com.cctv.langduzhe.data.http.ApiClient;
import com.cctv.langduzhe.data.http.RxSchedulerUtils;
import com.cctv.langduzhe.data.preference.PreferenceContents;
import com.cctv.langduzhe.data.preference.SPUtils;
import com.cctv.langduzhe.feature.LoginActivity;
import com.cctv.langduzhe.feature.MainActivity;
import com.cctv.langduzhe.feature.mine.EditUserInfoActivity;
import com.cctv.langduzhe.util.CommonUtil;
import com.cctv.langduzhe.util.MD5Util;
import com.cctv.langduzhe.util.TimeCountUtils;
import com.cjt2325.cameralibrary.util.LogUtil;

import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by gentleyin
 * on 2018/1/15.
 * 说明：
 */
public class LoginPresenter implements BasePresenter{

    private final LoginView loginView;
    private Context context;

    private CompositeSubscription subscriptions;
    private TimeCountUtils timeCountutils;

    public LoginPresenter(Context context, LoginView view) {
        loginView = view;
        this.context = context;
        this.subscriptions = new CompositeSubscription();
    }

    /**
    * @author 尹振东
    * create at 2018/2/11 下午3:17
    * 方法说明：获取验证码
    */
    public void getPhoneCode(String phoneStr) {
        String captchaId = UUID.randomUUID().toString();
        String captchaCode  = MD5Util.MD5(phoneStr+captchaId+"lOB0BnEXx3vVD1wi");
        Observable<String> observable = ApiClient.apiService.getPhoneCode(phoneStr,captchaId,captchaCode);
        Subscription subscription = observable
                .compose(RxSchedulerUtils.normalSchedulersTransformer())
                .subscribe(this::handleCode, throwable -> {
                    Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
                });
        subscriptions.add(subscription);

    }

    /**
    * @author 尹振东
    * create at 2018/2/11 下午2:48
    * 方法说明：处理请求验证码返回结果
    */
    private void handleCode(String result){
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (!TextUtils.isEmpty(jsonObject.getString("code")) && jsonObject.getString("code").equals(RESULT_OK)) {
            timeCountutils = new TimeCountUtils(60 * 1000, loginView.getAuthView(), context);
            timeCountutils.start();
            loginView.showToast("验证码已发送");
        }else {
            loginView.showToast(jsonObject.getString("message"));
        }
    }

    /**
    * @author 尹振东
    * create at 2018/2/11 下午3:17
    * 方法说明：登录请求
    */
    public void login(String phoneStr,String phoneCode) {

        String requestJson = handleRequestParams(phoneStr, phoneCode);
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        requestJson);
        Observable<String> observable = ApiClient.apiService.login(requestBody);
        Subscription subscription = observable
                .compose(RxSchedulerUtils.normalSchedulersTransformer())
                .subscribe(this::handleLogin, throwable -> {
                    Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
                });
        subscriptions.add(subscription);

    }

    /**
    * @author 尹振东
    * create at 2018/2/14 下午9:06
    * 方法说明：组装参数
    */
    private String handleRequestParams(String phoneStr,String phoneCode) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("smsCode", phoneCode);
        jsonObject.put("username", phoneStr);
        return jsonObject.toJSONString();
    }
    /**
    * @author 尹振东
    * create at 2018/2/11 下午3:16
    * 方法说明：处理登录返回结果
    */
    private void handleLogin(String result){
        LogUtil.i(result);
        loginView.dismissProgress();
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (!TextUtils.isEmpty(jsonObject.getString("code")) && jsonObject.getString("code").equals(RESULT_OK)) {
            String userName = jsonObject.getString("loginName");
            String token = "Bearer "+jsonObject.getString("token");
            SPUtils.put(context, PreferenceContents.USER_NAME, userName);
            SPUtils.put(context,PreferenceContents.TOKEN,token);
            loadReadInfo();
        }else{
            loginView.showToast(jsonObject.getString("message"));
        }

    }

    public void loadReadInfo() {
        Observable<String> observable = ApiClient.apiService.getUserInfo();
        Subscription subscription = observable
                .compose(RxSchedulerUtils.normalSchedulersTransformer())
                .subscribe(this::handleUserInfo, throwable -> {
                    Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
                });
        subscriptions.add(subscription);
    }

    private void handleUserInfo(String userInfo) {
        LogUtil.i(userInfo);
        JSONObject jsonObject = JSONObject.parseObject(userInfo);
        if (!TextUtils.isEmpty(jsonObject.getString("img"))) {

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("new_login", true);
        context.startActivity(intent);
        }else {
            Intent intent = new Intent(context, EditUserInfoActivity.class);
            intent.putExtra("new_user", true);
            context.startActivity(intent);
        }
        ((LoginActivity)context).finish();
        unSubscribe();
    }


    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        subscriptions.clear();
    }
}
