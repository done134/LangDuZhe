package com.cctv.langduzhe.feature.readPavilion;

import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.cctv.langduzhe.contract.readPavilion.ReadPavilionPresenter;
import com.cctv.langduzhe.contract.readPavilion.ReadPavilionView;
import com.cctv.langduzhe.data.entites.HomeVideoEntity;
import com.cctv.langduzhe.data.entites.PavilionEntity;
import com.cctv.langduzhe.eventMsg.CollectEvent;
import com.cctv.langduzhe.util.ToastUtils;
import com.cctv.langduzhe.adapter.ReadPavilionAdapter;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by gentleyin
 * on 2018/1/21.
 * 说明：朗读亭展示列表Fragment
 */
public class ReadPavilionListFragment extends BaseFragment  implements PullLoadMoreRecyclerView.PullLoadMoreListener,
        BaseRecyclerViewAdapter.OnItemClickListener ,ReadPavilionView, LikeView{

    private int flag;
    private int pageNum;

    private ReadPavilionPresenter presenter;
    private LikePresenter likePresenter;
    private int oprPosition;
    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        if (args == null) {
            flag = 0;
        }else {
            flag = args.getInt("data_type", 0);
        }
    }

    @BindView(R.id.pull_refresh_list)
    PullLoadMoreRecyclerView pullRefreshList;
    Unbinder unbinder;
    private View mView;

    private ReadPavilionAdapter readPavilionAdapter;
    private String order;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //注意View对象的重复使用，以便节省资源
        if (mView == null) {
            mView = inflater.inflate(R.layout.only_list_layout, container, false);
        }

        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pullRefreshList.setLinearLayout();
        pullRefreshList.setOnPullLoadMoreListener(this);
        readPavilionAdapter = new ReadPavilionAdapter();
        pullRefreshList.setAdapter(readPavilionAdapter);
        readPavilionAdapter.setOnItemClickListener(this::onItemClick);
    }

    @Override
    public void requestData() {
        if (flag == 0) {
            order = "";
        } else if (flag == 1) {
            order = "hot";
        }else {
            order = "";
        }
        onRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(CollectEvent collectEvent) {
        onRefresh();
    }
    @Override
    public void onRefresh() {
        pageNum = 0;
        if (flag != 2) {
            presenter.getMediaList(pageNum, order);
        } else {
            presenter.getMineReadList(pageNum);
        }
    }

    @Override
    public void onLoadMore() {
        pageNum +=1;
        if (flag != 2) {
            presenter.getMediaList(pageNum, order);
        } else {
            presenter.getMineReadList(pageNum);
        }
        pullRefreshList.setPullLoadMoreCompleted();
        ToastUtils.showShort(context,"没有更多");
    }

    @Override
    public void onItemClick(int optType, int position,boolean yesOrNo) {
        oprPosition = position;
        if (optType == 1) {
            if (yesOrNo) {
                likePresenter.likeRead(readPavilionAdapter.getList().get(position).getId());
            }else {
                likePresenter.unlikeRead(readPavilionAdapter.getList().get(position).getId());
            }
        }else {
            Bundle bundle = new Bundle();
            bundle.putSerializable("video", readPavilionAdapter.getList().get(position));
            bundle.putString("type", readPavilionAdapter.getList().get(position).getType());
            toActivity(ReadPavilionDetailActivity.class, bundle);
        }
    }

    @Override
    public void setPresenter() {
        presenter = new ReadPavilionPresenter(getActivity(), this);
        likePresenter = new LikePresenter(getActivity(), this);
    }

    @Override
    public void setHomeMedias(List<HomeVideoEntity.DataBean> data) {
        if (data != null && data.size() > 0) {
            if (pageNum == 0) {
                readPavilionAdapter.setData(data);
            } else {
                readPavilionAdapter.addData(data);
            }
        } else {
            showToast("没有更多数据");
        }
        pullRefreshList.setPullLoadMoreCompleted();
    }

    @Override
    public void likeResult(boolean isLike) {
        readPavilionAdapter.getList().get(oprPosition).setIsCollect(isLike ? 1 : 0);
        readPavilionAdapter.notifyDataSetChanged();
    }
}
