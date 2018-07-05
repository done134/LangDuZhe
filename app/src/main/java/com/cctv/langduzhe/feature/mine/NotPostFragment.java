package com.cctv.langduzhe.feature.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.adapter.NotPostAdapter;
import com.cctv.langduzhe.base.BaseFragment;
import com.cctv.langduzhe.contract.PostPresenter;
import com.cctv.langduzhe.contract.PostView;
import com.cctv.langduzhe.contract.mine.NotPostPresenter;
import com.cctv.langduzhe.contract.mine.NotPostView;
import com.cctv.langduzhe.data.entites.NotPostEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.jzvd.JZVideoPlayer;

/**
 * Created by gentleyin
 * on 2018/2/4.
 * 说明：我的阅读未发布列表页
 */
public class NotPostFragment extends BaseFragment implements NotPostAdapter.OnButtonClickListener, NotPostView, PostView {
    @BindView(R.id.pull_refresh_list)
    RecyclerView pullRefreshList;
    @BindView(R.id.tv_no_data)
    TextView tvNoData;
    Unbinder unbinder;
    private View mView;

    private NotPostAdapter notPostAdapter;
    private NotPostPresenter presenter;
    private NotPostEntity postEntity;
    private PostPresenter postPresenter;

    private LinearLayoutManager mLayoutManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //注意View对象的重复使用，以便节省资源
        if (mView == null) {
            mView = inflater.inflate(R.layout.only_list_layout, container, false);
        }

        unbinder = ButterKnife.bind(this, mView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        pullRefreshList.setLayoutManager(mLayoutManager);
        notPostAdapter = new NotPostAdapter(this);
        pullRefreshList.setAdapter(notPostAdapter);
//        notPostAdapter.setOnItemClickListener(this::onItemClick);
        pullRefreshList.addOnScrollListener(new RecyclerView.OnScrollListener() {

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
                if (notPostAdapter.getPlayPosition() >= 0) {
                    //当前播放的位置
                    int position = notPostAdapter.getPlayPosition();
                    //对应的播放列表TAG
                    if (position < firstVisibleItem || position > lastVisibleItem) {
                        //如果滑出去了上面和下面就是否
                        notPostAdapter.pauseVoice();
                        JZVideoPlayer.releaseAllVideos();
                    }
                }
            }
        });

        return mView;
    }

    @Override
    public void requestData() {
        presenter.subscribe();
    }

    @Override
    public void onDeleteClick(NotPostEntity postEntity) {
        presenter.delete(postEntity);
    }

    @Override
    public void onPostClick(NotPostEntity postEntity) {
        if (postEntity.type == 0) {
            postPresenter.postFile(PostPresenter.VOICE_TYPE, postEntity.readFilepath);
        }else {
            postPresenter.postFile(PostPresenter.VIDEO_TYPE, postEntity.readFilepath, postEntity.display);
        }
        JZVideoPlayer.releaseAllVideos();
        notPostAdapter.pauseVoice();
        this.postEntity = postEntity;
        showProgress();
    }

    @Override
    public void setPresenter() {
        presenter = new NotPostPresenter(getActivity(), this);
        postPresenter = new PostPresenter(getActivity(), this);
    }

    @Override
    public void displayCities(List<NotPostEntity> list) {
        if (list == null || list.size() == 0) {
            tvNoData.setVisibility(View.VISIBLE);
            pullRefreshList.setVisibility(View.GONE);
        } else {
            notPostAdapter.setList(list);
            tvNoData.setVisibility(View.GONE);
            pullRefreshList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        presenter.unSubscribe();
    }

    @Override
    public void postSucceed(String fileName, int duration, long fileSize) {
        presenter.post(fileName, postEntity, duration, fileSize);
        dismissProgress();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            notPostAdapter.pauseVoice();
            JZVideoPlayer.releaseAllVideos();
        }
    }
}
