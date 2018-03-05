package com.cctv.langduzhe.contract.mine;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cctv.langduzhe.base.BaseActivity;
import com.cctv.langduzhe.base.BasePresenter;
import com.cctv.langduzhe.data.http.ApiClient;
import com.cctv.langduzhe.data.http.RxSchedulerUtils;
import com.cctv.langduzhe.eventMsg.UpdateUserInfoEvent;
import com.cctv.langduzhe.feature.MainActivity;
import com.cjt2325.cameralibrary.util.LogUtil;

import org.greenrobot.eventbus.EventBus;

import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by gentleyin
 * on 2018/2/14.
 * 说明：
 */
public class EditUserInfoPresenter implements BasePresenter {
    private Context context;
    private EditUserInfoView infoView;
    private CompositeSubscription subscriptions;

    public EditUserInfoPresenter(Context context, EditUserInfoView view) {
        this.infoView = view;
        this.context = context;
        this.subscriptions = new CompositeSubscription();
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

    /**
    * @author 尹振东
    * create at 2018/2/17 下午5:58
    * 方法说明：用户头像和昵称
    }*/
    private void handleUserInfo(String userInfo) {
        LogUtil.i(userInfo);
        JSONObject result = JSONObject.parseObject(userInfo);
        if (result.getString("code").equals(RESULT_OK)) {
            JSONObject dataObj = result.getJSONObject("data");
            if (dataObj != null) {
                infoView.setUserInfo(dataObj);
            }
        }

    }

    public void saveUserInfo(String img, String username) {
        if (TextUtils.isEmpty(username)) {
            infoView.showToast("请输入昵称");
            return;
        }
        Observable<String> observable = ApiClient.apiService.saveUserInfo(img,username);
        Subscription subscription = observable
                .compose(RxSchedulerUtils.normalSchedulersTransformer())
                .subscribe(this::handleSaveResult, throwable -> {
                    Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
                });
        subscriptions.add(subscription);
    }

    private void handleSaveResult(String s) {
        LogUtil.i(s);
        JSONObject result = JSONObject.parseObject(s);
        if (result.getString("code").equals(RESULT_OK)) {
            ((BaseActivity)context).toActivity(MainActivity.class);
            EventBus.getDefault().post(new UpdateUserInfoEvent());
            infoView.showToast("保存成功");
        }else {
            infoView.showToast(result.getString("message"));
        }
    }


    @Override
    public void subscribe() {
        loadReadInfo();
    }

    @Override
    public void unSubscribe() {

    }
}
