package com.cctv.langduzhe.contract.read;

import android.content.Context;

import com.cctv.langduzhe.base.BasePresenter;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by gentleyin
 * on 2018/3/29.
 * 说明：朗读文字页面Presenter
 */
public class ReadArticlePresenter implements BasePresenter {

    private Context context;
    private ReadArticleView mineView;
    private CompositeSubscription subscriptions;

    public ReadArticlePresenter(Context context, ReadArticleView view) {
        this.mineView = view;
        this.context = context;
        this.subscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }
}
