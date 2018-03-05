package com.cctv.langduzhe.contract;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cctv.langduzhe.base.BasePresenter;
import com.cctv.langduzhe.data.http.ApiClient;
import com.cctv.langduzhe.data.http.RxSchedulerUtils;
import com.cctv.langduzhe.util.MD5Util;
import com.cctv.langduzhe.util.TimeCountUtils;

import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by gentleyin
 * on 2018/2/27.
 * 说明：
 */
public class CommentPresenter implements BasePresenter {

    private final CommentView loginView;
    private Context context;

    private CompositeSubscription subscriptions;

    public CommentPresenter(Context context, CommentView view) {
        loginView = view;
        this.context = context;
        this.subscriptions = new CompositeSubscription();
    }

    /**
     * @author 尹振东
     * create at 2018/2/11 下午3:17
     * 方法说明：获取验证码
     */
    public void submitComment(String mediaId, String commentStr) {
        String requestJson = handleRequestParams(mediaId, commentStr);
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        requestJson);
        Observable<String> observable = ApiClient.apiService.submitComment(requestBody);
        Subscription subscription = observable
                .compose(RxSchedulerUtils.normalSchedulersTransformer())
                .subscribe(this::handleResult, throwable -> {
                    Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
                });
        subscriptions.add(subscription);

    }

    private String handleRequestParams(String mediaId, String commentStr) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mediaId", mediaId);
        jsonObject.put("content", commentStr);
        return jsonObject.toJSONString();
    }

    private void handleResult(String s) {
        JSONObject jsonObject = JSONObject.parseObject(s);
        if (RESULT_OK.equals(jsonObject.getString("code"))) {
            loginView.setSuccess();
        }
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }
}
