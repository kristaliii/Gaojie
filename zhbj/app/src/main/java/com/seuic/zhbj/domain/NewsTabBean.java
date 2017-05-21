package com.seuic.zhbj.domain;

import java.util.ArrayList;

/**
 * Created by bgl on 2017/5/16.
 * 页签详情数据对象
 */

public class NewsTabBean {
    public NewsTab data;

    public  class NewsTab{
        public String more;
        public ArrayList<NewsData> news;
        public ArrayList<TopNews> topnews;
    }
    // 新闻列表对象
    public class NewsData{
        public int id;
        public String listimage;
        public String pubdate;
        public String title;
        public String type;
        public String url;
    }

    // 头条新闻
    public class TopNews{
        public int id;
        public String topimage;
        public String pubdate;
        public String title;
        public String type;
        public String url;
    }

}
