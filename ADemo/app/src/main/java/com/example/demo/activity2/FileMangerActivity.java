package com.example.demo.activity2;

import java.io.File;
import java.io.FileInputStream;

import org.xmlpull.v1.XmlPullParser;

import com.example.demo.R;
import com.example.demo.utils.FileUtils;
import com.example.demo.utils.ToastUtil;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Xml;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class FileMangerActivity extends Activity {

	private static final int FILE_SELECT_CODE = 0;
	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity2_filemanager);
		tv = (TextView) findViewById(R.id.textView1);
		showFileChooser();
	}
	
	/** 调用文件选择软件来选择文件 **/
	private void showFileChooser() {
		Intent intent ;
		intent = new Intent(Intent.ACTION_GET_CONTENT);
//		intent.setType("*/*");
		Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()+ "/片仔癀/");
		intent.setDataAndType(uri, "*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		try {
			startActivityForResult(Intent.createChooser(intent, "请选择一个要上传的文件"),FILE_SELECT_CODE);
		} catch (android.content.ActivityNotFoundException ex) {
			ToastUtil.showToast(this, "请安装文件管理器");
		}
	}
	
	private void openFile(){
		 File root = new File(Environment.getExternalStorageDirectory().getPath()+ "/abc/");
			   Uri uri = Uri.fromFile(root);
			   Intent intent = new Intent();
			   intent.setAction(android.content.Intent.ACTION_VIEW);
			   intent.setData(uri);
			   startActivityForResult(intent, 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * xmlpull 解析xml 文件
	 * @param view
	 */
	public void duQu(String path) {
		try {
			FileInputStream in = new FileInputStream(path);
			XmlPullParser p = Xml.newPullParser();
			p.setInput(in, "GBK");
			tv.setText("");// 清空
			int type;
			while ((type = p.next()) != 1) {
				if (type == XmlPullParser.START_TAG) {
					String n = p.getName();
					if ("email".equals(n)) {
						String date = p.getAttributeValue(null, "date");
						String time = p.getAttributeValue(null, "time");
						tv.append("\n日期：" + date);
						tv.append("\n时间：" + time);
					} else if ("from".equals(n)) {
						tv.append("\n发件人：" + p.nextText());
					} else if ("to-email".equals(n)) {
						tv.append("\n收件人：" + p.nextText());
					} else if ("subject".equals(n)) {
						tv.append("\n标题：" + p.nextText());
					} else if ("body".equals(n)) {
						tv.append("\n内容：" + p.nextText());
					}
				}
			}

			Toast.makeText(this, "读取完成", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(this, "读取失败", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
}
