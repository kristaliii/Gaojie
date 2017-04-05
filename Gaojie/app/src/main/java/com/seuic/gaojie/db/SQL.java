package com.seuic.gaojie.db;

/**
 * Created by Dell on 2017/3/20.
 */

public final class SQL {

    // sd卡下的数据库目录名字
    public static String DB_PATH = "GaoJieDB";

    // 数据库名字
    public static String DB_NAME = "GaoJie.db";

    // 编辑的时候选择的条码类型  表名
    public static final String TAB_NAME_BARCODE_TYPE = "gaojietab";

    // 条码类 表名
    public static final String TAB_NAME_BARCODE = "barcode";

    public static final String creatBarcodeTypeTab() {
        String sql = "create table if not exists " + TAB_NAME_BARCODE_TYPE +"( " +
                "_id integer primary key autoincrement, " +
                "text text not null)";
        return sql;
    }

    /**
     *  String barcode;        // 条码
        String barcodeType;    // 条码类型
         String date;           // 条码扫描日期
        Drawable color;        // 新增一个背景属性
     */
    public static final String creatBarcodeTab() {
        String sqlBarcode = "create table if not exists " + TAB_NAME_BARCODE +"( " +
                "_id integer primary key autoincrement," +
                "barcode text," +
                "barcodeType text," +
                "date text)";
        return sqlBarcode;
    }
}
