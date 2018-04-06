package com.cctv.langduzhe.contract.articles;

import com.cctv.langduzhe.base.BaseView;
import com.cctv.langduzhe.data.entites.CommandEntity;
import com.cctv.langduzhe.data.entites.HomeVideoEntity;

import java.util.List;

/**
 * Created by gentleyin
 * on 2018/3/19.
 * 说明：
 */
public interface ArticleDetailView extends BaseView {
    void setCommentList(List<CommandEntity.DataBean> data);

    void setMediaData(List<HomeVideoEntity.DataBean> data);

    void setArticleDetail(String content);
}
