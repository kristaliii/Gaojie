package com.example.demo.activity;

import java.util.List;

import com.example.demo.R;
import com.example.demo.engine.WebServiceHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SoapActivityTest extends Activity {
	protected static final int SEND_SUCCESS = 0;
	private List<String> proviceList;
	
	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_soap);
		
		final ListView lv_soap = (ListView) findViewById(R.id.lv_soap);
		
		final Handler handler = new Handler(){
			public void handleMessage(Message msg) {
				if (msg.what == SEND_SUCCESS) {
					ArrayAdapter<String > adapter = new ArrayAdapter<String>(SoapActivityTest.this, android.R.layout.simple_list_item_1, proviceList);
					lv_soap.setAdapter(adapter);
				}
			}
		};
		new Thread(){
			public void run() {
				proviceList = WebServiceHelper.getCitys("安徽");
				Log.e("tag", "proviceList:"+proviceList);
				Message msg = Message.obtain();
				msg.what = SEND_SUCCESS;
				handler.sendMessage(msg);
			}
		}.start();
	}
	
}
