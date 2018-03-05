package com.cctv.langduzhe.contract.readPavilion;

import android.content.Context;
import android.widget.Toast;

import com.cctv.langduzhe.base.BasePresenter;
import com.cctv.langduzhe.data.entites.CommandEntity;
import com.cctv.langduzhe.data.http.ApiClient;
import com.cctv.langduzhe.data.http.RxSchedulerUtils;
import com.cctv.langduzhe.util.JSON;

import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by gentleyin
 * on 2018/2/27.
 * 说明：
 */
public class ReadPavilionDetailPresenter implements BasePresenter {
    private Context context;
    private ReadPavilionDetailView readView;
    private CompositeSubscription subscriptions;

    public ReadPavilionDetailPresenter(Context context, ReadPavilionDetailView readView) {
        this.context = context;
        this.readView = readView;
        this.subscriptions = new CompositeSubscription();
    }

    @Override

    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }

    public void getCommentList(String mediaId, int pageNum) {

        Observable<String> observable = ApiClient.apiService.getCommentList(mediaId,pageNum,10);
        Subscription subscription = observable
                .compose(RxSchedulerUtils.normalSchedulersTransformer())
                .subscribe(this::handleComments, throwable -> {
                    Toast.makeText(context, "暂无评论", Toast.LENGTH_LONG).show();
                });
        subscriptions.add(subscription);
    }

    /**
     * @author 尹振东
     * create at 2018/2/16 上午11:26
     * 方法说明：获取评论列表
     */
    private void handleComments(String s) {
        CommandEntity commandEntity = JSON.parseObject(s, CommandEntity.class);
        if (commandEntity != null && RESULT_OK.equals(commandEntity.getCode())) {
            readView.setCommentList(commandEntity.getData());
        }else {
            readView.showToast("没有数据");
        }
    }
}
