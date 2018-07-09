package com.cctv.langduzhe.feature.articles;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.adapter.ContentPagerAdapter;
import com.cctv.langduzhe.base.BaseFragment;
import com.cctv.langduzhe.contract.MediaPackagePresenter;
import com.cctv.langduzhe.contract.MediaPackageView;
import com.cctv.langduzhe.data.entites.MediaPackageEntity;
import com.cjt2325.cameralibrary.util.ScreenUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by gentleyin
 * on 2018/7/9.
 * 说明：文字页面外层Fragment
 */
public class ArticleFrame extends BaseFragment implements MediaPackageView {

    @BindView(R.id.tablayout_home_season)
    TabLayout tblayoutHomeSeason;
    @BindView(R.id.vp_home_video)
    ViewPager vpHomeVideo;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.pre_progress)
    ProgressBar preProgress;
    @BindView(R.id.tv_no_data)
    TextView tvNoData;
    Unbinder unbinder;
    private View mView;

    private MediaPackagePresenter homePresenter;
    private List<MediaPackageEntity.DataBean> seasonList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //注意View对象的重复使用，以便节省资源
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_read_pavilion, container, false);
        }
        unbinder = ButterKnife.bind(this, mView);
        return mView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void requestData() {
        homePresenter.getPackageList("s");
    }

    @Override
    public void setPresenter() {
        homePresenter = new MediaPackagePresenter(getActivity(), this);
    }

    @Override
    public void setPackageInfo(List<MediaPackageEntity.DataBean> data) {
        if (data != null && data.size() > 0) {
            seasonList = data;
            showData(true);
            initFrame();

        } else {
            showData(false);
        }
    }

    private void showData(boolean hasData) {
        if (preProgress != null) {
            preProgress.setVisibility(hasData ? View.GONE : View.VISIBLE);
        }
        if (tvNoData != null) {
            tvNoData.setVisibility(hasData ? View.GONE : View.VISIBLE);
            tvNoData.setOnClickListener(v -> {
                requestData();
                preProgress.setVisibility(View.VISIBLE);
            });
        }
        if (llContent != null) {
            llContent.setVisibility(hasData ? View.VISIBLE : View.GONE);
        }

    }


    /**
     * @author 尹振东
     * create at 2018/7/9 上午9:36
     * 方法说明：根据请求到的季列表生成相应tab
     */
    private void initFrame() {

        tblayoutHomeSeason.setBackgroundColor(getActivity().getResources().getColor(R.color.c_191919));
        for (int i = 0; i < seasonList.size(); i++) {
            RadioButton customView = getTabItem(seasonList.get(i).getName());
            if (i == 0) {
                customView.setChecked(true);
            } else {
                customView.setChecked(false);
            }
            tblayoutHomeSeason.addTab(tblayoutHomeSeason.newTab().setCustomView(customView));

        }
        vpHomeVideo.setAdapter(new ContentPagerAdapter(getChildFragmentManager(), seasonList, 1));
        vpHomeVideo.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tblayoutHomeSeason));
        tblayoutHomeSeason.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpHomeVideo.setCurrentItem(tab.getPosition());
                ((RadioButton) tab.getCustomView()).setChecked(true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((RadioButton) tab.getCustomView()).setChecked(false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        setTabDividerLines();
    }

    /**
     * @author 尹振东
     * create at 2018/7/9 上午10:27
     * 方法说明：设置分割线
     */
    private void setTabDividerLines() {
        LinearLayout linearLayout = (LinearLayout) tblayoutHomeSeason.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerDrawable(ContextCompat.getDrawable(getActivity(),
                R.drawable.shape_middle_divider));
        linearLayout.setDividerPadding(ScreenUtils.dip2px(getActivity(), 10));
    }

    private RadioButton getTabItem(String title) {
        RadioButton tabView = (RadioButton) LayoutInflater.from(getActivity()).inflate(R.layout.item_article_tab, null, false);
        tabView.setText(title);
        return tabView;
    }
}