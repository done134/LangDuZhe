package com.cctv.langduzhe.contract.readPavilion;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cctv.langduzhe.R;
import com.cctv.langduzhe.base.BasePresenter;
import com.cctv.langduzhe.data.entites.HomeVideoEntity;
import com.cctv.langduzhe.data.http.ApiClient;
import com.cctv.langduzhe.data.http.RxSchedulerUtils;
import com.cctv.langduzhe.util.JSON;
import com.cctv.langduzhe.util.ToastUtils;
import com.cjt2325.cameralibrary.util.LogUtil;

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
public class ReadPavilionPresenter implements BasePresenter {
    private Context context;
    private ReadPavilionView readView;
    private CompositeSubscription subscriptions;
    private AlertDialog dialog;
    private String authCodeStr;

    public ReadPavilionPresenter(Context context, ReadPavilionView readView) {
        this.context = context;
        this.readView = readView;
        this.subscriptions = new CompositeSubscription();
    }

    /**
     * @author 尹振东
     * create at 2018/2/11 下午3:17
     * 方法说明：获取首页视频列表
     */
    public void getMediaList(int pageNum, String order, String searchStr) {
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        handleRequestParams(pageNum, order, searchStr));
        Observable<String> observable = ApiClient.apiService.getMediaList(requestBody);
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

    private String handleRequestParams(int pageNum, String order, String searchStr) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pageNum", pageNum);
        jsonObject.put("pageSize", 10);
        jsonObject.put("mediaType", "video");
        if (!TextUtils.isEmpty(order)) {
            jsonObject.put("order", order);
        }
        if (!TextUtils.isEmpty(searchStr)) {
            jsonObject.put("search", searchStr);
        }
        jsonObject.put("readerType", "ordinary");
        return jsonObject.toJSONString();
    }

    @Override

    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

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
     * create at 2018/3/20 下午10:17
     * 方法说明：展示输入授权码弹窗
     */
    public void showAuthDialog(Context context) {
        if (dialog == null) {
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_input_author_code, null);
            EditText etAuthCode = view.findViewById(R.id.et_author_code);
            view.findViewById(R.id.btn_confirm_author_code).setOnClickListener(v -> {
                String authCodeStr = etAuthCode.getText().toString().trim();
                if (TextUtils.isEmpty(authCodeStr)) {
                    ToastUtils.showLong(context, "请输入授权码");
                } else {
                    authCode(authCodeStr);
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

    /**
     * @author 尹振东
     * create at 2018/3/20 下午10:47
     * 方法说明：验证授权码
     */
    private void authCode(String authCodeStr) {
        readView.showProgress();
        this.authCodeStr = authCodeStr;
        Observable<String> observable = ApiClient.apiService.checkAuthCode(authCodeStr);
        Subscription subscription = observable
                .compose(RxSchedulerUtils.normalSchedulersTransformer())
                .subscribe(this::handleAuthCode, throwable -> {
                    readView.dismissProgress();
                    readView.showToast(throwable.getMessage());
                });
        subscriptions.add(subscription);
    }

    private void handleAuthCode(String result) {
        readView.dismissProgress();
        JSONObject resultObj = JSONObject.parseObject(result);
        if (RESULT_OK.equals(resultObj.getString("code"))) {
            readView.setAuthSuccess(authCodeStr);
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } else {
            readView.showToast(resultObj.getString("message"));
        }
    }

}
