package com.cctv.langduzhe.feature.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.base.BaseFragment;
import com.cctv.langduzhe.base.BaseRecyclerViewAdapter;
import com.cctv.langduzhe.contract.LikePresenter;
import com.cctv.langduzhe.contract.LikeView;
import com.cctv.langduzhe.contract.home.HomePresenter;
import com.cctv.langduzhe.contract.home.HomeView;
import com.cctv.langduzhe.data.entites.HomeVideoEntity;
import com.cctv.langduzhe.adapter.HomeVideoAdapter;
import com.cctv.langduzhe.util.ToastUtils;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by gentleyin
 * on 2018/1/15.
 * 说明：首页fragment
 */
public class HomeFragment extends BaseFragment implements
        PullLoadMoreRecyclerView.PullLoadMoreListener,BaseRecyclerViewAdapter.OnItemClickListener,HomeView,LikeView {
    @BindView(R.id.rv_home_list)
    PullLoadMoreRecyclerView rvHomeList;
    Unbinder unbinder;

    private int pageNum;
    private View mView;
    private HomePresenter presenter;
    private LikePresenter likePresenter;

    private HomeVideoAdapter homeVideoAdapter;
    private int optPostion;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //注意View对象的重复使用，以便节省资源
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_home, container, false);
        }

        unbinder = ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvHomeList.setLinearLayout();
        rvHomeList.setOnPullLoadMoreListener(this);
        homeVideoAdapter = new HomeVideoAdapter();
        homeVideoAdapter.setOnItemClickListener(this);
        rvHomeList.setAdapter(homeVideoAdapter);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void requestData() {
        presenter.subscribe();
    }

    @Override
    public void onRefresh() {
        pageNum = 0;
        presenter.getMediaList(pageNum);
    }

    @Override
    public void onLoadMore() {
        pageNum = pageNum + 1;
        presenter.getMediaList(pageNum);
    }

    @Override
    public void onItemClick(int optType, int position, boolean yesOrNo) {
        optPostion = position;
        if (optType == 1) {
            String mediasId = homeVideoAdapter.getItemInPosition(position).getId();
            if (yesOrNo) {
                likePresenter.likeRead(mediasId);
            } else {
                likePresenter.unlikeRead(mediasId);
            }
        }else {
            Bundle bundle = new Bundle();
            bundle.putSerializable("home_video",homeVideoAdapter.getItemInPosition(position));
            if (optType == 0) {
                bundle.putBoolean("showComment", true);
            }
            toActivity(HomeVideoDetailActivity.class,bundle);
        }

    }

    @Override
    public void setPresenter() {
        presenter = new HomePresenter(getActivity(), this);
        likePresenter = new LikePresenter(getActivity(), this);
    }

    @Override
    public void setHomeMedias(List<HomeVideoEntity.DataBean> list) {
        if (list != null && list.size() > 0) {
            if (pageNum == 0) {
                homeVideoAdapter.setData(list);
            }else {
                homeVideoAdapter.addData(list);
            }
        }else {
            ToastUtils.showLong(getActivity(), "数据已全部加载");
            rvHomeList.setPullLoadMoreCompleted();
        }
        rvHomeList.setPullLoadMoreCompleted();
    }

    @Override
    public void likeResult(boolean isLike) {
        homeVideoAdapter.getItemInPosition(optPostion).setIsCollect(isLike ? 1 : 0);
        homeVideoAdapter.notifyDataSetChanged();
    }

}
