package com.seuic.gaojie.utils;

import android.annotation.SuppressLint;
import android.os.Handler;
/**
 * Asynchonize
 * @author Administrator
 *
 */
@SuppressLint("HandlerLeak")
public abstract class MyAsyncTask {
	private  Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			postTask();
		}
	};
	/**
	 * 子线程之前执行
	 */
	public abstract void preTask();
	/**
	 * 子线程中执行
	 */
	public abstract void doInBack();
	/**
	 * 子线程之后执行
	 */
	public abstract void postTask();
	/**
	 * ִ执行
	 */
	public void excuted() {
		preTask();
		new Thread() {
			public void run() {
				doInBack();
				handler.sendEmptyMessage(0);
			}
		}.start();
	}
}
