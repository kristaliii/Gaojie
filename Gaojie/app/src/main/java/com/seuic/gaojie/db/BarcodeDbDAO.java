package com.seuic.gaojie.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

/**
 * 数据访问对象 借助此对象中的方法 访问SQLite数据库
 * (final 类不能够被继承，可以新建对象)
 */
public final class BarcodeDbDAO {
	private DBHelper dbHelper;

	public BarcodeDbDAO(Context context) {
		String path = Environment.getExternalStorageDirectory().getPath() + "/"+SQL.DB_PATH+"/";
		dbHelper = new DBHelper(context, path + SQL.DB_NAME, null, 1);
	}
	//插入 (增)
	public long insert(ContentValues values) {
		SQLiteDatabase sdb = dbHelper.getWritableDatabase();
		long id = sdb.insert(SQL.TAB_NAME_BARCODE, null, values);
		sdb.close();
		return id;
	}
	//查询 (查)
	public Cursor query() {
		String sql = "select * from "+ SQL.TAB_NAME_BARCODE;
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		return db.rawQuery(sql, null);
	}

	// 根据日期查询
	public Cursor queryByDate(String date) {
		String sql = "select * from " + SQL.TAB_NAME_BARCODE + " where date=?";
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		return db.rawQuery(sql, new String[]{date});
	}

	/**
	 * 根据条码查询
	 */
	public Cursor queryByBarcode(String barcode) {
		String sql = "select * from " + SQL.TAB_NAME_BARCODE + " where barcode=?";
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		return db.rawQuery(sql, new String[]{barcode});
	}

	//通过 billNumber 查询 (查)
	public Cursor queryByBillNumber(String billNumber) {
		String sql = "select * from " + SQL.TAB_NAME_BARCODE + " where _billnumber=?";
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		return db.rawQuery(sql, new String[] { billNumber });
	}
	// 根据 barcode 删除指定记录  (删)
	public void delete(String barcode) {
		SQLiteDatabase sdb = dbHelper.getWritableDatabase();
		sdb.delete(SQL.TAB_NAME_BARCODE, "_barcode=?", new String[] { barcode });
		sdb.close();
	}
	/**
	 * 根据text删除
     */
	public void deleteText(String text) {
		SQLiteDatabase sdb = dbHelper.getWritableDatabase();
		sdb.delete(SQL.TAB_NAME_BARCODE, "text=?", new String[] { text });
		sdb.close();
	}

	/**
	 * 根据条码删除
	 * @param barcode
     */
	public void deleteByBarcode(String barcode) {
		SQLiteDatabase sdb = dbHelper.getWritableDatabase();
		sdb.delete(SQL.TAB_NAME_BARCODE, "barcode=?", new String[] { barcode });
		sdb.close();
	}

	/**
	 * 根据日期删除
	 * @param date
     */
	public int deleteByDate(String date) {
		SQLiteDatabase sdb = dbHelper.getWritableDatabase();
		int delete = sdb.delete(SQL.TAB_NAME_BARCODE, "date=?", new String[]{date});
		sdb.close();
		return delete;
	}
	// 根据 billNumber 删除指定记录  (删)
	public void deleteByBillnumber(String billNumber) {
		SQLiteDatabase sdb = dbHelper.getWritableDatabase();
		sdb.delete(SQL.TAB_NAME_BARCODE, "_billnumber=?", new String[] { billNumber });
		sdb.close();
	}

	/**
	 * 删除整张表
	 */
	public void deleteTable(){
		SQLiteDatabase sdb = dbHelper.getWritableDatabase();
		sdb.delete(SQL.TAB_NAME_BARCODE, null, null);
		sdb.close();
	}
	/**
	 * 根据text更新记录  (改)
	 */
	public void update(ContentValues values, String whereClause,String[] whereArgs) {
		SQLiteDatabase sdb = dbHelper.getWritableDatabase();
		sdb.update(SQL.TAB_NAME_BARCODE, values, whereClause, whereArgs);
		sdb.close();
	}
}
