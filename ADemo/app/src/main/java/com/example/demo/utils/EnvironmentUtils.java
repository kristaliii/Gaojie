package com.example.demo.utils;

import java.io.File;
import java.io.IOException;

import android.os.Environment;
import android.util.Log;

public class EnvironmentUtils {
	/**
	 * 使用Environment在 /mnt/sdcard/ 下创建文件
	 */
	public static void create360Apk() {
		//12-05 16:21:54.825: I/tag(15837): /storage/sdcard0/360安全卫士.apk
		String path = Environment.getExternalStorageDirectory().getPath()+ "/360安全卫士.apk";

		File f = new File(path);
		boolean createNewFile = false;
		try {
			createNewFile = f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.i("tag", "" + createNewFile);
		Log.i("tag", path);
	}
}
