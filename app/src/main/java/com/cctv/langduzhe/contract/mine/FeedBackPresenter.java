package com.cctv.langduzhe.contract.mine;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cctv.langduzhe.base.BasePresenter;
import com.cctv.langduzhe.data.http.ApiClient;
import com.cctv.langduzhe.data.http.RxSchedulerUtils;

import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by YinZhendong on 2018/3/7.
 */

public class FeedBackPresenter implements BasePresenter {
    private final FeedBackView feedBackView;
    private Context context;

    private CompositeSubscription subscriptions;

    public FeedBackPresenter(Context context, FeedBackView view) {
        feedBackView = view;
        this.context = context;
        this.subscriptions = new CompositeSubscription();
    }

    /**
     * @author 尹振东
     * create at 2018/2/11 下午3:17
     * 方法说明：获取验证码
     */
    public void feedback(String feedbackContent) {
        Observable<String> observable = ApiClient.apiService.collectionRead(feedbackContent);
        Subscription subscription = observable
                .compose(RxSchedulerUtils.normalSchedulersTransformer())
                .subscribe(this::handleResult, throwable -> {
                    Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
                });
        subscriptions.add(subscription);
    }

    private void handleResult(String s) {
        JSONObject jsonObject = JSONObject.parseObject(s);
        if (RESULT_OK.equals(jsonObject.getString("code"))) {
            feedBackView.submitSuccess();
        }
    }
    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }
}
