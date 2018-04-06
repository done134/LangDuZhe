package com.cctv.langduzhe.feature;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.base.BaseActivity;
import com.cctv.langduzhe.base.BaseFragment;
import com.cctv.langduzhe.contract.MainPresenter;
import com.cctv.langduzhe.contract.MainView;
import com.cctv.langduzhe.eventMsg.QuitEvent;
import com.cctv.langduzhe.feature.articles.ThemesFragment;
import com.cctv.langduzhe.feature.home.HomeFragment;
import com.cctv.langduzhe.feature.mine.MineFragment;
import com.cctv.langduzhe.feature.readPavilion.ReadPavilionFragment;
import com.cctv.langduzhe.feature.readPavilion.ReadPavilionListFragment;
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
//    @BindView(R.id.btn_read)
//    TextView rbRead;
    @BindView(R.id.rb_message)
    AppCompatRadioButton rbMessage;
    @BindView(R.id.rb_mine)
    AppCompatRadioButton rbMine;
    @BindView(R.id.rg_main_bottom)
    RadioGroup rgMainBottom;
    @BindView(R.id.main_layout)
    RelativeLayout mainLayout;
    @BindView(R.id.fl_gaosi_bg)
    FrameLayout flGaosiBg;


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
        /*rbRead.setOnClickListener(v -> {
            Bitmap bitmap = getCacheBitmapFromView(mainLayout);
            if (bitmap != null) {
                int width = (int) Math.round(bitmap.getWidth() * 0.5);
                int height = (int) Math.round(bitmap.getHeight() * 0.5);

                Bitmap inputBmp = Bitmap.createScaledBitmap(bitmap, width, height, false);
                flGaosiBg.setBackground(new BitmapDrawable(blur(inputBmp, 20)));
                flGaosiBg.setVisibility(View.VISIBLE);
            }
                toActivity(ReadActivity.class);
//            }
        });*/
    }


    public void switchTabSelect(int position) {
        //显示fragment
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideFragments(transaction);
//            tvMainTitle.setText(tabsText[position]);
        showFragment(fragTags[position], transaction);

    }


    @Override
    protected void onResume() {
        super.onResume();
        flGaosiBg.setVisibility(View.GONE);
    }

    private BaseFragment showFragment(String fragTAG, FragmentTransaction transaction) {
        BaseFragment fragment = (BaseFragment) mFragmentManager.findFragmentByTag(fragTAG);
        if (fragment == null) {
            if (FRAG_HOME.equals(fragTAG)) {
                fragment = new HomeFragment();
            } else if (FRAG_READ_PAVILION.equals(fragTAG)) {
                fragment = new ReadPavilionListFragment();
            } else if (FRAG_MESSAGE.equals(fragTAG)) {
                fragment = new ThemesFragment();
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
            new Handler().postDelayed(() -> isExit = false, 2000);  // 利用handler延迟发送更改状态信息
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

    /**
     * 获取一个 View 的缓存视图
     *
     * @param view
     * @return
     */
    private Bitmap getCacheBitmapFromView(View view) {
        final boolean drawingCacheEnabled = true;
        view.setDrawingCacheEnabled(drawingCacheEnabled);
        view.buildDrawingCache(drawingCacheEnabled);
        final Bitmap drawingCache = view.getDrawingCache();
        Bitmap bitmap;
        if (drawingCache != null) {
            bitmap = Bitmap.createBitmap(drawingCache);
            view.setDrawingCacheEnabled(false);
        } else {
            bitmap = null;
        }
        return bitmap;
    }



    private Bitmap blur(Bitmap bitmap,float radius) {
        Bitmap output = Bitmap.createBitmap(bitmap); // 创建输出图片
        RenderScript rs = RenderScript.create(this); // 构建一个RenderScript对象
        ScriptIntrinsicBlur gaussianBlue = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs)); // 创建高斯模糊脚本
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap); // 创建用于输入的脚本类型
        Allocation allOut = Allocation.createFromBitmap(rs, output); // 创建用于输出的脚本类型
        gaussianBlue.setRadius(radius); // 设置模糊半径，范围0f<radius<=25f
        gaussianBlue.setInput(allIn); // 设置输入脚本类型
        gaussianBlue.forEach(allOut); // 执行高斯模糊算法，并将结果填入输出脚本类型中
        allOut.copyTo(output); // 将输出内存编码为Bitmap，图片大小必须注意
        rs.destroy();
        if (Build.VERSION.SDK_INT >= 23) {
            RenderScript.releaseAllContexts();
        } // 关闭RenderScript对象，API>=23则使用rs.releaseAllContexts()
        return output;
    }
}
