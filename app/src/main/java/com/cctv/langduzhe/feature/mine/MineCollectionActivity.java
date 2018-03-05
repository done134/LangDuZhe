package com.cctv.langduzhe.feature.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.adapter.MyCollectionAdapter;
import com.cctv.langduzhe.base.BaseActivity;
import com.cctv.langduzhe.base.BaseRecyclerViewAdapter;
import com.cctv.langduzhe.contract.CollectPresenter;
import com.cctv.langduzhe.contract.CollectView;
import com.cctv.langduzhe.contract.mine.MineCollectPresenter;
import com.cctv.langduzhe.contract.mine.MineCollectView;
import com.cctv.langduzhe.data.entites.HomeVideoEntity;
import com.cctv.langduzhe.data.entites.PavilionEntity;
import com.cctv.langduzhe.eventMsg.CollectEvent;
import com.cctv.langduzhe.feature.readPavilion.ReadPavilionDetailActivity;
import com.cctv.langduzhe.util.ToastUtils;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gentleyin
 * on 2018/2/2.
 * 说明：我的收藏页面
 */
public class MineCollectionActivity extends BaseActivity implements BaseRecyclerViewAdapter.OnItemClickListener,
        PullLoadMoreRecyclerView.PullLoadMoreListener ,MineCollectView,CollectView{

    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.btn_delete)
    TextView btnDelete;
    @BindView(R.id.btn_select_all)
    TextView btnSelectAll;
    @BindView(R.id.ll_option)
    LinearLayout llOption;
    @BindView(R.id.rv_mine_collection)
    PullLoadMoreRecyclerView rvMineCollection;

    /**
     * 是否是编辑状态
     */
    private boolean isEditStatus;
    private int pageNum;
    private MyCollectionAdapter myCollectionAdapter;

    private CollectPresenter collectPresenter;
    private MineCollectPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_collection);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();

    }

    private void initView() {
        tvTitle.setText("我的收藏");
        tvRight.setText("编辑");
        rvMineCollection.setLinearLayout();
        myCollectionAdapter = new MyCollectionAdapter();
        myCollectionAdapter.setOnItemClickListener(this::onItemClick);
        rvMineCollection.setAdapter(myCollectionAdapter);
        rvMineCollection.setOnPullLoadMoreListener(this);
        onRefresh();

    }

    @Override
    public void onItemClick(int optType, int position, boolean yseOrNo) {
        Bundle bundle = new Bundle();
        bundle.putString("type",myCollectionAdapter.getItemInPosition(position).getType());
        bundle.putSerializable("video", myCollectionAdapter.getItemInPosition(position));
        toActivity(ReadPavilionDetailActivity.class,bundle);
    }

    @Override
    public void onRefresh() {
        pageNum = 0;
        presenter.loadReadInfo(pageNum);
    }

    @Override
    public void onLoadMore() {
        pageNum+=1;
        presenter.loadReadInfo(pageNum);
    }

    @OnClick({R.id.btn_back, R.id.tv_right,R.id.btn_delete,R.id.btn_select_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.tv_right:
                if (isEditStatus) {
                    isEditStatus = false;
                    tvRight.setText("编辑");
                }else {
                    isEditStatus = true;
                    tvRight.setText("完成");
                    llOption.setVisibility(View.VISIBLE);
                }
                myCollectionAdapter.setEditStatus(isEditStatus);
                break;
            case R.id.btn_select_all:
                myCollectionAdapter.selectAll();
                break;
            case R.id.btn_delete:
                deleteSelected();
                break;
        }
    }

    private void deleteSelected() {
        List<HomeVideoEntity.DataBean> selectList =  myCollectionAdapter.getSelectList();
        StringBuilder mediaIds = new StringBuilder();
        for (int i = 0; i < selectList.size(); i++) {
            mediaIds.append(selectList.get(i).getId()).append(",");
        }
        collectPresenter.deleteCollect(mediaIds.toString());
    }

    @Override
    public void onBackPressed() {
        if (isEditStatus) {
            isEditStatus = false;
            tvRight.setText("编辑");
            myCollectionAdapter.setEditStatus(isEditStatus);
            llOption.setVisibility(View.GONE);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public void setPresenter() {
        presenter = new MineCollectPresenter(this, this);
        collectPresenter = new CollectPresenter(this, this);
    }

    @Override
    public void deleteSucceed() {

        isEditStatus = false;
        tvRight.setText("编辑");
        myCollectionAdapter.setEditStatus(isEditStatus);
        llOption.setVisibility(View.GONE);
        onRefresh();
    }


    @Subscribe
    public void onEvent(CollectEvent collectEvent) {
        onRefresh();
    }
    @Override
    public void collectSucceed() {
        onRefresh();
    }

    @Override
    public void setMediaList(List<HomeVideoEntity.DataBean> list) {
        if (list != null && list.size() > 0) {
            if (pageNum == 0) {
                myCollectionAdapter.setData(list);
            }else {
                myCollectionAdapter.addData(list);
            }
        }else {
            ToastUtils.showLong(this, "数据已全部加载");
            rvMineCollection.setPullLoadMoreCompleted();
        }
        rvMineCollection.setPullLoadMoreCompleted();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
