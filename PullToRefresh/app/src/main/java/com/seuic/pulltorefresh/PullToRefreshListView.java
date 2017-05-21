package com.seuic.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by Dell on 2017/4/9.
 */

public class PullToRefreshListView extends ListView implements AbsListView.OnScrollListener{
    private final static String TAG = "PullToRefreshListView";

    private View mViewFooter;
    private int measuredHeight;
    private OnRefreshListener mRefreshListener;

    public PullToRefreshListView(Context context) {
        super(context);
        init();
    }


    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initViewFooter();
        setOnScrollListener(this); // 凡是activity实现的监听，都有这个订阅关系
    }

    private void initViewFooter() {
        mViewFooter = View.inflate(getContext(), R.layout.footer, null);
        Log.d(TAG, "脚布局高度：" + mViewFooter.getHeight() + "");

        mViewFooter.measure(0, 0); // 按照测量规则提前手动测量宽高
        measuredHeight = mViewFooter.getMeasuredHeight(); // 获取到脚布局高度
        Log.v(TAG, "脚布局真的高度：" + measuredHeight);
        mViewFooter.setPadding(0, -measuredHeight, 0, 0); // 设置隐藏脚布局

        addFooterView(mViewFooter);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return super.onTouchEvent(ev);
    }

    public void refreshCompleted() {
        mViewFooter.setPadding(0,-measuredHeight,0,0);
    }

    /**
     * 面向接口编程  1 内部定义接口；
     */
    public interface OnRefreshListener{
        void onLoadMore();// 加载更多
    }
    /**
     * 面向接口编程  2 对外提供调用接口内方法的方法；
     */
    public void setOnRefreshListener(OnRefreshListener mRefreshListener) {
        this.mRefreshListener = mRefreshListener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
//        Log.e(TAG, "" + scrollState);
        if (scrollState == SCROLL_STATE_TOUCH_SCROLL) { // 1 滑动

        }
        if (scrollState == SCROLL_STATE_FLING) { // 2 惯性下滑动

        }
        Log.e(TAG, ""+getLastVisiblePosition());
        if (scrollState == SCROLL_STATE_IDLE  && getLastVisiblePosition() >=(getCount()-1)) { // 0 滑动停止
            // 开始加载更多
            mViewFooter.setPadding(0, 0, 0, 0);
            setSelection(getCount());// 跳转到最后一条，使其显示加载更多
            if(mRefreshListener!=null)
                mRefreshListener.onLoadMore();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

}
