package com.seuic.zhbj.domain;

import java.util.ArrayList;

/**
 * Created by bgl on 2017/4/25.
 * 分类信息的封装
 * 使用gson解析json时，对象书写技巧，缝{}创建对象，缝[]创建结合 ArrayList，
 * 所有字段名称要和json高度一致；
 */

public class NewsMenu {

    public int retcode;
    public ArrayList<Integer> extend;

    public ArrayList<NewsMenuData> data;

    @Override
    public String toString() {
        return "NewsMenu{" +
                "retcode=" + retcode +
                ", extend=" + extend +
                ", data=" + data +
                '}';
    }

    /**
     * 侧边栏菜单对象
     */
    public class NewsMenuData{
        public int id;
        public String title;
        public String type;
        public ArrayList<NewsTabData> children;

        @Override
        public String toString() {
            return "NewsMenuData{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", type='" + type + '\'' +
                    ", children=" + children +
                    '}';
        }
    }

    /**
     * 页签对象
     */
    public class NewsTabData{
        public int id;
        public String title;
        public String type;
        public String url;

        @Override
        public String toString() {
            return "NewsTabData{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", type='" + type + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }
}
