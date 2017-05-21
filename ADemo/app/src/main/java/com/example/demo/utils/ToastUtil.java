package com.example.demo.utils;


import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	private static Toast toast;
	/**
	 * 强大的吐司，可以连续弹，不会等上一个消失
	 * @param text
	 */
	public static void showToast(Context context,String text){
		// 单例
		if (toast==null) {
			toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		}
		toast.setText(text);
		toast.show();
	}
}
