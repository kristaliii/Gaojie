package com.example.demo.activity2;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import com.example.demo.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Xml;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StudentActivity extends Activity {

	private TextView tv;
	private EditText et3;
	private EditText et2;
	private EditText et1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stumain);

		et1 = (EditText) findViewById(R.id.et1);
		et2 = (EditText) findViewById(R.id.et2);
		et3 = (EditText) findViewById(R.id.et3);
		tv = (TextView) findViewById(R.id.tv1);
	}

	public void cun(View view) {
		try {
			String path = Environment.getExternalStorageDirectory().getPath();
			FileOutputStream out = new FileOutputStream(path + "/stu.xml");
			XmlSerializer s = Xml.newSerializer();
			s.setOutput(out, "GBK");
			s.startTag(null, "student").startTag(null, "name")
					.text(et1.getText().toString()).endTag(null, "name")
					.startTag(null, "gender").text(et2.getText().toString())
					.endTag(null, "gender").startTag(null, "age")
					.text(et3.getText().toString()).endTag(null, "age")
					.endTag(null, "student");
			s.flush();
			out.close();
			Toast.makeText(this, "保存完成", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}

	public void qu(View view) {
		try {
			tv.setText("");
			String path = Environment.getExternalStorageDirectory().getPath();
			FileInputStream in = new FileInputStream(path + "/stu.xml");
			XmlPullParser p = Xml.newPullParser();
			p.setInput(in, "GBK");
			int type;
			while ((type = p.next()) != XmlPullParser.END_DOCUMENT) {

				if (type == XmlPullParser.START_TAG) {
					String n = p.getName();
					if ("name".equals(n)) {
						tv.append("\n姓名：" + p.nextText());
					} else if ("gender".equals(n)) {
						tv.append("\n性别：" + p.nextText());
					} else if ("age".equals(n)) {
						tv.append("\n年龄：" + p.nextText());
					}
				}
			}
			in.close();
			Toast.makeText(this, "读取完成", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(this, "读取失败", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
}
