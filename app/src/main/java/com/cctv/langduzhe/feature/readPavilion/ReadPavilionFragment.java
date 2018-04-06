package com.cctv.langduzhe.feature.readPavilion;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.base.BaseFragment;
import com.cctv.langduzhe.adapter.ContentPagerAdapter;
import com.cctv.langduzhe.contract.readPavilion.ReadPavilionPresenter;
import com.cctv.langduzhe.contract.readPavilion.ReadPavilionView;
import com.cctv.langduzhe.data.entites.HomeVideoEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by gentleyin
 * on 2018/1/15.
 * 说明：朗读亭fragment
 */
public class ReadPavilionFragment extends BaseFragment{

    @BindView(R.id.rg_read_pavilion_type)
    RadioGroup rgReadPavilionType;
    @BindView(R.id.rb_read_pavilion_newest)
    RadioButton rbReadPavilionNewest;
    @BindView(R.id.rb_read_pavilion_hottest)
    RadioButton rbReadPavilionHottest;
    @BindView(R.id.vp_read_pavilion)
    ViewPager vpReadPavilion;
    Unbinder unbinder;
    private View mView;

    /**
     * Fragment列表
     */
    private List<BaseFragment> fragmentList;

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ReadPavilionListFragment newestFragment = new ReadPavilionListFragment();
        ReadPavilionListFragment hottestFragment = new ReadPavilionListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("data_type",1);
        hottestFragment.setArguments(bundle);
        fragmentList = new ArrayList<>();
        fragmentList.add(newestFragment);
        fragmentList.add(hottestFragment);
        vpReadPavilion.setAdapter(new ContentPagerAdapter(getFragmentManager(),fragmentList));
        vpReadPavilion.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    rbReadPavilionNewest.setChecked(true);
                }else {
                    rbReadPavilionHottest.setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        rgReadPavilionType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_read_pavilion_hottest) {
                vpReadPavilion.setCurrentItem(1,true);
            }else {
                vpReadPavilion.setCurrentItem(0,true);
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
    }

    @Override
    public void setPresenter() {
    }

}
