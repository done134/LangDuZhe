package com.cctv.langduzhe.contract.articles;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.cctv.langduzhe.base.BasePresenter;
import com.cctv.langduzhe.data.entites.ThemeEntity;
import com.cctv.langduzhe.data.http.ApiClient;
import com.cctv.langduzhe.data.http.RxSchedulerUtils;
import com.cjt2325.cameralibrary.util.LogUtil;

import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by gentleyin
 * on 2018/3/19.
 * 说明：
 */
public class ThemesPresenter implements BasePresenter {
    private final ThemesView homeView;
    private Context context;

    private CompositeSubscription subscriptions;

    public ThemesPresenter(Context context, ThemesView view) {
        homeView = view;
        this.context = context;
        this.subscriptions = new CompositeSubscription();
    }
    /**
     * @author 尹振东
     * create at 2018/2/11 下午3:17
     * 方法说明：获取首页视频列表
     */
    public void getArticles(int pageNum) {
        Observable<String> observable = ApiClient.apiService.getThemeList(pageNum);
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
        LogUtil.i(result);
        ThemeEntity homeVideoEntity = JSON.parseObject(result, ThemeEntity.class);
        if (homeVideoEntity != null && RESULT_OK.equals(homeVideoEntity.getCode())) {
            homeView.setThemeList(homeVideoEntity.getData());
        }else {
            homeView.showToast("没有数据");
        }
    }

    @Override
    public void subscribe() {
        getArticles(0);
    }

    @Override
    public void unSubscribe() {
        subscriptions.clear();
    }
}
