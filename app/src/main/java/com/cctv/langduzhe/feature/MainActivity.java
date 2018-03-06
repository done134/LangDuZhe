package com.cctv.langduzhe.feature;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatRadioButton;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.base.BaseActivity;
import com.cctv.langduzhe.base.BaseFragment;
import com.cctv.langduzhe.contract.MainPresenter;
import com.cctv.langduzhe.contract.MainView;
import com.cctv.langduzhe.eventMsg.QuitEvent;
import com.cctv.langduzhe.feature.home.HomeFragment;
import com.cctv.langduzhe.feature.message.MessageFragment;
import com.cctv.langduzhe.feature.mine.MineFragment;
import com.cctv.langduzhe.feature.read.ReadActivity;
import com.cctv.langduzhe.feature.readPavilion.ReadPavilionFragment;
import com.cctv.langduzhe.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gentleyin on 2018/1/14.
 * 主页面
 */

public class MainActivity extends BaseActivity implements MainView{

    //    @BindView(R.id.tv_main_title)
//    TextView tvMainTitle;
    @BindView(R.id.frame_main_content)
    FrameLayout frameMainContent;
    @BindView(R.id.rb_home)
    AppCompatRadioButton rbHome;
    @BindView(R.id.rb_read_pavilion)
    AppCompatRadioButton rbReadPavilion;
    @BindView(R.id.btn_read)
    TextView rbRead;
    @BindView(R.id.rb_message)
    AppCompatRadioButton rbMessage;
    @BindView(R.id.rb_mine)
    AppCompatRadioButton rbMine;
    @BindView(R.id.rg_main_bottom)
    RadioGroup rgMainBottom;


    private MainPresenter mainPresenter;

    private final String FRAG_HOME = "fragmentHome";
    private final String FRAG_READ_PAVILION = "fragmentReadPavilion";
    private final String FRAG_MESSAGE = "fragmentMessage";
    private final String FRAG_MINE = "fragmentMine";
    private FragmentManager mFragmentManager;
    private String[] fragTags;


    private boolean isExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        boolean newLogin = getIntent().getBooleanExtra("new_login", false);
        if (!newLogin) {
            mainPresenter.subscribe();
        }
        initView();
        setListener();
        switchTabSelect(0);

        EventBus.getDefault().register(this);

    }

    /**
     * 初始化界面
     */
    private void initView() {
        mFragmentManager = getSupportFragmentManager();
        fragTags = new String[]{FRAG_HOME, FRAG_READ_PAVILION, FRAG_MESSAGE, FRAG_MINE};
    }

    /**
     * 控件设置监听
     */
    private void setListener() {
        rgMainBottom.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_home:
                    //首页
                    switchTabSelect(0);
                    break;
                case R.id.rb_read_pavilion:
                    //朗读亭
                    switchTabSelect(1);
                    break;
                case R.id.rb_message:
                    //消息
                    switchTabSelect(2);
                    break;
                case R.id.rb_mine:
                    //我的
                    switchTabSelect(3);
                    break;
            }
        });
        //弹出录制选择页面
        rbRead.setOnClickListener(v -> toActivity(ReadActivity.class));
    }


    public void switchTabSelect(int position) {
        //显示fragment
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideFragments(transaction);
//            tvMainTitle.setText(tabsText[position]);
        showFragment(fragTags[position], transaction);

    }


    private BaseFragment showFragment(String fragTAG, FragmentTransaction transaction) {
        BaseFragment fragment = (BaseFragment) mFragmentManager.findFragmentByTag(fragTAG);
        if (fragment == null) {
            if (FRAG_HOME.equals(fragTAG)) {
                fragment = new HomeFragment();
            } else if (FRAG_READ_PAVILION.equals(fragTAG)) {
                fragment = new ReadPavilionFragment();
            } else if (FRAG_MESSAGE.equals(fragTAG)) {
                fragment = new MessageFragment();
            } else if (FRAG_MINE.equals(fragTAG)) {
                fragment = new MineFragment();
            }

            transaction.add(R.id.frame_main_content, fragment, fragTAG);
        }
        transaction.show(fragment);
        transaction.commitAllowingStateLoss();
        return fragment;
    }

    private void hideFragments(FragmentTransaction transaction) {
        for (String fragTag : fragTags) {
            BaseFragment fragment = (BaseFragment) mFragmentManager.findFragmentByTag(fragTag);
            if (null != fragment) {
                transaction.hide(fragment);
            }
        }
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            ToastUtils.showLong(this, "再按一次后退键退出程序");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);  // 利用handler延迟发送更改状态信息
        } else {
            this.finish();
        }

    }

    @Subscribe
    public void onEvent(QuitEvent qiutEvent) {
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void setPresenter() {
        mainPresenter = new MainPresenter(this,this);
    }
}
