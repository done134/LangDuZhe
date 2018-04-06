package com.cctv.langduzhe.feature.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.adapter.ContentPagerAdapter;
import com.cctv.langduzhe.adapter.MineContributeAdapter;
import com.cctv.langduzhe.base.BaseActivity;
import com.cctv.langduzhe.base.BasePresenter;
import com.cctv.langduzhe.base.BaseRecyclerViewAdapter;
import com.cctv.langduzhe.contract.mine.MineContributePresenter;
import com.cctv.langduzhe.contract.mine.MineContributeView;
import com.cctv.langduzhe.data.entites.ContributeEntity;
import com.cctv.langduzhe.eventMsg.ContributeEvent;
import com.cctv.langduzhe.util.ToastUtils;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gentleyin
 * on 2018/3/17.
 * 说明：我的投稿列表页
 */
public class MineContributeActivity extends BaseActivity implements MineContributeView,
        PullLoadMoreRecyclerView.PullLoadMoreListener {


    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.btn_image_right)
    ImageView btnImageRight;
    @BindView(R.id.tv_no_data)
    TextView tvNoData;
    @BindView(R.id.rv_contribute_list)
    PullLoadMoreRecyclerView rvContributeList;
    private MineContributePresenter presenter;

    private int pageNum;
    private MineContributeAdapter contentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_contribute);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
        presenter.subscribe();
    }

    private void initView() {
        tvTitle.setText("我的投稿");
        btnImageRight.setVisibility(View.VISIBLE);
        rvContributeList.setLinearLayout();
        rvContributeList.setOnPullLoadMoreListener(this);
        contentPagerAdapter = new MineContributeAdapter();
        rvContributeList.setAdapter(contentPagerAdapter);
    }

    @Override
    public void setPresenter() {
        presenter = new MineContributePresenter(this, this);
    }

    @Override
    public void onRefresh() {
        pageNum = 0;
        presenter.loadMineContribute(pageNum);
    }

    @Override
    public void onLoadMore() {
        pageNum += 1;
        presenter.loadMineContribute(pageNum);
    }

    @OnClick({R.id.btn_back, R.id.btn_image_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_image_right:
                toActivity(AddContributeActivity.class);
                break;
        }
    }

    @Subscribe
    public void onEvent(ContributeEvent event) {
        onRefresh();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void setContributeList(ContributeEntity contributeEntity) {
        if (contributeEntity != null) {
            if (BasePresenter.RESULT_OK.equals(contributeEntity.getCode())) {
                if (pageNum == 0) {
                    if (contributeEntity.getData() == null || contributeEntity.getData().size() == 0) {
                        showNodata();
                    }else {
                        contentPagerAdapter.setList(contributeEntity.getData());
                    }
                } else {
                    if (contributeEntity.getData() == null || contributeEntity.getData().size() == 0) {
                        ToastUtils.showLong(this,"没有更多数据");
                    }else {
                        contentPagerAdapter.addList(contributeEntity.getData());
                    }
                }
            } else {
                ToastUtils.showLong(this,contributeEntity.getMessage());
                showNodata();
            }
        } else {
            showNodata();
        }
        rvContributeList.setPullLoadMoreCompleted();
    }

    private void showNodata() {
        tvNoData.setVisibility(View.VISIBLE);
        rvContributeList.setVisibility(View.GONE);
    }
}
