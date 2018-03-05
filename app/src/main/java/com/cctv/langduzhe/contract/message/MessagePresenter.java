package com.cctv.langduzhe.contract.message;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cctv.langduzhe.base.BasePresenter;
import com.cctv.langduzhe.contract.LoginView;
import com.cctv.langduzhe.data.http.ApiClient;
import com.cctv.langduzhe.data.http.RxSchedulerUtils;
import com.cctv.langduzhe.util.MD5Util;
import com.cctv.langduzhe.util.TimeCountUtils;
import com.cjt2325.cameralibrary.util.LogUtil;

import java.util.UUID;

import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by gentleyin
 * on 2018/2/16.
 * 说明：
 */
public class MessagePresenter implements BasePresenter {

    private final MessageView messageView;
    private Context context;

    private CompositeSubscription subscriptions;

    public MessagePresenter(Context context, MessageView view) {
        messageView = view;
        this.context = context;
        this.subscriptions = new CompositeSubscription();
    }

    /**
     * @author 尹振东
     * create at 2018/2/11 下午3:17
     * 方法说明：获取消息列表
     */
    public void getMessageList(int pageNum) {
        Observable<String> observable = ApiClient.apiService.getMessageList(pageNum);
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
        LogUtil.i(jsonObject.getString("code"));
    }
    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }
}
