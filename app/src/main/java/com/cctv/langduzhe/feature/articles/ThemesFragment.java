package com.cctv.langduzhe.feature.articles;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.adapter.ThemesAdapter;
import com.cctv.langduzhe.base.BaseFragment;
import com.cctv.langduzhe.base.BaseRecyclerViewAdapter;
import com.cctv.langduzhe.contract.articles.ThemesPresenter;
import com.cctv.langduzhe.contract.articles.ThemesView;
import com.cctv.langduzhe.data.entites.ThemeEntity;
import com.cctv.langduzhe.eventMsg.CollectEvent;
import com.cctv.langduzhe.util.ToastUtils;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by gentleyin
 * on 2018/3/19.
 * 说明：文字列表Fragment
 */
public class ThemesFragment extends BaseFragment implements PullLoadMoreRecyclerView.PullLoadMoreListener, BaseRecyclerViewAdapter.OnItemClickListener, ThemesView {
    @BindView(R.id.rv_articles_list)
    PullLoadMoreRecyclerView rvArticleList;
    Unbinder unbinder;

    private int pageNum;
    private View mView;
    private ThemesPresenter presenter;

    private ThemesAdapter articlesAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //注意View对象的重复使用，以便节省资源
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_articles, container, false);
        }

        unbinder = ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvArticleList.setGridLayout(2);
        rvArticleList.setOnPullLoadMoreListener(this);
        articlesAdapter = new ThemesAdapter(getActivity());
        articlesAdapter.setOnItemClickListener(this);
        rvArticleList.setAdapter(articlesAdapter);
        rvArticleList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.left = 20;
                outRect.right = 20;
                outRect.bottom = 20;

                // Add top margin only for the first item to avoid double space between items
                if (parent.getChildPosition(view) == 0)
                    outRect.top = 20;
            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void requestData() {
        presenter.subscribe();
    }

    @Override
    public void onRefresh() {
        pageNum = 0;
        presenter.getArticles(pageNum);
    }

    @Override
    public void onLoadMore() {
        pageNum = pageNum + 1;
        presenter.getArticles(pageNum);
    }

    @Override
    public void onItemClick(int optType, int position, boolean yesOrNo) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("theme", articlesAdapter.getList().get(position));
        toActivity(ArticleListActivity.class, bundle);

    }

    @Override
    public void setPresenter() {
        presenter = new ThemesPresenter(getActivity(), this);
    }

    @Override
    public void setThemeList(List<ThemeEntity.DataBean> list) {
        if (list != null && list.size() > 0) {
            if (pageNum == 0) {
                articlesAdapter.setList(list);
            } else {
                articlesAdapter.addList(list);
            }
        } else {
            ToastUtils.showLong(getActivity(), "数据已全部加载");
        }
        rvArticleList.setPullLoadMoreCompleted();
    }


}
