package com.cctv.langduzhe.feature.articles;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.adapter.ArticlesAdapter;
import com.cctv.langduzhe.base.BaseActivity;
import com.cctv.langduzhe.base.BaseRecyclerViewAdapter;
import com.cctv.langduzhe.contract.articles.ArticleListPresenter;
import com.cctv.langduzhe.contract.articles.ArticleListView;
import com.cctv.langduzhe.data.entites.ArticlesEntity;
import com.cctv.langduzhe.data.entites.ThemeEntity;
import com.cctv.langduzhe.util.ToastUtils;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gentleyin
 * on 2018/3/19.
 * 说明：文字列表页
 */
public class ArticleListActivity extends BaseActivity implements ArticleListView, PullLoadMoreRecyclerView.PullLoadMoreListener, BaseRecyclerViewAdapter.OnItemClickListener {

    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_articles_list)
    PullLoadMoreRecyclerView rvArticlesList;
    private ArticleListPresenter presenter;
    private int pageNum;
    private String themeId;
    private ArticlesAdapter articlesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);
        ButterKnife.bind(this);
        getIntentData();
        initView();
        onRefresh();
    }

    private void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ThemeEntity.DataBean themeEntity = (ThemeEntity.DataBean) bundle.getSerializable("theme");
            themeId = themeEntity.getId();
            tvTitle.setText(themeEntity.getTitle());
        }
    }

    private void initView() {
        rvArticlesList.setLinearLayout();
        rvArticlesList.setOnPullLoadMoreListener(this);
        articlesAdapter = new ArticlesAdapter(this);
        articlesAdapter.setOnItemClickListener(this);
        rvArticlesList.setAdapter(articlesAdapter);
        btnBack.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void setPresenter() {
        presenter = new ArticleListPresenter(this, this);
    }

    @Override
    public void onItemClick(int optType, int position, boolean yesOrNo) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("article",articlesAdapter.getList().get(position));
        toActivity(ArticleDetailActivity.class,bundle);
    }

    @Override
    public void onRefresh() {
        pageNum = 0;
        presenter.getArticles(pageNum,themeId);
    }

    @Override
    public void onLoadMore() {
        pageNum +=1;
        presenter.getArticles(pageNum,themeId);
    }

    @Override
    public void setArticleList(List<ArticlesEntity.DataBean> list) {
        if (list != null && list.size() > 0) {
            if (pageNum == 0) {
                articlesAdapter.setList(list);
            } else {
                articlesAdapter.addList(list);
            }
        } else {
            ToastUtils.showLong(this, "数据已全部加载");
        }
        rvArticlesList.setPullLoadMoreCompleted();
    }
}
