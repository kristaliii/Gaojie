package com.example.demo.activity2;

import com.example.demo.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

public class BroadcastActivity extends Activity {
	private TextView textView1;
	private static String ACTION ="com.android.server.scannerservice.hayao.broadcast";
	private static String ACTION_SETTING ="com.android.scanner.service_settings";
	private static String KEY ="scannerdata";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity2_broadcast);
		textView1 = (TextView) findViewById(R.id.textView1);
		
		Intent intent = new Intent(ACTION_SETTING);
		intent.putExtra("action_barcode_broadcast", ACTION); // 修改扫描工具广播名称
		intent.putExtra("key_barcode_broadcast", KEY);
//		intent.putExtra("end_event", true); // 默认广播时条码后添加回车事件
		sendBroadcast(intent);
		
		IntentFilter filter = new IntentFilter(ACTION);
		registerReceiver(receiver, filter);
	}
	
	BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
//			String string = intent.getExtras().getString("KEY");
			String extra = intent.getStringExtra(KEY);
			textView1.setText(extra);
		}
	};
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}
}
