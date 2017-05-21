package com.seuic.zhbj;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.seuic.zhbj.fragment.ContentFragment;
import com.seuic.zhbj.fragment.LeftMenuFragment;

/**
 * Created by someone on 2017/4/23.
 */

public class MainActivity extends SlidingFragmentActivity {
    private final static String TAG_LEFT_MENU = "TAG_LEFT_MENU";
    private final static String TAG_CONTENT = "TAG_CONTENT";
    @Override
    public void onCreate( Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // 一个空的fragment

        setBehindContentView(R.layout.left_menu);// 一个空的fragment
        SlidingMenu slidingMenu = getSlidingMenu();

        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN); // 全屏触摸
        slidingMenu.setBehindOffset(300); // 屏幕预留200像宽度

        initFragment();

    }
    /**
     * 初始化两个fragment
     */
    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fl_main_content, new ContentFragment(), TAG_CONTENT);
        transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(), TAG_LEFT_MENU);
        transaction.commit();
//        Fragment fragmentByTag = fm.findFragmentByTag(TAG_CONTENT); // 根据标记找到对应fragment
    }

    /**
     * 获取侧边栏对象
     * @return
     */
    public LeftMenuFragment getLeftMenuFragment() {
        FragmentManager fm = getSupportFragmentManager();
        LeftMenuFragment fragment = (LeftMenuFragment) fm.findFragmentByTag(TAG_LEFT_MENU);
        return fragment;
    }

    /**
     * 获取ContentFragment主页对象
     * @return
     */
    public ContentFragment getContentFragment() {
        FragmentManager fm = getSupportFragmentManager();
        ContentFragment fragment = (ContentFragment) fm.findFragmentByTag(TAG_CONTENT);
        return fragment;
    }
}
