package com.seuic.zhbj.base.impl.menu;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.seuic.zhbj.R;
import com.seuic.zhbj.base.BaseMenuDetailPager;
import com.seuic.zhbj.domain.NewsMenu;
import com.seuic.zhbj.domain.NewsTabBean;
import com.seuic.zhbj.global.GlobalConstant;
import com.seuic.zhbj.utils.CacheUtils;
import com.seuic.zhbj.utils.ToastUtils;

import java.util.ArrayList;

/**
 * Created by bgl on 2017/5/12.
 * <p>
 * 为了方便起见，继承BaseMenuDetailPager，继承其方法，两者没有关联
 * <p>
 * 页签界面对象
 * 在 菜单详情页  -- 新闻 NewsMenuDetailPager 中初始化
 */

public class TabDetailPager extends BaseMenuDetailPager {

    @ViewInject(R.id.vp_top_news) ViewPager mViewPager;

    private NewsMenu.NewsTabData mTabData; // 单个页签的网络数据
    private final String mUrl;
    private ArrayList<NewsTabBean.TopNews> mTopNews;

    public TabDetailPager(Activity activity, NewsMenu.NewsTabData newsTabData) {
        super(activity);
        mTabData = newsTabData;
        mUrl = GlobalConstant.SERVER_URL + mTabData.url;
    }

    @Override
    public View initView() {
      /*  textView = new TextView(mActivity);
//        textView.setText("页签");
//        textView.setText(mTabData.title); // 在这里会有空指针
        textView.setTextColor(Color.RED);
        textView.setTextSize(22);
        textView.setGravity(Gravity.CENTER);*/
        View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
//        textView.setText(mTabData.title);
        String cache = CacheUtils.getCache(mUrl, mActivity);
        if (!TextUtils.isEmpty(cache))
            processedData(cache);

        getDataFromServer();
    }

    public void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processedData(result);
                CacheUtils.setCache(mUrl,result,mActivity);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                ToastUtils.showToast(mActivity, s);
            }
        });
    }

    private void processedData(String result) {
        Gson gson = new Gson();
        NewsTabBean newsTabBean = gson.fromJson(result, NewsTabBean.class);
        mTopNews = newsTabBean.data.topnews;
        if (mTopNews != null)
            mViewPager.setAdapter(new TopNewsAdapter());
    }


    // 头条新闻数据适配
    class TopNewsAdapter extends PagerAdapter {

        private final BitmapUtils mBitmapUtils;

        public TopNewsAdapter() {
            mBitmapUtils = new BitmapUtils(mActivity);
        }

        @Override
        public int getCount() {
            return mTopNews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = new ImageView(mActivity);
            view.setImageResource(R.mipmap.topnews_item_default);
            // 设置图片缩放方式，宽高填充父控件
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            // 下载图片-将图片设置给imageview，避免内存溢出，加缓存
            // BitmapUtils
            String imageUrl = mTopNews.get(position).topimage;
            mBitmapUtils.display(view,imageUrl);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
