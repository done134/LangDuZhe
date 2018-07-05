package com.cctv.langduzhe.feature.mine;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.adapter.MineReadAdapter;
import com.cctv.langduzhe.base.BaseFragment;
import com.cctv.langduzhe.base.BaseRecyclerViewAdapter;
import com.cctv.langduzhe.contract.mine.MineReadPresenter;
import com.cctv.langduzhe.contract.mine.MineReadView;
import com.cctv.langduzhe.data.entites.HomeVideoEntity;
import com.cctv.langduzhe.eventMsg.CollectEvent;
import com.cctv.langduzhe.eventMsg.NewPosteventMsg;
import com.cctv.langduzhe.feature.readPavilion.ReadPavilionDetailActivity;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.jzvd.JZVideoPlayer;

/**
 * Created by gentleyin
 * on 2018/4/25.
 * 说明：我的朗读，已发布页面
 */
public class MineReadFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, OnMoreListener,
        BaseRecyclerViewAdapter.OnItemClickListener, MineReadView {

    private int pageNum;

    private MineReadPresenter presenter;
    private int optPosition;

    @BindView(R.id.pull_refresh_list)
    SuperRecyclerView pullRefreshList;
    @BindView(R.id.tv_no_data)
    TextView tvNoData;
    Unbinder unbinder;
    private View mView;

    private MineReadAdapter mineReadAdapter;
    private LinearLayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //注意View对象的重复使用，以便节省资源
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_read_pavilion_list, container, false);
        }

        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, mView);
        mLayoutManager = new LinearLayoutManager(getActivity());

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pullRefreshList.setLayoutManager(mLayoutManager);
        pullRefreshList.setOnScrollListener(new RecyclerView.OnScrollListener() {

            int firstVisibleItem, lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                //大于0说明有播放
                if (mineReadAdapter.getPlayPosition() >= 0) {
                    //当前播放的位置
                    int position = mineReadAdapter.getPlayPosition();
                    //对应的播放列表TAG
                    if (position < firstVisibleItem || position > lastVisibleItem) {
                        //如果滑出去了上面和下面就是否
                        mineReadAdapter.pauseVideo();
                        JZVideoPlayer.releaseAllVideos();
                    }
                }
            }
        });

        pullRefreshList.setRefreshListener(this);
        pullRefreshList.setOnMoreListener(this);
        mineReadAdapter = new MineReadAdapter();
        pullRefreshList.setAdapter(mineReadAdapter);
        pullRefreshList.setRefreshingColorResources(R.color.ff2b2b, R.color.ff2b2b, R.color.ff2b2b, R.color.ff2b2b);
        mineReadAdapter.setOnItemClickListener(this);
    }


    @Override
    public void requestData() {
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
        if (collectEvent.type.equals("readPavilion")) {
            mineReadAdapter.getList().set(optPosition, collectEvent.collected);
            mineReadAdapter.notifyItemChanged(optPosition);
        }
    }
    @Subscribe
    public void onEvent(NewPosteventMsg collectEvent) {
        onRefresh();
    }

    @Override
    public void onRefresh() {
        pageNum = 0;
        presenter.getMineReadList(pageNum);
    }

    @Override
    public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
        pageNum += 1;
        presenter.getMineReadList(pageNum);
    }


    @Override
    public void onPause() {
        super.onPause();
        mineReadAdapter.pauseVideo();
    }

    @Override
    public void onItemClick(int optType, int position, boolean yesOrNo) {
        optPosition = position;
        if (optType == 1) {
            presenter.showConfirmDialog(getActivity(), mineReadAdapter.getList().get(position).getId());
        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable("video", mineReadAdapter.getList().get(position));
            bundle.putString("type", mineReadAdapter.getList().get(position).getType());
            toActivity(ReadPavilionDetailActivity.class, bundle);
        }
    }

    @Override
    public void setPresenter() {
        presenter = new MineReadPresenter(getActivity(), this);
    }

    @Override
    public void setHomeMedias(List<HomeVideoEntity.DataBean> data) {
        if (data != null && data.size() > 0) {
            showNodata(false);
            if (pageNum == 0) {
                mineReadAdapter.setData(data);
            } else {
                mineReadAdapter.addData(data);
            }
        } else {
            if (pageNum == 0) {
                showNodata(true);
            }
            showToast("没有更多数据");
        }
        dismissProgress();
        pullRefreshList.hideMoreProgress();
    }

    /**
    * @author 尹振东
    * create at 2018/4/25 下午8:10
    * 方法说明：删除成功
    */
    @Override
    public void setDelSuccess() {
        mineReadAdapter.getList().remove(optPosition);
        if(mineReadAdapter.getList()!=null&&mineReadAdapter.getList().size()>0) {
            mineReadAdapter.notifyDataSetChanged();
        }else {
            showNodata(true);
        }
    }

    private void showNodata(boolean noData) {
        tvNoData.setVisibility(noData ? View.VISIBLE : View.GONE);
        pullRefreshList.setVisibility(noData ? View.GONE : View.VISIBLE);
    }

}