package com.cctv.langduzhe.contract.articles;

import com.cctv.langduzhe.base.BaseView;
import com.cctv.langduzhe.data.entites.ThemeEntity;

import java.util.List;

/**
 * Created by gentleyin
 * on 2018/3/19.
 * 说明：
 */
public interface ThemesView extends BaseView {
    void setThemeList(List<ThemeEntity.DataBean> data);
}
