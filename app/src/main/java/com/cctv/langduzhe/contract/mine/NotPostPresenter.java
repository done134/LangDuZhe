package com.cctv.langduzhe.contract.mine;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cctv.langduzhe.base.BaseActivity;
import com.cctv.langduzhe.base.BasePresenter;
import com.cctv.langduzhe.data.db.NotPostDao;
import com.cctv.langduzhe.data.entites.NotPostEntity;
import com.cctv.langduzhe.data.http.ApiClient;
import com.cctv.langduzhe.data.http.RxSchedulerUtils;
import com.cctv.langduzhe.eventMsg.NewPosteventMsg;
import com.cctv.langduzhe.util.ToastUtils;
import com.cjt2325.cameralibrary.util.FileUtil;
import com.qiniu.android.utils.AsyncRun;


import org.greenrobot.eventbus.EventBus;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by gentleyin
 * on 2018/2/5.
 * 说明：未发布页面Presenter
 */
public class NotPostPresenter implements BasePresenter {

    private final NotPostView postView;
    private NotPostDao postDao;
    private Context context;
    private NotPostEntity postedEntity;

    private CompositeSubscription subscriptions;

    public NotPostPresenter(Context context, NotPostView view) {
        postView = view;
        this.context = context;
        postDao = new NotPostDao(context);
        this.subscriptions = new CompositeSubscription();
    }

    public void loadReads() {
        Subscription subscription = Observable.just(postDao.queryAll())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(postView::displayCities);
        subscriptions.add(subscription);
    }

    @Override
    public void subscribe() {
        loadReads();
    }

    @Override
    public void unSubscribe() {
        subscriptions.clear();
    }

    //删除
    public void delete(NotPostEntity postEntity) {
        postDao.deleteById(postEntity.id);
        FileUtil.deleteFile(postEntity.readFilepath);
        EventBus.getDefault().post(new NewPosteventMsg());
        loadReads();
    }

    //上传
    public void post(String fileName, NotPostEntity entity, int duration, long fileSize) {
        postedEntity = entity;
        String requestJson = handleRequestParams(fileName, entity,duration,fileSize);
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        requestJson);
        Observable<String> observable = ApiClient.apiService.submitMedia(requestBody);
        Subscription subscription = observable
                .compose(RxSchedulerUtils.normalSchedulersTransformer())
                .subscribe(this::handleUpResult, throwable -> {
                    Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
                });
        subscriptions.add(subscription);
    }

    private String handleRequestParams(String fileName, NotPostEntity entity, int duration, long fileSize) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("path", fileName);
        jsonObject.put("title", entity.readName);
        jsonObject.put("type", entity.type==0?"audio":"video");
        jsonObject.put("duration", duration);
        jsonObject.put("fileSize", fileSize);
        return jsonObject.toJSONString();
    }

    private void handleUpResult(String s) {
        JSONObject result = JSONObject.parseObject(s);
        if (result.getString("code").equals(RESULT_OK)) {
//            ((BaseActivity) context).finish();
            delete(postedEntity);
            postView.showToast("保存成功");
        } else {
            postView.showToast(result.getString("message"));
        }
    }


}
