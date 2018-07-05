package com.cctv.langduzhe.feature.articles;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.SimpleObserve;
import com.cctv.langduzhe.adapter.VoiceAdapter;
import com.cctv.langduzhe.base.BaseActivity;
import com.cctv.langduzhe.base.BaseRecyclerViewAdapter;
import com.cctv.langduzhe.contract.LikePresenter;
import com.cctv.langduzhe.contract.LikeView;
import com.cctv.langduzhe.contract.articles.ArticleDetailPresenter;
import com.cctv.langduzhe.contract.articles.ArticleDetailView;
import com.cctv.langduzhe.data.entites.ArticlesEntity;
import com.cctv.langduzhe.data.entites.HomeVideoEntity;
import com.cctv.langduzhe.feature.read.ReadArticleActivity;
import com.cctv.langduzhe.util.CommonUtil;
import com.cctv.langduzhe.util.ToastUtils;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by gentleyin
 * on 2018/3/19.
 * 说明：文字详情页面
 */
public class ArticleDetailActivity extends BaseActivity implements ArticleDetailView ,
        BaseRecyclerViewAdapter.OnItemClickListener ,LikeView{

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_article_cover)
    ImageView ivArticleCover;
    @BindView(R.id.tv_article_content)
    TextView tvArticleContent;
    @BindView(R.id.rv_media_list)
    RecyclerView rvMediaList;
    @BindView(R.id.scroll_view)
    NestedScrollView scrollView;
    @BindView(R.id.tv_article_title)
    TextView tvArticleTitle;
    private ArticleDetailPresenter presenter;
    private LikePresenter likePresenter;
    private String articleId;
    private VoiceAdapter voiceAdapter;
    private int optPosition;

    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        ButterKnife.bind(this);
        getIntentData();
        initView();
    }

    private void initView() {
        mLayoutManager = new LinearLayoutManager(this);
        rvMediaList.setLayoutManager(mLayoutManager);
        voiceAdapter = new VoiceAdapter();
        rvMediaList.setAdapter(voiceAdapter);
        voiceAdapter.setOnItemClickListener(this);
        rvMediaList.setNestedScrollingEnabled(false);
        rvMediaList.addOnScrollListener(new RecyclerView.OnScrollListener() {

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
                if (voiceAdapter.getPlayPosition() >= 0) {
                    //当前播放的位置
                    int position = voiceAdapter.getPlayPosition();
                    //对应的播放列表TAG
                    if (position < firstVisibleItem || position > lastVisibleItem) {
                        //如果滑出去了上面和下面就是否
                        voiceAdapter.pauseVideo();
                    }
                }
            }
        });

    }

    private void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ArticlesEntity.DataBean article = (ArticlesEntity.DataBean) bundle.getSerializable("article");
            articleId = article.getId();
            tvTitle.setText(article.getTitle());
            presenter.getArticleDetail(articleId);
            presenter.getMediaList(articleId,0);
            tvArticleTitle.setText(article.getTitle());

            int screenWidth = CommonUtil.getScreenWidth(this);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ivArticleCover.getLayoutParams();
            lp.width = screenWidth;
            lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            ivArticleCover.setLayoutParams(lp);

            ivArticleCover.setMaxWidth(screenWidth);
            ivArticleCover.setMaxHeight(screenWidth * 3); //这里其实可以根据需求而定，我这里测试为最大宽度的5倍
            Picasso.with(this).load(article.getImg()).into(ivArticleCover);
        }

    }

    @Override
    public void setPresenter() {
        presenter = new ArticleDetailPresenter(this, this);
        likePresenter = new LikePresenter(this,this);
    }


    @Override
    public void setMediaData(List<HomeVideoEntity.DataBean> data) {
        voiceAdapter.setData(data);
    }

    @Override
    public void setArticleDetail(String content) {
        if (!TextUtils.isEmpty(content)) {
            tvArticleContent.setText(content);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        voiceAdapter.pauseVideo();
    }

    @OnClick({R.id.btn_back,R.id.btn_add_read})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_add_read:
                RxPermissions rxPermissions = new RxPermissions(this);
                rxPermissions.request(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO)
                        .subscribe(new SimpleObserve() {
                            @Override
                            public void onNext(Boolean aBoolean) {
                                if (aBoolean) {

                                    Bundle bundle = new Bundle();
                                    bundle.putString("articleId", articleId);
                                    bundle.putString("content",tvArticleContent.getText().toString());
                                    toActivity(ReadArticleActivity.class,bundle);
                                } else {
                                    ToastUtils.showShort(ArticleDetailActivity.this, "需要相应的权限");
                                    finish();
                                }
                            }
                        });
                break;
        }
    }

    @Override
    public void onItemClick(int optType, int position, boolean yesOrNo) {
        optPosition = position;
        if (optType == 0) {
            presenter.addWatchSum(voiceAdapter.getList().get(position).getId());
        } else if (optType == 1) {
            if (hasLogin()) {
                if (yesOrNo) {
                    likePresenter.likeRead(voiceAdapter.getList().get(position).getId());
                } else {
                    likePresenter.unlikeRead(voiceAdapter.getList().get(position).getId());
                }
            }
        }
    }

    @Override
    public void likeResult(boolean isLike) {
        int likeSum = voiceAdapter.getList().get(optPosition).getLikeSum();
        if (isLike) {
            voiceAdapter.getList().get(optPosition).setLikeSum(likeSum+1);
        } else {
            voiceAdapter.getList().get(optPosition).setLikeSum(likeSum-1);
        }
        voiceAdapter.getList().get(optPosition).setIsLike(isLike ? 1 : 0);
        voiceAdapter.notifyDataSetChanged();
    }
}
