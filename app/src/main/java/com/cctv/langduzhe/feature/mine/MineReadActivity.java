package com.cctv.langduzhe.feature.mine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.base.BaseActivity;
import com.cctv.langduzhe.base.BaseFragment;
import com.cctv.langduzhe.adapter.ContentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gentleyin
 * on 2018/1/21.
 * 说明：我的朗读页面
 */
public class MineReadActivity extends BaseActivity {
    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rb_have_publish)
    RadioButton rbHavePublish;
    @BindView(R.id.rb_not_publish)
    RadioButton rbNotPublish;
    @BindView(R.id.vp_my_read)
    ViewPager vpMyRead;
    @BindView(R.id.rg_my_read)
    RadioGroup rgMyRead;


    /**
     * Fragment列表
     */
    private List<BaseFragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_read);
        ButterKnife.bind(this);
        initView();
        tvTitle.setText("我的朗读");
    }

    private void initView() {
        MineReadFragment postedFragment = new MineReadFragment();
        Bundle postedBundle = new Bundle();
        postedBundle.putInt("data_type",2);
        postedFragment.setArguments(postedBundle);
        NotPostFragment notPostFragment = new NotPostFragment();
        fragmentList = new ArrayList<>();
        fragmentList.add(postedFragment);
        fragmentList.add(notPostFragment);
        vpMyRead.setAdapter(new PageAdapter(getSupportFragmentManager()));
        vpMyRead.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    rbHavePublish.setChecked(true);
                }else {
                    rbNotPublish.setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        btnBack.setOnClickListener(v -> onBackPressed());
        rgMyRead.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_not_publish) {
                vpMyRead.setCurrentItem(1,true);
            }else {
                vpMyRead.setCurrentItem(0,true);
            }
        });
    }

    @Override
    public void setPresenter() {

    }

    class PageAdapter extends FragmentPagerAdapter {

        public PageAdapter(FragmentManager fm) {
            super(fm);
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
}
