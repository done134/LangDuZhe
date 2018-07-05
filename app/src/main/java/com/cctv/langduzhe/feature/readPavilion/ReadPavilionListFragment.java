package com.cctv.langduzhe.feature.readPavilion;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.SimpleObserve;
import com.cctv.langduzhe.base.BaseFragment;
import com.cctv.langduzhe.base.BaseRecyclerViewAdapter;
import com.cctv.langduzhe.contract.LikePresenter;
import com.cctv.langduzhe.contract.LikeView;
import com.cctv.langduzhe.contract.readPavilion.ReadPavilionPresenter;
import com.cctv.langduzhe.contract.readPavilion.ReadPavilionView;
import com.cctv.langduzhe.data.entites.HomeVideoEntity;
import com.cctv.langduzhe.eventMsg.CollectEvent;
import com.cctv.langduzhe.feature.read.RecordVideoActivity;
import com.cctv.langduzhe.util.ToastUtils;
import com.cctv.langduzhe.adapter.ReadPavilionAdapter;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.jzvd.JZVideoPlayer;

/**
 * Created by gentleyin
 * on 2018/1/21.
 * 说明：朗读亭展示列表Fragment
 */
public class ReadPavilionListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,OnMoreListener,
        BaseRecyclerViewAdapter.OnItemClickListener, ReadPavilionView, LikeView {

    private int flag;
    private int pageNum;

    private ReadPavilionPresenter presenter;
    private LikePresenter likePresenter;
    private int optPosition;

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        if (args == null) {
            flag = 0;
        } else {
            flag = args.getInt("data_type", 0);
        }

    }

    @BindView(R.id.pull_refresh_list)
    SuperRecyclerView pullRefreshList;
    @BindView(R.id.tv_no_data)
    TextView tvNoData;
    ImageView btnAdd,btnSearch;
    Unbinder unbinder;
    private View mView;

    private ReadPavilionAdapter readPavilionAdapter;
    private LinearLayoutManager mLayoutManager;
    private String order;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //注意View对象的重复使用，以便节省资源
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_read_pavilion_list, container, false);
        }

        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, mView);
        btnAdd = getActivity().findViewById(R.id.btn_add_read);
        btnSearch = getActivity().findViewById(R.id.btn_search);
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
                if (readPavilionAdapter.getPlayPosition() >= 0) {
                    //当前播放的位置
                    int position = readPavilionAdapter.getPlayPosition();
                    //对应的播放列表TAG
                    if (position < firstVisibleItem || position > lastVisibleItem) {
                        //如果滑出去了上面和下面就是否
                        readPavilionAdapter.pauseVideo();
                        JZVideoPlayer.releaseAllVideos();
                    }
                }
            }
        });

        pullRefreshList.setRefreshListener(this);
        pullRefreshList.setOnMoreListener(this);
        readPavilionAdapter = new ReadPavilionAdapter();
        pullRefreshList.setAdapter(readPavilionAdapter);
        pullRefreshList.setRefreshingColorResources(R.color.ff2b2b,R.color.ff2b2b,R.color.ff2b2b,R.color.ff2b2b);
        readPavilionAdapter.setOnItemClickListener(this);
        if (btnAdd != null) {
            btnAdd.setOnClickListener(v -> {
                if (hasLogin()) {
                    presenter.showAuthDialog(getActivity());
                }
            });
        }
        if (btnSearch != null) {
            btnSearch.setOnClickListener(v -> toActivity(SearchReadActivity.class));
        }
    }


    @Override
    public void requestData() {
        if (flag == 0) {
            order = "";
        } else if (flag == 1) {
            order = "hot";
        } else {
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
        if (collectEvent.type.equals("readPavilion")) {
            readPavilionAdapter.getList().set(optPosition, collectEvent.collected);
            readPavilionAdapter.notifyItemChanged(optPosition);
        }
    }

    @Override
    public void onRefresh() {
        pageNum = 0;
        if (flag != 2) {
            presenter.getMediaList(pageNum, order, "");
        } else {
            presenter.getMineReadList(pageNum);
        }
    }

    @Override
    public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition){
        pageNum += 1;
        if (flag != 2) {
            presenter.getMediaList(pageNum, order, "");
        } else {
            presenter.getMineReadList(pageNum);
        }
//        pullRefreshList.setLoadingMore(false);
//        ToastUtils.showShort(context, "没有更多");
    }


    @Override
    public void onPause() {
        super.onPause();
        readPavilionAdapter.pauseVideo();
    }

    @Override
    public void onItemClick(int optType, int position, boolean yesOrNo) {
        optPosition = position;
        if (optType == 1) {
            if (yesOrNo) {
                likePresenter.likeRead(readPavilionAdapter.getList().get(position).getId());
            } else {
                likePresenter.unlikeRead(readPavilionAdapter.getList().get(position).getId());
            }
        } else {
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
            showNodata(false);
            if (pageNum == 0) {
                readPavilionAdapter.setData(data);
            } else {
                readPavilionAdapter.addData(data);
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

    private void showNodata(boolean noData) {
        tvNoData.setVisibility(noData?View.VISIBLE:View.GONE);
        pullRefreshList.setVisibility(noData?View.GONE:View.VISIBLE);
    }
    @Override
    public void setAuthSuccess(String authCodeStr) {
        RxPermissions rxPermissions = new RxPermissions(getActivity());
        rxPermissions.request(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO)
                .subscribe(new SimpleObserve() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            Bundle bundle = new Bundle();
                            bundle.putString("authCode", authCodeStr);
                            toActivity(RecordVideoActivity.class, bundle);
                        } else {
                            ToastUtils.showShort(getActivity(), "需要相应的权限");
                        }
                    }
                });

    }

    @Override
    public void likeResult(boolean isLike) {
        int likeSum = readPavilionAdapter.getList().get(optPosition).getLikeSum();
        if (isLike) {
            readPavilionAdapter.getList().get(optPosition).setLikeSum(likeSum + 1);
        } else {
            readPavilionAdapter.getList().get(optPosition).setLikeSum(likeSum - 1);
        }
        readPavilionAdapter.getList().get(optPosition).setIsLike(isLike ? 1 : 0);
        readPavilionAdapter.notifyItemChanged(optPosition);
    }
}
