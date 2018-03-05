package com.cctv.langduzhe.contract.home;

import com.cctv.langduzhe.base.BaseView;
import com.cctv.langduzhe.data.entites.CommandEntity;
import com.cctv.langduzhe.data.entites.HomeVideoEntity;

import java.util.List;

/**
 * Created by gentleyin
 * on 2018/2/15.
 * 说明：首页详情
 */
public interface HomeDetailView extends BaseView {
    void setMediaData(List<HomeVideoEntity.DataBean> result);

    void setCommentList(List<CommandEntity.DataBean> data);

    void setCollectResult();

    void setDataError();
}
