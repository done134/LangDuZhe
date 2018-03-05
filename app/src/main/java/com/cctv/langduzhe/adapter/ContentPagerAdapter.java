package com.cctv.langduzhe.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.cctv.langduzhe.base.BaseFragment;

import java.util.List;

public class ContentPagerAdapter extends FragmentStatePagerAdapter {
    private List<BaseFragment> fragmentList;

    public ContentPagerAdapter(FragmentManager fm, List<BaseFragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}