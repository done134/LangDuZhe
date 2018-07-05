package com.cctv.langduzhe.feature;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cctv.langduzhe.R;
import com.cctv.langduzhe.SimpleObserve;
import com.cctv.langduzhe.base.BaseActivity;
import com.cctv.langduzhe.base.BaseFragment;
import com.cctv.langduzhe.contract.MainPresenter;
import com.cctv.langduzhe.contract.MainView;
import com.cctv.langduzhe.eventMsg.QuitEvent;
import com.cctv.langduzhe.feature.articles.ThemesFragment;
import com.cctv.langduzhe.feature.home.HomeFragment;
import com.cctv.langduzhe.feature.mine.MineFragment;
import com.cctv.langduzhe.feature.readPavilion.ReadPavilionListFragment;
import com.cctv.langduzhe.util.AnimationUtil;
import com.cctv.langduzhe.util.ToastUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gentleyin on 2018/1/14.
 * 主页面
 */

public class MainActivity extends BaseActivity implements MainView {

    @BindView(R.id.tv_title)
    TextView tvMainTitle;
    @BindView(R.id.main_title)
    RelativeLayout rlTitle;
    @BindView(R.id.frame_main_content)
    FrameLayout frameMainContent;
    @BindView(R.id.main_layout)
    RelativeLayout mainLayout;
    @BindView(R.id.fl_gaosi_bg)
    FrameLayout flGaosiBg;
    @BindView(R.id.ll_menu)
    LinearLayout llMenu;
    @BindView(R.id.btn_add_read)
    ImageButton btnAddRead;
    @BindView(R.id.btn_search)
    ImageButton btnSearch;
    @BindView(R.id.btn_home)
    TextView btnHome;
    @BindView(R.id.btn_read_pavilion)
    TextView btnReadPavilion;
    @BindView(R.id.btn_theme)
    TextView btnTheme;
    @BindView(R.id.btn_mine)
    TextView btnMine;
    @BindView(R.id.btn_cancel)
    ImageView btnCancel;

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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        boolean newLogin = getIntent().getBooleanExtra("new_login", false);
        if (!newLogin) {
            mainPresenter.subscribe();
        }
        initView();
        switchTabSelect(0);

        EventBus.getDefault().register(this);
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new SimpleObserve() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            mainPresenter.checkUpdateApk(MainActivity.this);
                        } else {
                            ToastUtils.showShort(MainActivity.this, "需要相应的存储权限");
                            finish();
                        }
                    }
                });
    }


    /**
     * 初始化界面
     */
    private void initView() {
        mFragmentManager = getSupportFragmentManager();
        fragTags = new String[]{FRAG_HOME, FRAG_READ_PAVILION, FRAG_MESSAGE, FRAG_MINE};

    }

    @OnClick({R.id.btn_menu, R.id.btn_home, R.id.btn_read_pavilion, R.id.btn_theme, R.id.btn_mine, R.id.btn_cancel, R.id.ll_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_menu:
                showMenu();
                break;
            case R.id.btn_home:
                switchTabSelect(0);
                break;
            case R.id.btn_read_pavilion:
                switchTabSelect(1);
                break;
            case R.id.btn_theme:
                switchTabSelect(2);
                break;
            case R.id.btn_mine:
                if (hasLogin()) {
                    switchTabSelect(3);
                }
                break;
            case R.id.btn_cancel:
                dismissMenu();
                break;
            case R.id.ll_menu:
                //Do Nothing
                break;
        }
    }


    /**
     * @author 尹振东
     * create at 2018/4/14 上午2:50
     * 方法说明：展示页面选项
     */
    public void showMenu() {
        Bitmap bitmap = getCacheBitmapFromView(mainLayout);
        if (bitmap != null) {
            int width = (int) Math.round(bitmap.getWidth() * 0.5);
            int height = (int) Math.round(bitmap.getHeight() * 0.5);

            Bitmap inputBmp = Bitmap.createScaledBitmap(bitmap, width, height, false);
            flGaosiBg.setBackground(new BitmapDrawable(blur(inputBmp)));
            flGaosiBg.setVisibility(View.VISIBLE);
            flGaosiBg.setAnimation(AnimationUtil.moveFromLeft(500, 0));
        }
        llMenu.setVisibility(View.VISIBLE);
        llMenu.setAnimation(AnimationUtil.moveFromLeft(500, 0));
        btnHome.setAnimation(AnimationUtil.moveFromLeft(400, 200));
        btnReadPavilion.setAnimation(AnimationUtil.moveFromLeft(400, 250));
        btnTheme.setAnimation(AnimationUtil.moveFromLeft(400, 300));
        btnMine.setAnimation(AnimationUtil.moveFromLeft(400, 350));
        btnCancel.setAnimation(AnimationUtil.moveFromLeft(400, 450));

    }

    /**
     * @author 尹振东
     * create at 2018/4/14 上午2:51
     * 方法说明：隐藏页面选项
     */
    private void dismissMenu() {
        llMenu.setVisibility(View.GONE);
        llMenu.setAnimation(AnimationUtil.moveToViewLeft(700,0));
        flGaosiBg.setVisibility(View.GONE);
        flGaosiBg.setAnimation(AnimationUtil.moveToViewLeft(700,0));

        btnHome.setAnimation(AnimationUtil.moveToViewLeft(500, 0));
        btnReadPavilion.setAnimation(AnimationUtil.moveToViewLeft(500, 100));
        btnTheme.setAnimation(AnimationUtil.moveToViewLeft(500, 150));
        btnMine.setAnimation(AnimationUtil.moveToViewLeft(500, 200));
        btnCancel.setAnimation(AnimationUtil.moveToViewLeft(450, 250));
    }

    public void switchTabSelect(int position) {
        //显示fragment
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideFragments(transaction);
        showFragment(fragTags[position], transaction);
        switch (position) {
            case 0:
                tvMainTitle.setText("首页");
                rlTitle.setVisibility(View.VISIBLE);
                btnAddRead.setVisibility(View.GONE);
                btnSearch.setVisibility(View.GONE);
                break;
            case 1:
                tvMainTitle.setText("朗读亭");
                rlTitle.setVisibility(View.VISIBLE);
                btnAddRead.setVisibility(View.VISIBLE);
                btnSearch.setVisibility(View.VISIBLE);
                break;
            case 2:
                tvMainTitle.setText("文字");
                rlTitle.setVisibility(View.VISIBLE);
                btnAddRead.setVisibility(View.GONE);
                btnSearch.setVisibility(View.GONE);
                break;
            case 3:
                rlTitle.setVisibility(View.GONE);
                break;
        }
        dismissMenu();
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
        if (llMenu.getVisibility() == View.VISIBLE) {
            dismissMenu();
        } else {
            exit();
        }
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
        switchTabSelect(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void setPresenter() {
        mainPresenter = new MainPresenter(this, this);
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


    private Bitmap blur(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap); // 创建输出图片
        RenderScript rs = RenderScript.create(this); // 构建一个RenderScript对象
        ScriptIntrinsicBlur gaussianBlue = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs)); // 创建高斯模糊脚本
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap); // 创建用于输入的脚本类型
        Allocation allOut = Allocation.createFromBitmap(rs, output); // 创建用于输出的脚本类型
        gaussianBlue.setRadius(25); // 设置模糊半径，范围0f<radius<=25f
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
