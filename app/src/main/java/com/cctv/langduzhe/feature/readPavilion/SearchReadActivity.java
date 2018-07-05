package com.cctv.langduzhe.feature.readPavilion;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.adapter.ReadPavilionAdapter;
import com.cctv.langduzhe.base.BaseActivity;
import com.cctv.langduzhe.base.BaseRecyclerViewAdapter;
import com.cctv.langduzhe.contract.readPavilion.SearchReadPresenter;
import com.cctv.langduzhe.contract.readPavilion.SearchReadView;
import com.cctv.langduzhe.data.entites.HomeVideoEntity;
import com.cctv.langduzhe.eventMsg.CollectEvent;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gentleyin
 * on 2018/4/15.
 * 说明：
 */
public class SearchReadActivity extends BaseActivity implements PullLoadMoreRecyclerView.PullLoadMoreListener,
        BaseRecyclerViewAdapter.OnItemClickListener,SearchReadView {

    SearchReadPresenter presenter;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.pull_refresh_list)
    PullLoadMoreRecyclerView pullRefreshList;

    private int pageNum;
    private ReadPavilionAdapter readPavilionAdapter;

    private int optPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_read_pavilion);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        pullRefreshList.setLinearLayout();
        pullRefreshList.setOnPullLoadMoreListener(this);
        readPavilionAdapter = new ReadPavilionAdapter();
        pullRefreshList.setAdapter(readPavilionAdapter);
        readPavilionAdapter.setOnItemClickListener(this);

        setListener();
    }

    @Override
    public void setPresenter() {
        presenter = new SearchReadPresenter(this, this);
    }

    @OnClick({R.id.btn_search, R.id.btn_cancel,R.id.btn_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_search:
                search();
                break;
            case R.id.btn_cancel:
                onBackPressed();
                break;
            case R.id.btn_clear:
                etSearch.setText("");
                break;
        }
    }


    /**
    * @author 尹振东
    * create at 2018/4/15 下午5:17
    * 方法说明：给软键盘的搜索按钮添加点击事件监听
    */
    private void setListener() {
        etSearch.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                search();
            }
            return false;
        });
    }

    /**
     * @author 尹振东
     * create at 2018/4/14 下午7:26
     * 方法说明：搜索
     */
    private void search() {
        // 先隐藏键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);

        //非空判断
        String searchContext = etSearch.getText().toString().trim();
        if (TextUtils.isEmpty(searchContext)) {
            showToast("输入框为空，请输入");
        } else {
            // 调用搜索的API方法
            pageNum = 0;
            presenter.getMediaList(pageNum, searchContext);
            showProgress();
        }
    }

    @Subscribe
    public void onEvent(CollectEvent collectEvent) {
        if (collectEvent.type.equals("readPavilion")) {
            readPavilionAdapter.getList().set(optPosition, collectEvent.collected);
            readPavilionAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        pageNum = 0;
            presenter.getMediaList(pageNum, "");
    }

    @Override
    public void onLoadMore() {
        pageNum += 1;
            presenter.getMediaList(pageNum, "");
        pullRefreshList.setPullLoadMoreCompleted();
    }

    @Override
    public void onItemClick(int optType, int position, boolean yesOrNo) {
        optPosition = position;
            Bundle bundle = new Bundle();
            bundle.putSerializable("video", readPavilionAdapter.getList().get(position));
            bundle.putString("type", readPavilionAdapter.getList().get(position).getType());
            toActivity(ReadPavilionDetailActivity.class, bundle);
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
        dismissProgress();
        pullRefreshList.setPullLoadMoreCompleted();
    }
}
