package com.cctv.langduzhe.feature.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.cctv.langduzhe.R;
import com.cctv.langduzhe.base.BaseFragment;
import com.cctv.langduzhe.contract.mine.MinePresenter;
import com.cctv.langduzhe.contract.mine.MineView;
import com.cctv.langduzhe.eventMsg.UpdateUserInfoEvent;
import com.cctv.langduzhe.util.picasco.PicassoUtils;
import com.cctv.langduzhe.view.widget.CircleImageView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by gentleyin
 * on 2018/1/15.
 * 说明：我的fragment
 */
public class MineFragment extends BaseFragment implements MineView{
    @BindView(R.id.iv_user_picture)
    CircleImageView ivUserPicture;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.btn_my_collection)
    TextView btnMyCollection;
    @BindView(R.id.btn_my_read)
    TextView btnMyRead;
    @BindView(R.id.btn_about_us)
    TextView btnAboutUs;
    @BindView(R.id.btn_app_set)
    TextView btnAppSet;
    Unbinder unbinder;
    private View mView;
    private MinePresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //注意View对象的重复使用，以便节省资源
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_mine, container, false);
        }

        unbinder = ButterKnife.bind(this, mView);
        EventBus.getDefault().register(this);
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe
    public void onEvent(UpdateUserInfoEvent event) {
        presenter.loadReadInfo();
    }

    @Override
    public void requestData() {
        presenter.subscribe();
    }

    @OnClick({R.id.iv_user_picture, R.id.tv_user_name, R.id.btn_my_collection, R.id.btn_my_read, R.id.btn_about_us, R.id.btn_app_set})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_user_picture:
            case R.id.tv_user_name:
                toActivity(EditUserInfoActivity.class);
                break;
            case R.id.btn_my_collection:
                toActivity(MineCollectionActivity.class);
                break;
            case R.id.btn_my_read:
                toActivity(MineReadActivity.class);
                break;
            case R.id.btn_about_us:
                break;
            case R.id.btn_app_set:
                toActivity(SettingsActivity.class);
                break;
        }
    }

    @Override
    public void setPresenter() {
        presenter = new MinePresenter(getActivity(), this);
    }

    @Override
    public void setUserInfo(JSONObject dataObj) {
        String userImgUrl = dataObj.getString("img");
        String name = dataObj.getString("name");
        if (!TextUtils.isEmpty(userImgUrl)) {
            PicassoUtils.loadImageByurl(getActivity(),userImgUrl,ivUserPicture);
        }
        tvUserName.setText(name);

    }
}
