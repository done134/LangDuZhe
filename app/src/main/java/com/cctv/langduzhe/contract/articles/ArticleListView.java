package com.cctv.langduzhe.contract.articles;

import com.cctv.langduzhe.base.BaseView;
import com.cctv.langduzhe.data.entites.ArticlesEntity;

import java.util.List;

/**
 * Created by gentleyin
 * on 2018/3/19.
 * 说明：文字列表View
 */
public interface ArticleListView extends BaseView{
    void setArticleList(List<ArticlesEntity.DataBean> data);
}
