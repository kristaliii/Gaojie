package com.example.demo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelperDao {
	private static MyOpenHelper helper ;
	public MyOpenHelperDao(Context context){
		helper = new MyOpenHelper(context, "config.db", null, 1);
	}
	/**
	 * 使用sqlitedatabase里面的insert方法插入数据
	 * @param values
	 * @return
	 */
	public static long insert(ContentValues values){
		SQLiteDatabase db = helper.getWritableDatabase();
		long id = db.insert("notetab", null, values);
		db.close();
		return id;
	}

	
	
	private class MyOpenHelper extends SQLiteOpenHelper{

		public MyOpenHelper(Context context, String name,CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			String sql = "create table if not exists notetab(id integer primary key autoincrement, name varchar(64), address varchar(64) ) ";
			db.execSQL(sql);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			String sql = "drop table if exists notetab";
			db.execSQL(sql);
		}
		
	}
}
