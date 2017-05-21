package com.example.demo.utils;

import java.io.File;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class FileUtils {
	/**
	 * 删除指定文件
	 * 
	 * @param fileNames
	 */
	public static void deleteFiles(String... fileNames) {
		if (fileNames.length <= 0)
			return;
		for (int i = 0; i < fileNames.length; i++) {
			File file = new File(fileNames[i]);
			if (file.exists())
				file.delete();

		}
	}

	/**
	 * 打开文件的方式
	 * @param f文件对象
	 */
	public static void openFile(File f, Context context) {
		if (!f.exists()) {
			ToastUtil.showToast(context, "文件尚未下载，无法查看，请您先下载文件！");
			return;
		}
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);

		/* 调用getMIMEType()来取得MimeType */
		String type = getMIMEType(f);
		/* 设置intent的file与MimeType */
		intent.setDataAndType(Uri.fromFile(f), type);
		context.startActivity(intent);
	}

	/* 判断文件MimeType的method */
	private static String getMIMEType(File f) {
		String type = "";
		String fName = f.getName();
		/* 取得扩展名 */
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase(Locale.CHINA);

		/* 依扩展名的类型决定MimeType */
		if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
				|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")
				|| end.equals("wma")) {
			type = "audio";
		} else if (end.equals("3gp") || end.equals("mp4")) {
			type = "video";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			type = "image";
		} else if (end.equals("apk")) {
			/* android.permission.INSTALL_PACKAGES */
			type = "application/vnd.android.package-archive";
		} else {
			type = "*";
		}
		/* 如果无法直接打开，就跳出软件列表给用户选择 */
		if (end.equals("apk")) {
		} else {
			type += "/*";
		}
		return type;
	}
}
