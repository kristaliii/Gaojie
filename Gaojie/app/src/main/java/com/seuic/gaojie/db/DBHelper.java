package com.seuic.gaojie.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by Dell on 2017/3/16.
 */

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction(); //开启一个数据库事务
        try{
            db.execSQL(SQL.creatBarcodeTypeTab());
            db.execSQL(SQL.creatBarcodeTab());
            db.setTransactionSuccessful(); //标记事务中的sql语句全部成功执行
        }finally {
            db.endTransaction(); //判断事务的标记是否成功，如果不成功，回滚错误之前执行的sql语句
        }
        Log.i("TAG", "table create ok");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table if exists "+SQL.TAB_NAME_BARCODE_TYPE; // 此处是会报错的，因为TAB_NAME在SDcard中
        db.execSQL(sql);
        onCreate(db);
    }
}