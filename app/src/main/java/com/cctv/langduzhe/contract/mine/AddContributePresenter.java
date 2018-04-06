package com.cctv.langduzhe.contract.mine;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cctv.langduzhe.base.BasePresenter;
import com.cctv.langduzhe.data.http.ApiClient;
import com.cctv.langduzhe.data.http.RxSchedulerUtils;
import com.cjt2325.cameralibrary.util.LogUtil;

import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by gentleyin
 * on 2018/3/18.
 * 说明：
 */
public class AddContributePresenter implements BasePresenter {
    private Context context;
    private AddContributeView mineView;
    private CompositeSubscription subscriptions;

    public AddContributePresenter(Context context, AddContributeView view) {
        this.mineView = view;
        this.context = context;
        this.subscriptions = new CompositeSubscription();
    }


    public void submitContribute(String contributeTitle, String contributeContent) {
        Observable<String> observable = ApiClient.apiService.submitContribute(contributeTitle,contributeContent);
        Subscription subscription = observable
                .compose(RxSchedulerUtils.normalSchedulersTransformer())
                .subscribe(this::handleUserInfo, throwable -> {
                    Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
                });
        subscriptions.add(subscription);
    }

    private void handleUserInfo(String userInfo) {
        LogUtil.i(userInfo);
        JSONObject result = JSONObject.parseObject(userInfo);
        if (result.getString("code").equals(RESULT_OK)) {
            mineView.submitSuccess();
        }

    }



    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }

}
