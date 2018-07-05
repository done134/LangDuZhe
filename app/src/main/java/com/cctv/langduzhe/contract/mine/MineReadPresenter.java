package com.cctv.langduzhe.contract.mine;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cctv.langduzhe.R;
import com.cctv.langduzhe.base.BasePresenter;
import com.cctv.langduzhe.data.entites.HomeVideoEntity;
import com.cctv.langduzhe.data.http.ApiClient;
import com.cctv.langduzhe.data.http.RxSchedulerUtils;
import com.cctv.langduzhe.util.JSON;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by gentleyin
 * on 2018/4/25.
 * 说明：我的朗读已发布页面
 */
public class MineReadPresenter implements BasePresenter {
    private Context context;
    private MineReadView readView;
    private CompositeSubscription subscriptions;
    private AlertDialog dialog;

    public MineReadPresenter(Context context, MineReadView readView) {
        this.context = context;
        this.readView = readView;
        this.subscriptions = new CompositeSubscription();
    }



    /**
     * @author 尹振东
     * create at 2018/2/28 下午8:32
     * 方法说明：获取我的朗读列表
     */
    public void getMineReadList(int pageNum) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pageNum", pageNum);

        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        jsonObject.toJSONString());
        Observable<String> observable = ApiClient.apiService.getMineReadList(requestBody);
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
    private void handleCode(String result) {
        HomeVideoEntity homeVideoEntity = JSON.parseObject(result, HomeVideoEntity.class);
        if (homeVideoEntity != null && RESULT_OK.equals(homeVideoEntity.getCode())) {
            readView.setHomeMedias(homeVideoEntity.getData());
        } else {
            readView.showToast("没有数据");
        }
    }


    /**
     * @author 尹振东
     * create at 2018/3/20 下午10:47
     * 方法说明：删除朗读数据
     */
    private void delMineRead(String authCodeStr) {
        readView.showProgress();
        Observable<String> observable = ApiClient.apiService.delRead(authCodeStr);
        Subscription subscription = observable
                .compose(RxSchedulerUtils.normalSchedulersTransformer())
                .subscribe(this::handleDel, throwable -> {
                    readView.dismissProgress();
                    readView.showToast(throwable.getMessage());
                });
        subscriptions.add(subscription);
    }

    private void handleDel(String result) {
        readView.dismissProgress();
        JSONObject resultObj = JSONObject.parseObject(result);
        if (RESULT_OK.equals(resultObj.getString("code"))) {
            readView.setDelSuccess();
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } else {
            readView.showToast(resultObj.getString("message"));
        }
    }
    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }

    /**
    * @author 尹振东
    * create at 2018/4/25 下午8:20
    * 方法说明：删除确认弹窗
    */
    public void showConfirmDialog(Context context, String id) {
        if (dialog == null) {
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);
            view.findViewById(R.id.btn_confirm).setOnClickListener(v -> {
                delMineRead(id);
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            });
                    dialog = new AlertDialog.Builder(context, R.style.ios_bottom_dialog)
                            .setView(view)
                            .create();
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }
}
