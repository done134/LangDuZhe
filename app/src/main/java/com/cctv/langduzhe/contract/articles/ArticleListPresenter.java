package com.cctv.langduzhe.contract.articles;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.cctv.langduzhe.base.BasePresenter;
import com.cctv.langduzhe.data.entites.ArticlesEntity;
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
 * 说明：文字列表Presenter
 */
public class ArticleListPresenter implements BasePresenter {
    private final ArticleListView view;
    private Context context;

    private CompositeSubscription subscriptions;

    public ArticleListPresenter(Context context, ArticleListView view) {
        this.view = view;
        this.context = context;
        this.subscriptions = new CompositeSubscription();
    }
    /**
     * @author 尹振东
     * create at 2018/2/11 下午3:17
     * 方法说明：获取首页视频列表
     */
    public void getArticles(int pageNum,String themeId) {
        Observable<String> observable = ApiClient.apiService.getArticleList(pageNum,themeId);
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
        ArticlesEntity homeVideoEntity = JSON.parseObject(result, ArticlesEntity.class);
        if (homeVideoEntity != null && RESULT_OK.equals(homeVideoEntity.getCode())) {
            view.setArticleList(homeVideoEntity.getData());
        }else {
            view.showToast("没有数据");
        }
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        subscriptions.clear();
    }
}
