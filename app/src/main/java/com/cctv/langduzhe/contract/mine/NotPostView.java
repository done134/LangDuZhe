package com.cctv.langduzhe.contract.mine;

import com.cctv.langduzhe.base.BaseView;
import com.cctv.langduzhe.data.entites.NotPostEntity;

import java.util.List;

/**
 * Created by gentleyin
 * on 2018/2/5.
 * 说明：
 */
public interface NotPostView extends BaseView {

    void displayCities(List<NotPostEntity> list);
}
