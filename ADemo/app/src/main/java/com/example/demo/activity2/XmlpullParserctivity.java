package com.example.demo.activity2;

import java.io.FileInputStream;

import org.xmlpull.v1.XmlPullParser;

import com.example.demo.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Xml;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class XmlpullParserctivity extends Activity {

	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity2_main);

		tv = (TextView) findViewById(R.id.textView1);
	}

	/**
	 * xmlpull 解析xml 文件
	 * @param view
	 */
	public void duQu(View view) {
		try {
			String path = Environment.getExternalStorageDirectory().getPath();
			FileInputStream in = new FileInputStream(path + "/email.xml");
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

	public void shengCheng(View view) {

	}
}
