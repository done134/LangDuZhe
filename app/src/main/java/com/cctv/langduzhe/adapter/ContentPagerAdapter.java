package com.cctv.langduzhe.adapter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.cctv.langduzhe.base.BaseFragment;
import com.cctv.langduzhe.data.entites.MediaPackageEntity;
import com.cctv.langduzhe.feature.articles.ThemesFragment;
import com.cctv.langduzhe.feature.home.HomeListFragment;

import java.util.HashMap;
import java.util.List;

public class ContentPagerAdapter extends FragmentStatePagerAdapter {
    private List<MediaPackageEntity.DataBean> fragmentList;
    private HashMap<Integer, BaseFragment> mFrameMap;
    private int type;

    public ContentPagerAdapter(FragmentManager fm, List<MediaPackageEntity.DataBean> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
        type = 0;
    }
    public ContentPagerAdapter(FragmentManager fm, List<MediaPackageEntity.DataBean> fragmentList, int type) {
        super(fm);
        this.fragmentList = fragmentList;
        this.type = type;
    }



    @Override
    public Fragment getItem(int position) {
        return createFragment(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @SuppressLint("UseSparseArrays")
    private Fragment createFragment(int pos) {
        if (mFrameMap == null) {
            mFrameMap = new HashMap<>();
        }
        BaseFragment fragment = mFrameMap.get(pos);

        if (fragment == null) {
            if (type == 0) {
                fragment = new HomeListFragment();
            } else if (type == 1) {
                fragment = new ThemesFragment();
            }
            Bundle bundle = new Bundle();
            bundle.putString("season_id", fragmentList.get(pos).getId());
            bundle.putInt("sortNum", fragmentList.get(pos).getSortNum());
            fragment.setArguments(bundle);
            mFrameMap.put(pos, fragment);
        }
        return fragment;
    }
}