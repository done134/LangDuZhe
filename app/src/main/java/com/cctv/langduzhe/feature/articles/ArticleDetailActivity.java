package com.cctv.langduzhe.feature.articles;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.base.BaseActivity;
import com.cctv.langduzhe.contract.articles.ArticleDetailPresenter;
import com.cctv.langduzhe.contract.articles.ArticleDetailView;
import com.cctv.langduzhe.data.entites.ArticlesEntity;
import com.cctv.langduzhe.data.entites.CommandEntity;
import com.cctv.langduzhe.data.entites.HomeVideoEntity;
import com.cctv.langduzhe.feature.read.ReadArticleActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gentleyin
 * on 2018/3/19.
 * 说明：文字详情页面
 */
public class ArticleDetailActivity extends BaseActivity implements ArticleDetailView {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_article_cover)
    ImageView ivArticleCover;
    @BindView(R.id.tv_article_content)
    TextView tvArticleContent;
    @BindView(R.id.rv_media_list)
    RecyclerView rvMediaList;
    private ArticleDetailPresenter presenter;
    private String articleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        ButterKnife.bind(this);
        getIntentData();
    }

    private void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ArticlesEntity.DataBean article = (ArticlesEntity.DataBean) bundle.getSerializable("article");
            articleId = article.getId();
            tvTitle.setText(article.getTitle());
            presenter.getArticleDetail(articleId);
            presenter.getMediaList(articleId,0);
        }

    }

    @Override
    public void setPresenter() {
        presenter = new ArticleDetailPresenter(this, this);
    }

    @Override
    public void setCommentList(List<CommandEntity.DataBean> data) {

    }

    @Override
    public void setMediaData(List<HomeVideoEntity.DataBean> data) {

    }

    @Override
    public void setArticleDetail(String content) {
        if (!TextUtils.isEmpty(content)) {
            tvArticleContent.setText(Html.fromHtml(content));
        }
    }

    @OnClick({R.id.btn_back,R.id.btn_add_read})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_add_read:
                Bundle bundle = new Bundle();
                bundle.putString("articleId", articleId);
                bundle.putString("content",tvArticleContent.getText().toString());
                toActivity(ReadArticleActivity.class,bundle);
                break;
        }
    }

}
