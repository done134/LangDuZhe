package com.cctv.langduzhe.contract.mine;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cctv.langduzhe.base.BaseActivity;
import com.cctv.langduzhe.base.BasePresenter;
import com.cctv.langduzhe.data.http.ApiClient;
import com.cctv.langduzhe.data.http.RxSchedulerUtils;
import com.cjt2325.cameralibrary.util.LogUtil;

import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by gentleyin
 * on 2018/2/17.
 * 说明：
 */
public class MinePresenter implements BasePresenter {
    private Context context;
    private MineView mineView;
    private CompositeSubscription subscriptions;

    public MinePresenter(Context context, MineView view) {
        this.mineView = view;
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
     * /*{
    "code":"K-000000",
    "data":{
    "id":"71cf35e687e54b328238c197cf191ca5",
    "img":"https://pbs.twimg.com/media/DUh2r-HUMAU4UjV.jpg",
    "mobile":"13263426383",
    "name":"YZD"
    }
    }*/
    private void handleUserInfo(String userInfo) {
        LogUtil.i(userInfo);
        JSONObject result = JSONObject.parseObject(userInfo);
        if (result.getString("code").equals(RESULT_OK)) {
            JSONObject dataObj = result.getJSONObject("data");
            if (dataObj != null) {
                mineView.setUserInfo(dataObj);
            }
        }

    }

    public void saveUserInfo(String img, String username) {
        if (TextUtils.isEmpty(username)) {
            mineView.showToast("请输入昵称");
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
            ((BaseActivity)context).finish();
            mineView.showToast("保存成功");
        }else {
            mineView.showToast(result.getString("message"));
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
